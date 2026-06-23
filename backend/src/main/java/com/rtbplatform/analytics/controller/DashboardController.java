package com.rtbplatform.analytics.controller;

import com.rtbplatform.auction.repository.AuctionRepository;
import com.rtbplatform.budget.repository.BudgetRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final AuctionRepository auctionRepository;
    private final BudgetRepository budgetRepository;

    @GetMapping("/stats")
    public DashboardStats getStats() {
        long totalAuctions = auctionRepository.count();
        
        // Calculate total spend across all budgets
        BigDecimal totalSpend = budgetRepository.findAll().stream()
                .map(b -> b.getTotalSpent())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardStats.builder()
                .totalAuctions(totalAuctions)
                .winRate(totalAuctions > 0 ? 68.2 : 0.0) // Placeholder logic for MVP
                .avgLatency(42.0) // Mocked latency for MVP until Prometheus integration
                .totalSpend(totalSpend)
                .build();
    }

    @Data
    @Builder
    public static class DashboardStats {
        private long totalAuctions;
        private double winRate;
        private double avgLatency;
        private BigDecimal totalSpend;
    }
}
