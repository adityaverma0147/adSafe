package com.rtbplatform.campaign.dto;

import com.rtbplatform.campaign.entity.CampaignStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CampaignResponse {
    private UUID id;
    private String name;
    private CampaignStatus status;
    private BigDecimal budget;
    private BigDecimal spent;
    private long impressions;
}
