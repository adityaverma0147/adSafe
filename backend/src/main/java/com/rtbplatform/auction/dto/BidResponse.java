package com.rtbplatform.auction.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BidResponse {
    private String id; // The auction request ID this bid is for
    private UUID dspId; // The DSP bidding
    private BigDecimal price; // The bid price in USD
    private UUID campaignId; // The campaign this bid represents
    private UUID creativeId; // The actual ad creative
    private String adMarkup; // The HTML/JS to render
}
