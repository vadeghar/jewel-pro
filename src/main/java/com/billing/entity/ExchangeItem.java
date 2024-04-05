package com.billing.entity;

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
    private String desc;
    private String source; // PURCHASE/ SALE
    private Long sourceId;
    private BigDecimal rate;
    private BigDecimal exchangeValue;
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sale_id")
    private Sale sale;
}
