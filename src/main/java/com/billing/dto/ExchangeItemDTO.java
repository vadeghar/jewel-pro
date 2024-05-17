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

//    private Long id;
//    private BigDecimal weight;
//    private BigDecimal meltPercentage;
//    private BigDecimal wastageInGms;
//    private BigDecimal netWeight;
//    private String itemDesc;
//    private String source; // PURCHASE/ SALE
//    private Long sourceId;
//    private BigDecimal rate;
//    private BigDecimal exchangeValue;
//    private Long saleId;

//    public ExchangeItem mapDtoToEntity(ExchangeItemDTO exchangeItemDTO, ExchangeItem exchangeItem) {
//        exchangeItem.setId(exchangeItemDTO.getId());
//        exchangeItem.setWeight(exchangeItemDTO.getWeight());
//        exchangeItem.setMeltPercentage(exchangeItemDTO.getMeltPercentage());
//        exchangeItem.setWastageInGms(exchangeItemDTO.getWastageInGms());
//        exchangeItem.setNetWeight(exchangeItemDTO.getNetWeight());
//        exchangeItem.setItemDesc(exchangeItemDTO.getItemDesc());
//        exchangeItem.setSource(exchangeItemDTO.getSource());
//        exchangeItem.setSourceId(exchangeItemDTO.getSourceId());
//        exchangeItem.setRate(exchangeItemDTO.getRate());
//        exchangeItem.setExchangeValue(exchangeItemDTO.getExchangeValue());
//
//        return exchangeItem;
//    }
}
