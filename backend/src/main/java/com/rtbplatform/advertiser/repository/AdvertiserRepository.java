package com.rtbplatform.advertiser.repository;

import com.rtbplatform.advertiser.entity.Advertiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdvertiserRepository extends JpaRepository<Advertiser, UUID> {
    Optional<Advertiser> findByUserId(UUID userId);
}
