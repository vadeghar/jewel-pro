package com.billing.dto;

import com.billing.constant.Metal;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EstRequest {
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
