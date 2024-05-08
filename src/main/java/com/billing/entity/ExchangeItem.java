package com.billing.entity;

import com.billing.dto.ExchangeItemDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name="exchange_item")
public class ExchangeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal weight;
    private BigDecimal meltPercentage;
    private BigDecimal wastageInGms;
    private BigDecimal netWeight;
    private String itemDesc;
    private String source; // PURCHASE/ SALE
    private Long sourceId;
    private BigDecimal rate;
    private BigDecimal exchangeValue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false, referencedColumnName = "id")
    @JsonIgnore
    private Sale sale;
//
//    public ExchangeItemDTO toDto() {
//        ExchangeItemDTO exchangeItemDTO = new ExchangeItemDTO();
//        exchangeItemDTO.setId(this.id);
//        exchangeItemDTO.setWeight(this.weight);
//        exchangeItemDTO.setMeltPercentage(this.meltPercentage);
//        exchangeItemDTO.setWastageInGms(this.wastageInGms);
//        exchangeItemDTO.setNetWeight(this.netWeight);
//        exchangeItemDTO.setItemDesc(this.itemDesc);
//        exchangeItemDTO.setSource(this.source);
//        exchangeItemDTO.setSourceId(this.sourceId);
//        exchangeItemDTO.setRate(this.rate);
//        exchangeItemDTO.setExchangeValue(this.exchangeValue);
//        exchangeItemDTO.setSaleId(this.sale != null ? this.sale.getId() : null);
//        return exchangeItemDTO;
//    }
}
