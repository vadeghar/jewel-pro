package com.billing.entity;

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
@Table(name = "STOCK")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 30)
    private String code;
    @Column(precision = 20, scale = 3)
    private BigDecimal weight;
    @Column(precision = 20, scale = 3)
    private BigDecimal stnWeight;
    @Column(length = 20)
    private String stnType;
    @Column(precision = 20, scale = 3)
    private BigDecimal stnCostPerCt;
    @Column(precision = 20, scale = 3)
    private BigDecimal vaWeight;
    @Column(precision = 20, scale = 3)
    private BigDecimal itemTotalWeight;
    @Column(precision = 20, scale = 2)
    private BigDecimal saleMC;
    @Column(length = 20)
    private String huid;
    @Column(length = 20)
    private String purity;
    private boolean active;
    @OneToOne(mappedBy = "stock", cascade = CascadeType.ALL)
    private PurchaseItem purchaseItem;
    @LastModifiedDate
    private LocalDateTime updatedDate;
    @CreatedDate
    private LocalDateTime createdDate;
    private String createdBy;
    private Integer pcs;
}
