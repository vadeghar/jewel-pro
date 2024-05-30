package com.billing.dto;

import com.billing.entity.ItemType;
import com.billing.enums.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDTO {
    private Long id;
    private String name;
    private String code;
    private BigDecimal weight;
    private BigDecimal stnWeight;
    private String stnType;
    private BigDecimal stnCostPerCt;
    private BigDecimal vaWeight;
    private BigDecimal saleMC;
    private String huid;
    private String purity;
    private boolean active;
    private Integer pcs;
    private StockStatus stockStatus;
    private ItemType itemType;

}
