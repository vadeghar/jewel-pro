package com.billing.controller.rest;

import com.billing.entity.Supplier;
import com.billing.service.SupplierService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/supplier")
public class SupplierRestController {
    private final SupplierService supplierService;

    public SupplierRestController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("like")
    public List<Supplier> getSupplierByNameLike(@RequestParam String name) {
        return supplierService.findSupplierByNameLike(name);
    }

    @PostMapping
    public Supplier saveSupplier(@RequestBody Supplier supplier) {
        return supplierService.createSupplier(supplier);
    }

    @GetMapping
    public List<Supplier> get() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public Supplier getById(@PathVariable("id") Long id) {
        return supplierService.getById(id);
    }
}
