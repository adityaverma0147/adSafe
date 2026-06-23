package com.rtbplatform.campaign.repository;

import com.rtbplatform.campaign.entity.Campaign;
import com.rtbplatform.campaign.entity.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    List<Campaign> findByStatus(CampaignStatus status);
}
