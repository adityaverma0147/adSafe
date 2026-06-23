package com.rtbplatform.campaign.controller;

import com.rtbplatform.budget.entity.Budget;
import com.rtbplatform.budget.repository.BudgetRepository;
import com.rtbplatform.campaign.dto.CampaignResponse;
import com.rtbplatform.campaign.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignRepository campaignRepository;
    private final BudgetRepository budgetRepository;

    @GetMapping
    public List<CampaignResponse> getAllCampaigns() {
        return campaignRepository.findAll().stream().map(campaign -> {
            Budget budget = budgetRepository.findByCampaignId(campaign.getId()).orElse(null);
            return CampaignResponse.builder()
                    .id(campaign.getId())
                    .name(campaign.getName())
                    .status(campaign.getStatus())
                    .budget(budget != null ? budget.getTotalLimit() : null)
                    .spent(budget != null ? budget.getTotalSpent() : null)
                    .impressions(0) // Simplified for MVP
                    .build();
        }).collect(Collectors.toList());
    }
}
