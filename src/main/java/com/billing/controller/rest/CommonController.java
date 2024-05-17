package com.billing.controller.rest;

import com.billing.entity.PurchaseItem;
import com.billing.entity.Supplier;
import com.billing.service.PurchaseItemService;
import com.billing.service.PurchaseService;
import com.billing.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("menu-items")
    public Map<String, List<String>> getMenuItems() {
        Map<String, List<String>> menuItems = new HashMap<>();
        menuItems.put("purchaseSec", List.of("newSupplier","supplierList","newPurchase","purchaseList"));
        menuItems.put("estimationSec", List.of("newEstimation", "EstimationList"));
        menuItems.put("saleSec", List.of("newCustomer", "customerList", "salesList","newSale"));
        menuItems.put("orderSec", List.of("newWorker","workerList","orderList","newOrder"));
        menuItems.put("paymentSec", List.of("salesPayment", "purchasePayment"));
        menuItems.put("settingsSec", new ArrayList<>());
        menuItems.put("reportsSec", new ArrayList<>());
        menuItems.put("plSec", new ArrayList<>());
        menuItems.put("chartSec", new ArrayList<>());
        return menuItems;
    }


}
