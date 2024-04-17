package com.billing.controller.rest;

import com.billing.dto.PurchaseDTO;
import com.billing.dto.PurchaseItemDTO;
import com.billing.entity.PurchaseItem;
import com.billing.entity.Supplier;
import com.billing.service.PurchaseItemService;
import com.billing.service.PurchaseService;
import com.billing.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/api/v1")
@RestController
@Slf4j
public class CommonController {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private PurchaseItemService purchaseItemService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/suppliers")
    public List<String> suppliers(@RequestParam String term) {
        List<Supplier> supplierList = supplierService.getSuppliersNameLike(term);
        if (!CollectionUtils.isEmpty(supplierList)) {
            return supplierList.stream()
                    .map(supplier -> supplier.getName())
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    @GetMapping("/supplier/autocomplete")
    public List<Supplier> suppliersAutocomplete(@RequestParam String term) {
        return supplierService.getSuppliersNameLike(term);
    }

    @GetMapping("/items")
    public List<PurchaseItem> items(@RequestParam String term) {
        List<PurchaseItem> purchaseItemList = purchaseItemService.findPurchaseItemByCodeOrName(term);
        if (!CollectionUtils.isEmpty(purchaseItemList)) {
            return purchaseItemList;
        }
        return List.of();
    }

    @GetMapping("/purchase")
    public ResponseEntity<?> getAllPurchase(@RequestParam(required = false) Long id) {
        if (id != null && id > 0) {
            PurchaseDTO purchaseDTO = purchaseService.getByPurchaseId(id);
            return ResponseEntity.ok().body(purchaseDTO);
        }
        List<PurchaseDTO> purchaseDTOList = purchaseService.getAllPurchases();
        return ResponseEntity.ok().body(purchaseDTOList);
    }

    @PostMapping("/purchase/save")
    public ResponseEntity<PurchaseDTO> save(@RequestBody PurchaseDTO purchaseDTO) {
        log.info("CommonController >> save >> RequestBody: {}", purchaseDTO);
        Long id = purchaseService.savePurchase(purchaseDTO);
        log.info("<< CommonController << save");
        return ResponseEntity.ok(PurchaseDTO.builder().id(id).build());
    }

    @PostMapping("/purchase/{purchaseId}/items")
    public ResponseEntity<String> addPurchaseItems(@PathVariable("purchaseId") Long purchaseId, @RequestBody List<PurchaseItemDTO> purchaseItems) {
        log.info("CommonController >> addPurchaseItems >> purchaseId: {} >> items >> {}", purchaseId, purchaseItems);

        log.info("CommonController << addPurchaseItems << purchaseId: {}", purchaseId);
        return new ResponseEntity<>("Purchase items added successfully", HttpStatus.OK);
    }
}
