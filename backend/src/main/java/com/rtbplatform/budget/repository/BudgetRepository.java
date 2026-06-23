package com.rtbplatform.budget.repository;

import com.rtbplatform.budget.entity.Budget;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    
    Optional<Budget> findByCampaignId(UUID campaignId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Budget b WHERE b.campaign.id = :campaignId")
    Optional<Budget> findByCampaignIdWithPessimisticLock(@Param("campaignId") UUID campaignId);
}
