package com.billing.dto;

import lombok.*;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ExchangeItemDTO {

    private Long id;
    private BigDecimal weight;
    private BigDecimal meltPercentage;
    private BigDecimal wastageInGms;
    private BigDecimal netWeight;
    private String itemDesc;
    private String source;
    private Long sourceId;
    private BigDecimal rate;
    private BigDecimal exchangeValue;

}
