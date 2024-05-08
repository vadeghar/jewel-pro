package com.billing.controller.rest;

import com.billing.dto.StockDTO;
import com.billing.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
public class StockRestController {

    private final StockService stockService;

    public StockRestController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<StockDTO> getStocksByCode(@RequestParam String code) {
        return stockService.getStockByCode(code);
    }
}
