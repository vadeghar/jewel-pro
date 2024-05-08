package com.billing.repository;

import com.billing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByPhone(String phone);
    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:name%")
    List<Customer> findAllByNameLike(@Param("name") String name);
}

