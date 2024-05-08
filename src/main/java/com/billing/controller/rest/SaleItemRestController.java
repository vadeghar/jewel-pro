package com.billing.controller.rest;

import com.billing.dto.SaleItemDTO;
import com.billing.service.SaleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sales/{saleId}/sale-items")
public class SaleItemRestController {

    @Autowired
    private SaleItemService saleItemService;

    // Get all sale items for a specific sale
    @GetMapping
    public List<SaleItemDTO> getAllSaleItems(@PathVariable Long saleId) {
        return saleItemService.getAllSaleItemsForSale(saleId);
    }

    // Get sale item by ID for a specific sale
    @GetMapping("/{itemId}")
    public ResponseEntity<SaleItemDTO> getSaleItemById(@PathVariable Long saleId, @PathVariable Long itemId) {
        Optional<SaleItemDTO> saleItem = saleItemService.getSaleItemByIdForSale(saleId, itemId);
        return saleItem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new sale item for a specific sale
    @PostMapping
    public ResponseEntity<SaleItemDTO> createSaleItem(@PathVariable Long saleId, @RequestBody SaleItemDTO saleItem) {
        SaleItemDTO savedSaleItem = saleItemService.createSaleItemForSale(saleId, saleItem);
        return new ResponseEntity<>(savedSaleItem, HttpStatus.CREATED);
    }

    // Update an existing sale item for a specific sale
    @PutMapping("/{itemId}")
    public ResponseEntity<SaleItemDTO> updateSaleItem(@PathVariable Long saleId, @PathVariable Long itemId, @RequestBody SaleItemDTO saleItem) {
        SaleItemDTO updatedSaleItem = saleItemService.updateSaleItemForSale(saleId, itemId, saleItem);
        if (updatedSaleItem != null) {
            return ResponseEntity.ok(updatedSaleItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a sale item for a specific sale
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable Long saleId, @PathVariable Long itemId) {
        boolean deleted = saleItemService.deleteSaleItemForSale(saleId, itemId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

