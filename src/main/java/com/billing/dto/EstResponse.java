package com.billing.dto;

import com.billing.constant.Metal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstResponse {
    private Long id;
    private LocalDateTime dateTime;
    private String estimationNo;
    private BigDecimal netWeight;
    private BigDecimal netWeightPrice;
    private BigDecimal stoneWtInCts;
    private BigDecimal stonePricePerCt;
    private BigDecimal stonePrice;
    private BigDecimal totalPriceInclMc;
    private BigDecimal totalPriceInclVa;
    private BigDecimal vaPrice;
    private Double cGstPercentage = 1.5;
    private Double sGstPercentage = 1.5;
    private BigDecimal totalPriceInclGst;
    private BigDecimal cGstPrice;
    private BigDecimal sGstPrice;
    private BigDecimal grandTotalAfterDiscount;
    private BigDecimal totalPrice;
    private BigDecimal mc;

    //Request attributes
    private String itemDesc;
    private String tagNo;
    private String itemCode;
    private String itemHuid;
    private String itemQuality;
    private Metal itemMetal;
    private BigDecimal goldRate;
    private BigDecimal silverRate;
    private int pcs;
    private BigDecimal weight;
    private BigDecimal vaPercentage;
    private BigDecimal stoneWeight;
    private String stoneName;
    private int stonePcs;
    private String defaultMcEnabled = "YES";
    private String isGstEstimation = "NO";
    private BigDecimal discount = BigDecimal.ZERO;
}
