package com.rtbplatform.campaign.repository;

import com.rtbplatform.campaign.entity.Creative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CreativeRepository extends JpaRepository<Creative, UUID> {
    List<Creative> findByCampaignIdAndIsActiveTrue(UUID campaignId);
}
