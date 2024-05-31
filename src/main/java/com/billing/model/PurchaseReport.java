package com.billing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReport {
    private LocalDate purchaseDate;
    private String metalType;
    private String supplierName;
    private BigDecimal totalNetWeight;
    private BigDecimal purchaseRate;
    private String isGstPurchase;
    private BigDecimal totalGstAmount;
    private String gstNo;
    private BigDecimal totalPurchaseAmount;
    private BigDecimal totalBalAmount;



    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
}

