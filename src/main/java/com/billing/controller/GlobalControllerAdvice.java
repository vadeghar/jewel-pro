package com.billing.controller;

import com.billing.constant.Metal;
import com.billing.service.MetalRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.math.BigDecimal;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final MetalRateService metalRateService;

    @Autowired
    public GlobalControllerAdvice( MetalRateService metalRateService) {
        this.metalRateService = metalRateService;
    }

    @ModelAttribute
    public void globalAttributes(Model model) {
        BigDecimal goldRate = metalRateService.getRate(Metal.GOLD);
        BigDecimal silverRate = metalRateService.getRate(Metal.SILVER);
        model.addAttribute("currentGoldRate", goldRate != null ? goldRate.toPlainString() : "0.00");
        model.addAttribute("currentSilverRate", silverRate!= null ? silverRate.toPlainString() : "0.00");
    }
}

