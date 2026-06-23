package com.rtbplatform.budget.entity;

import com.rtbplatform.campaign.entity.Campaign;
import com.rtbplatform.common.entity.BaseEntity;
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
@Table(name = "budgets")
public class Budget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false, unique = true)
    private Campaign campaign;

    @Column(name = "total_limit", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalLimit;

    @Column(name = "daily_limit", nullable = false, precision = 19, scale = 4)
    private BigDecimal dailyLimit;

    @Column(name = "total_spent", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "daily_spent", nullable = false, precision = 19, scale = 4)
    private BigDecimal dailySpent = BigDecimal.ZERO;

    @Version
    private Long version;
}
