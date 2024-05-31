package com.billing.repository;

import com.billing.entity.Estimation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EstimationRepository extends JpaRepository<Estimation, Long> {
    @Query("SELECT MAX(e.id) FROM Estimation e")
    Long findMaxId();
}
