package com.billing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name="estimation_exchange_item")
public class EstimationExchangeItem {
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
    @JoinColumn(name = "estimation_id", nullable = false, referencedColumnName = "id")
    @JsonIgnore
    private Estimation estimation;
}
