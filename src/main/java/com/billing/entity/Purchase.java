package com.billing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@Table(name="PURCHASE")
@EntityListeners(AuditingEntityListener.class)
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String purchaseType = "PURCHASE"; // Purchase / Purchase Return
    @Column(length = 30)
    private String purchaseBillNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    @Column(length = 30)
    private String metalType;
    @Column(precision = 20, scale = 3)
    private BigDecimal totalGrossWeight;
    @Column(precision = 20, scale = 2)
    private String actualPurity;
    @Column(precision = 20, scale = 2)
    private String purchasePurity;
    @Column(precision = 20, scale = 3)
    private BigDecimal totalNetWeight;

    private Integer totalPcs;
    @Column(precision = 20, scale = 3)
    private BigDecimal totalStnWeight;

    @Column(length = 10)
    private String paymentMode;
    @Column(precision = 20, scale = 2)
    private BigDecimal totalMcAmount;
    @Column(precision = 20, scale = 2)
    private BigDecimal purchaseRate;
    @Column(length = 10)
    private String isGstPurchase;
    @Column(length = 20)
    private String gstNo;
    @Column(length = 20, scale = 2)
    private BigDecimal purchaseAmount;
    @Column(precision = 20, scale = 2)
    private BigDecimal totalCgstAmount = BigDecimal.ZERO;
    @Column(precision = 20, scale = 2)
    private BigDecimal totalSGstAmount = BigDecimal.ZERO;
    @Column(precision = 20, scale = 2)
    private BigDecimal totalPurchaseAmount;
    @Column(length = 20, scale = 2)
    private BigDecimal paidAmount;
    @Column(length = 20, scale = 2)
    private BigDecimal balAmount;
    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<PurchaseItem> purchaseItems;

    @LastModifiedDate
    private LocalDateTime updatedDate;
    @CreatedDate
    private LocalDateTime createdDate;
    @CreatedBy
    private String createdBy;


}
