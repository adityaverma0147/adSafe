package com.rtbplatform.dsp.entity;

import com.rtbplatform.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dsps")
public class Dsp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "endpoint_url", nullable = false, length = 255)
    private String endpointUrl;

    @Column(name = "timeout_ms", nullable = false)
    private Integer timeoutMs = 50;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
