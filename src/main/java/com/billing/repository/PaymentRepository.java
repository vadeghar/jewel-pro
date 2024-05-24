package com.billing.repository;

import com.billing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findBySourceAndSourceId(String source, Long sourceId);

    List<Payment> findBySource(String source);
    List<Payment> findBySourceAndReceivedBy(String source, String receivedBy);

}
