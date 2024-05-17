package com.billing.dto;

import com.billing.entity.ItemMaster;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EstRequest {
//    private String itemDesc;
//    private String tagNo;
//    private String itemCode;
//    private BigDecimal stoneWeight;
//    private String stoneName;
//    private int stonePcs;
//    private int pcs;
//    private BigDecimal weight;
//    private String itemHuid;
//    private String itemQuality;
//    private Metal itemMetal;

    private ItemMaster itemMaster;
    private BigDecimal goldRate;
    private BigDecimal silverRate;
    private BigDecimal vaPercentage;
    private String defaultMcEnabled = "YES";
    private String isGstEstimation = "NO";
    private BigDecimal discount = BigDecimal.ZERO;
}
