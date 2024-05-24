package com.billing.controller.rest;

import com.billing.constant.Constants;
import com.billing.entity.Payment;
import com.billing.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentRestController {
    @Autowired
    PaymentService paymentService;

    @RequestMapping("sales")
    public ResponseEntity<List<Payment>> getSalePayment() {
        return ResponseEntity.ok(paymentService.getPaymentListBySource(Constants.SOURCE_SALE));
    }

    @RequestMapping("purchases")
    public ResponseEntity<List<Payment>> getPurchasePayment() {
        return ResponseEntity.ok(paymentService.getPaymentListBySource(Constants.SOURCE_PURCHASE));
    }


}
