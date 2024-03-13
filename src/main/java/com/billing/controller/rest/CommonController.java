package com.billing.controller.rest;

import com.billing.entity.Supplier;
import com.billing.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/api/v1")
@RestController
public class CommonController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/suppliers")
    public List<String> autocomplete(@RequestParam String term) {
        List<Supplier> supplierList = supplierService.getSuppliersNameLike(term);
        if (!CollectionUtils.isEmpty(supplierList)) {
            return supplierList.stream()
                    .map(supplier -> supplier.getName())
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
