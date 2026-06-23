package com.rtbplatform.auction.service;

import com.rtbplatform.analytics.service.AnalyticsProducerService;
import com.rtbplatform.auction.dto.BidRequest;
import com.rtbplatform.auction.dto.BidResponse;
import com.rtbplatform.auction.entity.Auction;
import com.rtbplatform.auction.entity.AuctionBid;
import com.rtbplatform.auction.entity.AuctionStatus;
import com.rtbplatform.auction.repository.AuctionBidRepository;
import com.rtbplatform.auction.repository.AuctionRepository;
import com.rtbplatform.budget.service.BudgetService;
import com.rtbplatform.dsp.entity.Dsp;
import com.rtbplatform.dsp.repository.DspRepository;
import com.rtbplatform.dsp.service.DspCallerService;
import com.rtbplatform.publisher.entity.Publisher;
import com.rtbplatform.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final DspRepository dspRepository;
    private final PublisherRepository publisherRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionBidRepository auctionBidRepository;
    private final DspCallerService dspCallerService;
    private final BudgetService budgetService;
    private final AnalyticsProducerService analyticsProducerService;

    @Cacheable(value = "activeDsps", unless = "#result == null or #result.isEmpty()")
    public List<Dsp> getActiveDsps() {
        return dspRepository.findByIsActiveTrue();
    }

    public BidResponse conductAuction(BidRequest bidRequest) {
        long startTime = System.currentTimeMillis();

        // 1. Validate Publisher
        Publisher publisher = publisherRepository.findById(bidRequest.getSite().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid publisher ID"));

        // 2. Initialize Auction Record
        Auction auction = new Auction();
        auction.setPublisher(publisher);
        auction.setAdRequestId(bidRequest.getId());
        auction.setStatus(AuctionStatus.PENDING);
        auction = auctionRepository.save(auction);

        // 3. Fetch Active DSPs (cached)
        List<Dsp> activeDsps = getActiveDsps();
        if (activeDsps.isEmpty()) {
            finalizeAuction(auction, AuctionStatus.NO_BIDS, null, startTime);
            return null;
        }

        // 4. Scatter-Gather: Call all DSPs in parallel
        List<CompletableFuture<BidResponse>> futures = activeDsps.stream()
                .map(dsp -> dspCallerService.callDspAsync(dsp, bidRequest))
                .collect(Collectors.toList());

        // Wait for all to complete (or timeout handled individually inside callDspAsync)
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 5. Collect valid responses
        List<BidResponse> responses = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();

        if (responses.isEmpty()) {
            finalizeAuction(auction, AuctionStatus.NO_BIDS, null, startTime);
            return null;
        }

        // 6. Find highest bid
        BidResponse winningBid = null;
        for (BidResponse bid : responses) {
            if (winningBid == null || bid.getPrice().compareTo(winningBid.getPrice()) > 0) {
                winningBid = bid;
            }
        }

        // 7. Deduct Budget
        boolean budgetDeducted = budgetService.deductBudget(winningBid.getCampaignId(), winningBid.getPrice());
        if (!budgetDeducted) {
            log.warn("Highest bid discarded due to insufficient budget. Campaign: {}", winningBid.getCampaignId());
            finalizeAuction(auction, AuctionStatus.FAILED, null, startTime);
            return null;
        }

        // 8. Record the winning bid
        final UUID winningDspId = winningBid.getDspId();
        AuctionBid auctionBid = new AuctionBid();
        auctionBid.setAuction(auction);
        auctionBid.setDsp(activeDsps.stream().filter(d -> d.getId().equals(winningDspId)).findFirst().orElseThrow());
        auctionBid.setBidPrice(winningBid.getPrice());
        auctionBid.setLatencyMs((int) (System.currentTimeMillis() - startTime));
        auctionBid.setIsWinner(true);
        auctionBid = auctionBidRepository.save(auctionBid);

        // 9. Finalize Auction & Publish Event
        finalizeAuction(auction, AuctionStatus.COMPLETED, auctionBid, startTime);

        return winningBid;
    }

    private void finalizeAuction(Auction auction, AuctionStatus status, AuctionBid winningBid, long startTime) {
        auction.setStatus(status);
        auction.setWinningBid(winningBid);
        auction.setDurationMs((int) (System.currentTimeMillis() - startTime));
        auctionRepository.save(auction);
        analyticsProducerService.publishAuctionEvent(auction);
    }
}
