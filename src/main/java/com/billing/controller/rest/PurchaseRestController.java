package com.billing.controller.rest;

import com.billing.dto.PurchaseDTO;
import com.billing.dto.PurchaseItemDTO;
import com.billing.entity.Payment;
import com.billing.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/purchase")
@Slf4j
public class PurchaseRestController {

    private final PurchaseService purchaseService;

    public PurchaseRestController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPurchase() {
        log.debug("PurchaseController >> getAllPurchase >>");
        List<PurchaseDTO> purchaseDTOList = purchaseService.getAllPurchases();
        log.debug("PurchaseController << getAllPurchase <<");
        return ResponseEntity.ok().body(purchaseDTOList);
    }

    @GetMapping("{id}")
    public ResponseEntity<PurchaseDTO> getByid(@PathVariable Long id) {
        PurchaseDTO purchaseDTO = purchaseService.getByPurchaseId(id);
        return ResponseEntity.ok().body(purchaseDTO);
    }

    @PostMapping("/save")
    public ResponseEntity<PurchaseDTO> save(@RequestBody PurchaseDTO purchaseDTO) {
        log.info("PurchaseController >> save >> RequestBody: {} >>", purchaseDTO);
        Long id = purchaseService.savePurchase(purchaseDTO);
        log.info("<< PurchaseController << save <<");
        return ResponseEntity.ok(PurchaseDTO.builder().id(id).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> markPurchaseAsDeleted(@PathVariable("id") Long id) {
        log.info("PurchaseController >> deleteItemType >> purchaseId: {} >>", id);
        purchaseService.softDelete(id);
        log.info("PurchaseController << deleteItemType << purchaseId: {} <<", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{purchaseId}/items")
    public ResponseEntity<?> savePurchaseItems(@PathVariable("purchaseId") Long purchaseId, @RequestBody List<PurchaseItemDTO> purchaseItems) {
        log.info("PurchaseController >> addPurchaseItems >> purchaseId: {} >> items >> {} >>", purchaseId, purchaseItems);
        PurchaseDTO purchaseDTO = purchaseService.savePurchaseItems(purchaseId, purchaseItems);
        log.info("PurchaseController << addPurchaseItems << purchaseId: {} <<", purchaseId);
        return new ResponseEntity<>(purchaseDTO, HttpStatus.OK);
    }

    @GetMapping("/supplier/{id}")
    public List<PurchaseDTO> customerSales(@PathVariable Long id) {
        return purchaseService.getAllPurchasesBySupplierId(id);
    }

    @GetMapping("/{id}/payment-list")
    public ResponseEntity<List<Payment>> getPaymentListBySaleId(@PathVariable Long id) {
        List<Payment> paymentList = purchaseService.getPaymentListByPurchaseId(id);
        return ResponseEntity.of(Optional.of(paymentList));
    }

    @PostMapping("/{id}/payment")
    public ResponseEntity<PurchaseDTO> savePayment(@PathVariable Long id, @RequestBody Payment payment) {
        PurchaseDTO purchaseDTO = purchaseService.addPaymentAndReturnPurchase(id, payment);
        return ResponseEntity.of(Optional.of(purchaseDTO));
    }
}
