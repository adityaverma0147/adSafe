package com.rtbplatform.publisher.repository;

import com.rtbplatform.publisher.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, UUID> {
    Optional<Publisher> findByUserId(UUID userId);
}
