package com.billing.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name="purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String purchaseType; // Purchase / Purchase Return
    private String supplierName;
    private String purchaseBillNo;
    private LocalDateTime purchaseDateTs;
    private String metalType;
    private BigDecimal grossWeight;
    private BigDecimal netWeight;
    private Integer qty;
    private BigDecimal stnWeight;

    private String paymentMode;
    private BigDecimal mcAmount;
    private BigDecimal rate;
    private String isGstPurchase;
    private BigDecimal cGstAmount;
    private BigDecimal sGstAmount;
    private BigDecimal totalPurchaseAmount;

    private String description;







}
