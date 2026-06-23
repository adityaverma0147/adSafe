package com.rtbplatform.auction.controller;

import com.rtbplatform.auction.dto.BidRequest;
import com.rtbplatform.auction.dto.BidResponse;
import com.rtbplatform.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/bid")
    public ResponseEntity<BidResponse> conductAuction(@RequestBody BidRequest bidRequest) {
        log.debug("Received ad request: {}", bidRequest.getId());
        
        try {
            BidResponse winner = auctionService.conductAuction(bidRequest);
            if (winner != null) {
                return ResponseEntity.ok(winner);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (IllegalArgumentException ex) {
            log.error("Invalid auction request: {}", ex.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Auction engine failure", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
