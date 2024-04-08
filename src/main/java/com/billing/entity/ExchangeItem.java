package com.billing.entity;

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
//    @ToString.Exclude
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "sale_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false, referencedColumnName = "id")
    @JsonIgnore
    private Sale sale;
}
