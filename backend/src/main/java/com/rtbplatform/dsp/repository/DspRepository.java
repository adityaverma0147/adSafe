package com.rtbplatform.dsp.repository;

import com.rtbplatform.dsp.entity.Dsp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DspRepository extends JpaRepository<Dsp, UUID> {
    List<Dsp> findByIsActiveTrue();
}
