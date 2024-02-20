package com.billing.controller;

import com.billing.constant.Metal;
import com.billing.dto.ErrorResponse;
import com.billing.entity.Estimation;
import com.billing.service.EstimationService;
import com.billing.service.MetalRateService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@Controller
@RequestMapping("/estimation")
public class EstimationController {

    private final EstimationService estimationService;
    private final MetalRateService metalRateService;

    public EstimationController(EstimationService estimationService, MetalRateService metalRateService) {
        this.estimationService = estimationService;
        this.metalRateService = metalRateService;
    }

    @GetMapping
    public String get(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("estimation") Estimation estimation1,
                      BindingResult result,
                      Model model) {
//        if (httpHeaders.containsKey("X-Application-Name")) {
//            System.out.println("Found X-Application-Name in header");
//        }
        BigDecimal goldRate = metalRateService.getRate(Metal.GOLD);
        BigDecimal silverRate = metalRateService.getRate(Metal.SILVER);
        Estimation estimation = new Estimation();
//        estimation.setGoldRate(goldRate);
//        estimation.setSilverRate(silverRate);
        model.addAttribute("currentGoldRate", goldRate.toPlainString());
        model.addAttribute("currentSilverRate", silverRate.toPlainString());
        model.addAttribute("estimation", estimation);
        return "estimation";
    }


    @PostMapping("/estimate")
    public String registration(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("estimation") Estimation estimation,
                               BindingResult result,
                               Model model) {
        BigDecimal goldRate = metalRateService.getRate(Metal.GOLD);
        BigDecimal silverRate = metalRateService.getRate(Metal.SILVER);
        if (httpHeaders.containsKey("X-Application-Name")) {
            System.out.println("Found X-Application-Name in header");
        }
        if(Metal.GOLD == estimation.getItemMetal()) {
            estimation.setRate(goldRate);
        }
        if(Metal.SILVER == estimation.getItemMetal()) {
            estimation.setRate(silverRate);
        }
        ErrorResponse errorResponse = estimationService.validateEstimation(estimation);

//        estimation.setGoldRate(goldRate);
//        estimation.setSilverRate(silverRate);
        model.addAttribute("currentGoldRate", goldRate.toPlainString());
        model.addAttribute("currentSilverRate", silverRate.toPlainString());
        if(errorResponse.hasErrors()) {
            model.addAttribute("estimation", estimation);
            model.addAttribute("errorResponse", errorResponse);
            return "estimation";
        }

        estimation.calculate();
        System.out.println("Calculated estimation: "+estimation);
        //estimationService.save(estimation);
        model.addAttribute("estimation", estimation);
        return "estimation";
    }

}
