package com.rtbplatform.analytics.service;

import com.rtbplatform.auction.entity.Auction;
import com.rtbplatform.fraud.entity.FraudEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String AUCTION_TOPIC = "rtb.auctions.won";
    private static final String FRAUD_TOPIC = "rtb.fraud.events";

    public void publishAuctionEvent(Auction auction) {
        log.debug("Publishing auction event for ID: {}", auction.getId());
        // In a real system, we'd map to a DTO first to avoid lazy init issues
        kafkaTemplate.send(AUCTION_TOPIC, auction.getId().toString(), auction)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish auction event: {}", auction.getId(), ex);
                    }
                });
    }

    public void publishFraudEvent(FraudEvent fraudEvent) {
        log.debug("Publishing fraud event for ad request: {}", fraudEvent.getAdRequestId());
        kafkaTemplate.send(FRAUD_TOPIC, fraudEvent.getAdRequestId(), fraudEvent)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish fraud event: {}", fraudEvent.getAdRequestId(), ex);
                    }
                });
    }
}
