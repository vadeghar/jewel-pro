package com.billing.controller;

import com.billing.dto.ErrorResponse;
import com.billing.entity.MetalRate;
import com.billing.service.MetalRateService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/metal-rate")
public class MetalRateController {
    private final MetalRateService metalRateService;
    public MetalRateController(MetalRateService metalRateService) {
        this.metalRateService = metalRateService;
    }

    @GetMapping
    public String getItemRate(Model model) {
        model.addAttribute("metalRate", new MetalRate());
        model.addAttribute("metalRateList", metalRateService.getAll());
        return "metal-rate";
    }

    @PostMapping("/save")
    public String registration(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("metalRate") MetalRate metalRate,
                               BindingResult result,
                               Model model) {
        if (httpHeaders.containsKey("X-Application-Name")) {
            System.out.println("Found X-Application-Name in header");
        }
        ErrorResponse errorResponse = metalRateService.validateMetalRate(metalRate);
        if(errorResponse.hasErrors()) {
            model.addAttribute("metalRate", metalRate);
            model.addAttribute("errorResponse", errorResponse);
            return "metal-rate";
        }
        model.addAttribute("itemMaster", metalRateService.save(metalRate));
        return "redirect:/metal-rate?success";
    }
}
