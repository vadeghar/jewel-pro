package com.billing.repository;

import com.billing.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

//    @Query("SELECT pi FROM PurchaseItem pi WHERE LOWER(pi.name) LIKE LOWER(CONCAT('%', :nameOrCode, '%')) OR LOWER(pi.code) LIKE LOWER(CONCAT('%', :nameOrCode, '%'))")
//    List<PurchaseItem> findByNameOrCode(@Param("nameOrCode") String nameOrCode);

}
