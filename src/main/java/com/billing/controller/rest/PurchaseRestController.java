package com.billing.controller.rest;

import com.billing.dto.PurchaseDTO;
import com.billing.dto.PurchaseItemDTO;
import com.billing.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase")
@Slf4j
public class PurchaseRestController {

    private final PurchaseService purchaseService;

    public PurchaseRestController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPurchase(@RequestParam(required = false) Long id) {
        log.debug("PurchaseController >> getAllPurchase >> {} >>", id);
        if (id != null && id > 0) {
            PurchaseDTO purchaseDTO = purchaseService.getByPurchaseId(id);
            return ResponseEntity.ok().body(purchaseDTO);
        }
        List<PurchaseDTO> purchaseDTOList = purchaseService.getAllPurchases();
        log.debug("PurchaseController << getAllPurchase <<");
        return ResponseEntity.ok().body(purchaseDTOList);
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
}
