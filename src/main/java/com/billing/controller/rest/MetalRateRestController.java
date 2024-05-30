package com.billing.controller.rest;

import com.billing.constant.Metal;
import com.billing.dto.ErrorResponse;
import com.billing.entity.MetalRate;
import com.billing.model.BoardRate;
import com.billing.service.MetalRateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/rate")
public class MetalRateRestController {
    private final MetalRateService metalRateService;
    public MetalRateRestController(MetalRateService metalRateService) {
        this.metalRateService = metalRateService;
    }

    @GetMapping("current-rate")
    public ResponseEntity<BoardRate> currentRate() {
        BigDecimal goldRate = metalRateService.getRate(Metal.GOLD);
        BigDecimal silverRate = metalRateService.getRate(Metal.SILVER);
        return ResponseEntity.ok(BoardRate.builder().goldRate(goldRate).silverRate(silverRate).build());
    }

    @PostMapping("save")
    public ResponseEntity<BoardRate> saveRate(@RequestBody MetalRate metalRate) {
        MetalRate newMetalRate = metalRateService.save(metalRate);
        BigDecimal goldRate = metalRateService.getRate(Metal.GOLD);
        BigDecimal silverRate = metalRateService.getRate(Metal.SILVER);
        return ResponseEntity.ok(BoardRate.builder().goldRate(goldRate).silverRate(silverRate).build());
    }
//
//    @PostMapping("/save")
//    public String registration(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("metalRate") MetalRate metalRate,
//                               BindingResult result,
//                               Model model) {
//        if (httpHeaders.containsKey("X-Application-Name")) {
//            System.out.println("Found X-Application-Name in header");
//        }
//        ErrorResponse errorResponse = metalRateService.validateMetalRate(metalRate);
//        if(errorResponse.hasErrors()) {
//            model.addAttribute("metalRate", metalRate);
//            model.addAttribute("errorResponse", errorResponse);
//            return "metal-rate";
//        }
//        model.addAttribute("itemMaster", metalRateService.save(metalRate));
//        return "redirect:/metal-rate?success";
//    }
}
