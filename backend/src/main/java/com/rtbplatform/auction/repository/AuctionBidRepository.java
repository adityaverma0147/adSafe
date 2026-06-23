package com.rtbplatform.auction.repository;

import com.rtbplatform.auction.entity.AuctionBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuctionBidRepository extends JpaRepository<AuctionBid, UUID> {
    List<AuctionBid> findByAuctionId(UUID auctionId);
}
