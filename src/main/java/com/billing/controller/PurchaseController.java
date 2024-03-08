package com.billing.controller;

import com.billing.constant.Metal;
import com.billing.dto.ErrorResponse;
import com.billing.entity.Estimation;
import com.billing.entity.Purchase;
import com.billing.service.MetalRateService;
import com.billing.service.PurchaseService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final MetalRateService metalRateService;
    public PurchaseController(PurchaseService purchaseService, MetalRateService metalRateService) {
        this.purchaseService = purchaseService;
        this.metalRateService = metalRateService;
    }

    @GetMapping
    public String get(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("purchase") Purchase purchase,
                      BindingResult result,
                      Model model) {
        if (httpHeaders.containsKey("X-Application-Name")) {
            System.out.println("Found X-Application-Name in header");
        }
        List<Purchase> purchaseList = purchaseService.getAll();
        BigDecimal goldRate = metalRateService.getRate(Metal.GOLD);
        BigDecimal silverRate = metalRateService.getRate(Metal.SILVER);
        model.addAttribute("currentGoldRate", goldRate.toPlainString());
        model.addAttribute("currentSilverRate", silverRate.toPlainString());
        model.addAttribute("purchaseList", purchaseList);
        model.addAttribute("purchase", new Purchase());
        return "purchase";
    }

    @PostMapping("/save")
    public String registration(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("purchase") Purchase purchase,
                               BindingResult result,
                               Model model) {
        if (httpHeaders.containsKey("X-Application-Name")) {
            System.out.println("Found X-Application-Name in header");
        }
        ErrorResponse errorResponse = purchaseService.validatePurchase(purchase);
        BigDecimal goldRate = metalRateService.getRate(Metal.GOLD);
        BigDecimal silverRate = metalRateService.getRate(Metal.SILVER);
        model.addAttribute("currentGoldRate", goldRate.toPlainString());
        model.addAttribute("currentSilverRate", silverRate.toPlainString());
        model.addAttribute("purchaseList", purchaseService.getAll());
        if(errorResponse.hasErrors()) {
            model.addAttribute("purchase", purchase);
            model.addAttribute("errorResponse", errorResponse);
            return "item";
        }
        model.addAttribute("purchase", purchaseService.save(purchase));
        return "redirect:/purchase?success";
    }
}
