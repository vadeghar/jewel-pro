package com.billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sale")
public class SaleController {

    @GetMapping
    public String get(Model model, @RequestParam(required = false) Long id) {
        model.addAttribute("saleId", id);
        return "views/sale/sale";
    }

    @GetMapping("list")
    public String saleList() {
        return "views/sale/sale-list";
    }

    @GetMapping("payment-modal")
    public String paymentModal() {
        return "views/payment/list-payment-modal";
    }
    @GetMapping("/add-payment")
    public String payment(Model model) {
        return "views/payment/add-payment-modal";
    }
    @GetMapping("/view")
    public String view(@RequestParam Long id, Model model) {
        model.addAttribute("saleId", id);
        return "views/sale/sale-view";
    }

//    @GetMapping("/create")
//    public String showSaleForm(Model model) {
//        Sale sale = new Sale();
//        model.addAttribute("sale", sale);
//        return "sale/form"; // Assuming you have a Thymeleaf template named "form.html" in "sale" folder
//    }
//
//    @PostMapping("/save")
//    public String saveSale(@ModelAttribute("sale") SaleDTO sale, Model model) {
//        System.out.println("SaleDTO: "+sale);
////        SaleDTO saleDTO = saleService.saveSale3(sale);
//        model.addAttribute("sale", new SaleDTO());
//        return "sale";
//    }

    // Other CRUD operations for Sale entity
}

