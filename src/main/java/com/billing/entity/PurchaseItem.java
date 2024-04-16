package com.billing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @Column(precision = 20, scale = 2)
    private BigDecimal purchaseMC;
    @Column(length = 10)
    private BigDecimal actualPurity;
    @Column(length = 10)
    private BigDecimal purchasePurity;
    @Column(precision = 20, scale = 2)
    private BigDecimal purchaseRate;
    @Column(precision = 20, scale = 2)
    private BigDecimal purchaseStnCostPerCt = BigDecimal.ZERO;
    private BigDecimal cGstAmount = BigDecimal.ZERO;
    @Column(precision = 20, scale = 2)
    private BigDecimal sGstAmount = BigDecimal.ZERO;
    @Column(precision = 20, scale = 2)
    private BigDecimal itemAmount;
    private Integer pcs;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_type_id")
    private ItemType itemType;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @LastModifiedDate
    private LocalDateTime updatedDate;
    @CreatedDate
    private LocalDateTime createdDate;
    private String createdBy;
}

