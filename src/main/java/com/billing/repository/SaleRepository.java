package com.billing.repository;

import com.billing.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT MAX(s.id) FROM Sale s")
    Long findMaxId();

    List<Sale> findByCustomerId(Long customerId);
}
