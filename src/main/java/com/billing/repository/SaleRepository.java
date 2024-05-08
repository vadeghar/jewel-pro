package com.billing.repository;

import com.billing.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT MAX(s.id) FROM Sale s")
    Long findMaxId();
}
