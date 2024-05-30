package com.billing.repository;

import com.billing.entity.EstimationItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimationItemRepository extends JpaRepository<EstimationItem, Long> {

}
