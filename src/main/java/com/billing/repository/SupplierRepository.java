package com.billing.repository;

import com.billing.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query("SELECT s FROM Supplier s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<Supplier> findByNameContainingIgnoreCase(@Param("value") String value);

    Supplier findByName(String name);
}
