package com.billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("payment")
public class PaymentController {

    @GetMapping("sales")
    public String sales(Model model) {
        model.addAttribute("source", "Sales");
        return "views/payment/payment-list";
    }

    @GetMapping("purchases")
    public String purchases(Model model) {
        model.addAttribute("source", "Purchases");
        return "views/payment/payment-list";
    }
}
