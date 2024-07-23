package com.billing.controller;

import com.billing.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
    private final SupplierService supplierService;
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public String get(@RequestParam(value = "id", required = false) Long id, Model model) {
        return "views/supplier/supplier";
    }

    @GetMapping("supplier-list")
    public String showSupplierList(Model model) {
        return "views/supplier/supplier-list";
    }

    @GetMapping("supplier-sale")
    public String supplierPurchase(Model model) {
        return "views/supplier/supplier-sale";
    }

    @GetMapping("/{id}")
    public String getById(Model model) {
        return "views/supplier/supplier";
    }
    
    
    
    
//
//    @PostMapping("/save")
//    public String registration(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("supplier") Supplier supplier,
//                               BindingResult result,
//                               Model model) {
//        if (httpHeaders.containsKey("X-Application-Name")) {
//            System.out.println("Found X-Application-Name in header");
//        }
//        ErrorResponse errorResponse = supplierService.validateSupplier(supplier);
//        model.addAttribute("supplierList", supplierService.getAll());
//        if(errorResponse.hasErrors()) {
//            model.addAttribute("supplier", supplier);
//            model.addAttribute("errorResponse", errorResponse);
//            return "supplier";
//        }
//        model.addAttribute("supplier", supplierService.save(supplier));
//        return "redirect:/supplier?success";
//    }
}
