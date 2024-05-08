package com.billing.repository;

import com.billing.entity.Stock;
import com.billing.enums.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT s FROM Stock s JOIN PurchaseItem pi on pi.stock.id = s.id WHERE s.stockStatus = :stockStatus and s.code LIKE %:code%")
    List<Stock> findAllByCodeLike(@Param("code") String code, @Param("stockStatus") StockStatus stockStatus);
}
