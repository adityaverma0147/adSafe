package com.rtbplatform.budget.service;

import com.rtbplatform.budget.entity.Budget;
import com.rtbplatform.budget.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private static final int MAX_RETRIES = 3;

    @Transactional
    public boolean deductBudget(UUID campaignId, BigDecimal amount) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                Budget budget = budgetRepository.findByCampaignId(campaignId)
                        .orElseThrow(() -> new IllegalArgumentException("Budget not found for campaign"));

                if (budget.getTotalSpent().add(amount).compareTo(budget.getTotalLimit()) > 0 ||
                    budget.getDailySpent().add(amount).compareTo(budget.getDailyLimit()) > 0) {
                    log.warn("Insufficient budget for campaign {}", campaignId);
                    return false;
                }

                budget.setTotalSpent(budget.getTotalSpent().add(amount));
                budget.setDailySpent(budget.getDailySpent().add(amount));
                budgetRepository.saveAndFlush(budget);
                return true;

            } catch (ObjectOptimisticLockingFailureException ex) {
                attempt++;
                log.warn("Optimistic lock failure for campaign {}, retrying... ({}/{})", campaignId, attempt, MAX_RETRIES);
                if (attempt == MAX_RETRIES) {
                    log.error("Failed to deduct budget for campaign {} after {} retries", campaignId, MAX_RETRIES);
                    return false;
                }
                try {
                    Thread.sleep(10); // Small backoff before retry
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }
}
