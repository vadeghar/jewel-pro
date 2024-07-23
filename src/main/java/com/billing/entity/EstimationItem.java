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
@Table(name="estimation_item")
public class EstimationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false, referencedColumnName = "id")
    @JsonIgnore
    private Estimation estimation;
}
