package com.billing.repository;

import com.billing.entity.ExchangeItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeItemRepository extends JpaRepository<ExchangeItem, Long> {
}
