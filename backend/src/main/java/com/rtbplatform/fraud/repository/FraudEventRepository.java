package com.rtbplatform.fraud.repository;

import com.rtbplatform.fraud.entity.FraudEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FraudEventRepository extends JpaRepository<FraudEvent, UUID> {
}
