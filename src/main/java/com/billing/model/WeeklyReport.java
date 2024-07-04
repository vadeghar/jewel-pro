package com.billing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyReport {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalNetWeight;
    private BigDecimal totalGst;
    private BigDecimal totalPurchaseAmount;
    private BigDecimal totalPaidAmount;
    private BigDecimal totalBalAmount;
}
