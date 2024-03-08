package com.billing.controller;

import com.billing.dto.ErrorResponse;
import com.billing.entity.Supplier;
import com.billing.service.SupplierService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
    private final SupplierService supplierService;
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public String get(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("supplier") Supplier supplier,
                      BindingResult result,
                      Model model) {
        if (httpHeaders.containsKey("X-Application-Name")) {
            System.out.println("Found X-Application-Name in header");
        }
        List<Supplier> supplierList = supplierService.getAll();
        //model.addAttribute("errorResponse", new ErrorResponse(new ArrayList<>(), LocalDateTime.now()));
        model.addAttribute("supplierList", supplierList);
        model.addAttribute("supplier", new Supplier());
        return "supplier";
    }

    @PostMapping("/save")
    public String registration(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("supplier") Supplier supplier,
                               BindingResult result,
                               Model model) {
        if (httpHeaders.containsKey("X-Application-Name")) {
            System.out.println("Found X-Application-Name in header");
        }
        ErrorResponse errorResponse = supplierService.validateSupplier(supplier);
        model.addAttribute("supplierList", supplierService.getAll());
        if(errorResponse.hasErrors()) {
            model.addAttribute("supplier", supplier);
            model.addAttribute("errorResponse", errorResponse);
            return "supplier";
        }
        model.addAttribute("supplier", supplierService.save(supplier));
        return "redirect:/supplier?success";
    }
}
