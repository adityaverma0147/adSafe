package com.rtbplatform.auction.service;

import com.rtbplatform.analytics.service.AnalyticsProducerService;
import com.rtbplatform.auction.dto.BidRequest;
import com.rtbplatform.auction.dto.BidResponse;
import com.rtbplatform.auction.entity.Auction;
import com.rtbplatform.auction.repository.AuctionBidRepository;
import com.rtbplatform.auction.repository.AuctionRepository;
import com.rtbplatform.budget.service.BudgetService;
import com.rtbplatform.dsp.entity.Dsp;
import com.rtbplatform.dsp.repository.DspRepository;
import com.rtbplatform.dsp.service.DspCallerService;
import com.rtbplatform.publisher.entity.Publisher;
import com.rtbplatform.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock private DspRepository dspRepository;
    @Mock private PublisherRepository publisherRepository;
    @Mock private AuctionRepository auctionRepository;
    @Mock private AuctionBidRepository auctionBidRepository;
    @Mock private DspCallerService dspCallerService;
    @Mock private BudgetService budgetService;
    @Mock private AnalyticsProducerService analyticsProducerService;

    @InjectMocks
    private AuctionService auctionService;

    private BidRequest bidRequest;
    private Publisher publisher;
    private Dsp dsp1;
    private Dsp dsp2;

    @BeforeEach
    void setUp() {
        publisher = new Publisher();
        publisher.setId(UUID.randomUUID());
        publisher.setDomainName("example.com");

        bidRequest = BidRequest.builder()
                .id("req-123")
                .site(BidRequest.PublisherInfo.builder().id(publisher.getId()).build())
                .build();

        dsp1 = new Dsp();
        dsp1.setId(UUID.randomUUID());
        dsp1.setName("DSP 1");

        dsp2 = new Dsp();
        dsp2.setId(UUID.randomUUID());
        dsp2.setName("DSP 2");
    }

    @Test
    void testConductAuction_SelectsHighestBid() {
        // Arrange
        when(publisherRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));
        when(auctionRepository.save(any(Auction.class))).thenAnswer(i -> i.getArgument(0));
        when(dspRepository.findByIsActiveTrue()).thenReturn(List.of(dsp1, dsp2));

        BidResponse bid1 = new BidResponse();
        bid1.setDspId(dsp1.getId());
        bid1.setPrice(new BigDecimal("1.50"));
        bid1.setCampaignId(UUID.randomUUID());

        BidResponse bid2 = new BidResponse();
        bid2.setDspId(dsp2.getId());
        bid2.setPrice(new BigDecimal("2.50")); // Highest
        bid2.setCampaignId(UUID.randomUUID());

        when(dspCallerService.callDspAsync(eq(dsp1), any())).thenReturn(CompletableFuture.completedFuture(bid1));
        when(dspCallerService.callDspAsync(eq(dsp2), any())).thenReturn(CompletableFuture.completedFuture(bid2));
        
        when(budgetService.deductBudget(eq(bid2.getCampaignId()), eq(bid2.getPrice()))).thenReturn(true);

        // Act
        BidResponse winner = auctionService.conductAuction(bidRequest);

        // Assert
        assertNotNull(winner);
        assertEquals(new BigDecimal("2.50"), winner.getPrice());
        assertEquals(dsp2.getId(), winner.getDspId());
        
        verify(budgetService, times(1)).deductBudget(bid2.getCampaignId(), bid2.getPrice());
        verify(analyticsProducerService, times(1)).publishAuctionEvent(any());
    }
}
