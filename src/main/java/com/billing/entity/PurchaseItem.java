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
@Table(name = "purchase_item")
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private BigDecimal weight;
    private BigDecimal vaWeight;
    private BigDecimal makingCharge;
    private BigDecimal stnWeight;
    private String stnType;
    private BigDecimal stnCostPerCt;
    private int pcs;
    private String huid;
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
}

