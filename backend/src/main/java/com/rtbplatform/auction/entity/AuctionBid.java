package com.rtbplatform.auction.entity;

import com.rtbplatform.common.entity.BaseEntity;
import com.rtbplatform.dsp.entity.Dsp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "auction_bids")
public class AuctionBid extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dsp_id", nullable = false)
    private Dsp dsp;

    @Column(name = "bid_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal bidPrice;

    @Column(name = "latency_ms", nullable = false)
    private Integer latencyMs;

    @Column(name = "is_winner", nullable = false)
    private Boolean isWinner = false;
}
