package com.rtbplatform.auction.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BidRequest {
    private String id; // The auction/request ID
    private PublisherInfo site;
    private UserInfo user;

    @Data
    @Builder
    public static class PublisherInfo {
        private UUID id;
        private String domain;
    }

    @Data
    @Builder
    public static class UserInfo {
        private String ip;
        private String ua;
    }
}
