package com.billing.dto;

import com.billing.entity.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemDTO {
    private Long id;
    private BigDecimal purchaseMC;
    private BigDecimal actualPurity;
    private BigDecimal purchasePurity;
    private BigDecimal purchaseRate;
    private BigDecimal purchaseStnCostPerCt;
    private BigDecimal cGstAmount = BigDecimal.ZERO;
    private BigDecimal sGstAmount = BigDecimal.ZERO;
    private BigDecimal itemAmount;
    private Integer pcs;
    private ItemType itemType;
    private String createdBy;

    // Stock attributes
    private String name;
    private String code;
    private BigDecimal weight;
    private BigDecimal stnWeight;
    private String stnType;
    private BigDecimal saleStnCostPerCt;
    private BigDecimal vaWeight;
    private BigDecimal itemTotalWeight;
    private BigDecimal saleMC;
    private String huid;
    private String purity;
    private boolean active;
    private Long itemTypeId;
}
