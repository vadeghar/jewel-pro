package com.billing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    private String purchaseType = "PURCHASE"; // Purchase / Purchase Return
    private String supplierName;
    private String purchaseBillNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    private LocalDateTime lastUpdatedTs = LocalDateTime.now();
    private String metalType;
    private BigDecimal grossWeight;
    private BigDecimal netWeight;
    private String purity;
    private String salePurity;
    private Integer pcs;
    private BigDecimal stnWeight;
    private BigDecimal effectiveWeight;

    private String paymentMode;
    private BigDecimal mcAmount;
    private BigDecimal rate;
    private String isGstPurchase;
    private BigDecimal cGstAmount;
    private BigDecimal sGstAmount;
    private BigDecimal totalPurchaseAmount;

    private String description;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> purchaseItems;





}
