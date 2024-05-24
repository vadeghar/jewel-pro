package com.billing.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EstimationItemDTO {

    private Long id;
    private Long stockId;
    private String name;
    private String code;
    private String metalType;
    private String purity;
    private BigDecimal weight;
    private BigDecimal stnWeight;
    private BigDecimal netWeight;
    private BigDecimal vaWeight;
    private BigDecimal makingCharge;
    private String stnType;
    private BigDecimal stnCostPerCt;
    private int pcs;
    private String huid;
    private BigDecimal rate;
    private BigDecimal itemTotal;
}
