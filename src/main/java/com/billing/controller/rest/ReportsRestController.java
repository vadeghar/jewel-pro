package com.billing.controller.rest;

import com.billing.model.ReportFilters;
import com.billing.model.WeeklyReport;
import com.billing.service.PurchaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/reports")
public class ReportsRestController {

    private final PurchaseService purchaseService;

    public ReportsRestController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/purchase")
    public List<WeeklyReport> getPurchaseReport(@RequestBody ReportFilters filters) {
        return purchaseService.generateReport(filters);
    }

}
