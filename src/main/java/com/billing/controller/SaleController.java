package com.billing.controller;

import com.billing.entity.Purchase;
import com.billing.entity.Sale;
import com.billing.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/sale")
public class SaleController {
    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public String get(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("sale") Sale sale,
                      BindingResult result,
                      Model model) {
        if (httpHeaders.containsKey("X-Application-Name")) {
            System.out.println("Found X-Application-Name in header");
        }
        Sale sale1 = new Sale();
        sale.setIsGstSale("NO");
        model.addAttribute("sale", sale1);
        return "sale";
    }

    @GetMapping("/list")
    public String getAllSales(Model model) {
        List<Sale> sales = saleService.getAllSales();
        model.addAttribute("sales", sales);
        return "sale/list"; // Assuming you have a Thymeleaf template named "list.html" in "sale" folder
    }

    @GetMapping("/create")
    public String showSaleForm(Model model) {
        Sale sale = new Sale();
        model.addAttribute("sale", sale);
        return "sale/form"; // Assuming you have a Thymeleaf template named "form.html" in "sale" folder
    }

    @PostMapping("/save")
    public String saveSale(@ModelAttribute("sale") Sale sale) {
        saleService.saveSale(sale);
        return "redirect:/sales/list";
    }

    // Other CRUD operations for Sale entity
}

