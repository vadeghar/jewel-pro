package com.billing.entity;

//import com.billing.dto.PurchaseItemDTO;
import com.billing.dto.PurchaseItemDTO;
import com.billing.enums.StockStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "STOCK")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(exclude = {"purchaseItem"})
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
//    @Column(precision = 20, scale = 3)
//    private BigDecimal itemTotalWeight;
    @Column(precision = 20, scale = 2)
    private BigDecimal saleMC;
    @Column(length = 20)
    private String huid;
    @Column(length = 20)
    private String purity;
    private boolean active;
    @OneToOne(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PurchaseItem purchaseItem;
    @LastModifiedDate
    private LocalDateTime updatedDate;
    @CreatedDate
    private LocalDateTime createdDate;
    @CreatedBy
    private String createdBy;
    private Integer pcs;
    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_type_id")
    private ItemType itemType;

    public Stock fromDto(PurchaseItemDTO dto) {
//        this.getStock().setId(dto.getStockId());
        this.setActive(dto.isActive());
        this.setCode(dto.getCode());
        this.setPcs(dto.getPcs());
        this.setHuid(dto.getHuid());
        this.setSaleMC(dto.getSaleMC());
        this.setName(dto.getName());
        this.setWeight(dto.getWeight());
        this.setStnWeight(dto.getStnWeight());
        this.setVaWeight(dto.getVaWeight());
        this.setStnCostPerCt(dto.getSaleStnCostPerCt());
        this.setStnType(dto.getStnType());
        this.setPurity(dto.getPurity());
        this.setStockStatus(dto.getStockStatus());
        return this;
    }
}
