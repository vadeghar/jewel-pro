package com.billing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name="sale_item")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_item_id", referencedColumnName = "id")
    private PurchaseItem purchaseItem;
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
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Sale sale;
}
