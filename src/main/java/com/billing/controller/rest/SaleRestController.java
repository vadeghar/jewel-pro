package com.billing.controller.rest;

import com.billing.dto.PurchaseDTO;
import com.billing.dto.SaleDTO;
import com.billing.entity.Payment;
import com.billing.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/sale")
public class SaleRestController {

    private final SaleService saleService;

    public SaleRestController(SaleService saleService) {
        this.saleService = saleService;
    }

    // Get all sales
    @GetMapping
    public List<SaleDTO> getAllSales() {
        return saleService.getAllSales();
    }

    @GetMapping("/customer/{id}/sales")
    public List<SaleDTO> customerSales(@PathVariable Long id) {
        return saleService.getAllSalesByCustomerId(id);
    }

    // Get sale by ID
    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        SaleDTO sale = saleService.getSaleById(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    // Create a new sale
    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@RequestBody SaleDTO sale) {
        SaleDTO savedSale = saleService.createSale(sale);
        return new ResponseEntity<>(savedSale, HttpStatus.CREATED);
    }

    // Update an existing sale
    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> updateSale(@PathVariable Long id, @RequestBody SaleDTO sale) {
        SaleDTO updatedSale = saleService.updateSale(id, sale);
        if (updatedSale != null) {
            return ResponseEntity.ok(updatedSale);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a sale
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        boolean deleted = saleService.deleteSale(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/payment-list")
    public ResponseEntity<List<Payment>> getPaymentListBySaleId(@PathVariable Long id) {
        List<Payment> paymentList = saleService.getPaymentListBySaleId(id);
        return ResponseEntity.of(Optional.of(paymentList));
    }

    @PostMapping("/{id}/payment")
    public ResponseEntity<SaleDTO> savePayment(@PathVariable Long id, @RequestBody Payment payment) {
        SaleDTO saleDTO = saleService.addPaymentAndReturnSale(id, payment);
        return ResponseEntity.of(Optional.of(saleDTO));
    }


}
