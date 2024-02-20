package com.billing.repository;

import com.billing.constant.Metal;
import com.billing.entity.MetalRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetalRateRepository extends JpaRepository<MetalRate, Long> {
    Optional<MetalRate> findTopByItemMetalOrderByLastUpdateAtDesc(Metal itemMetal);
}
