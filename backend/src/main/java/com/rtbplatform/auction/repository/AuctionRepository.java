package com.rtbplatform.auction.repository;

import com.rtbplatform.auction.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, UUID> {
    Optional<Auction> findByAdRequestId(String adRequestId);
}
