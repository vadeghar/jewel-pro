package com.billing.controller.rest;

import com.billing.model.ChartData;
import com.billing.service.PurchaseService;
import com.billing.service.SaleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/graph")
public class GraphRestController {

    private final PurchaseService purchaseService;
    private final SaleService saleService;

    public GraphRestController(PurchaseService purchaseService, SaleService saleService) {
        this.purchaseService = purchaseService;
        this.saleService = saleService;
    }

    @GetMapping("purchase/yearly")
    public List<ChartData> yearlyData(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return purchaseService.getYearlyData(startDate, endDate);
    }

    @GetMapping("purchase/monthly")
    public List<ChartData> monthlyData(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return purchaseService.getMonthlyData(startDate, endDate);
    }

    @GetMapping("purchase/weekly")
    public List<ChartData> weeklyData(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return purchaseService.getWeeklyData(startDate, endDate);
    }

    @GetMapping("purchase/current-month-total")
    public BigDecimal currentMonthPurchaseTotal() {
        return purchaseService.getCurrentMonthPurchaseTotal();
    }

    @GetMapping("sale/yearly")
    public List<ChartData> yearlySaleData(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return saleService.getYearlyData(startDate, endDate);
    }

    @GetMapping("sale/monthly")
    public List<ChartData> monthlySaleData(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return saleService.getMonthlyData(startDate, endDate);
    }

    @GetMapping("sale/weekly")
    public List<ChartData> weeklySaleData(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return saleService.getWeeklyData(startDate, endDate);
    }

    @GetMapping("sale/current-month-total")
    public BigDecimal currentMonthSaleTotal() {
        return saleService.getCurrentMonthSaleTotal();
    }

    @GetMapping("sale/top-records")
    public List<ChartData> topCustomers() {
        return saleService.getTopCustomers();
    }

    @GetMapping("purchase/top-records")
    public List<ChartData> topSuppliers() {
        return purchaseService.getTopSuppliers();
    }
}
