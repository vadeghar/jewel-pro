package com.billing.service;

import com.billing.entity.Payment;
import com.billing.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserService userService;

    public PaymentService(PaymentRepository paymentRepository, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
    }

    public List<Payment> getPaymentListBySourceAndSourceId(String source, Long sourceId) {
        return paymentRepository.findBySourceAndSourceId(source, sourceId);
    }

    public List<Payment> getPaymentListBySource(String source) {
        if (userService.isUserIsAdmin()) {
            return paymentRepository.findBySource(source);
        } else {
            String currentUser = userService.getCurrentUser();
            return getPaymentListBySourceAndReceivedBy(source, currentUser);
        }

    }

    private List<Payment> getPaymentListBySourceAndReceivedBy(String source, String receivedBy) {
        return paymentRepository.findBySourceAndReceivedBy(source, receivedBy);
    }
}
