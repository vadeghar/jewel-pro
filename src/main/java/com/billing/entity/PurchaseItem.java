package com.billing.entity;

import com.billing.dto.PurchaseItemDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "purchase_item")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(exclude = {"stock"})
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(precision = 20, scale = 2)
//    private BigDecimal purchaseMC;
//    @Column(length = 10)
//    private BigDecimal actualPurity;
//    @Column(length = 10)
//    private BigDecimal purchasePurity;
//    @Column(precision = 20, scale = 2)
//    private BigDecimal purchaseRate;
//    @Column(precision = 20, scale = 2)
//    private BigDecimal purchaseStnCostPerCt = BigDecimal.ZERO;
//    private BigDecimal cGstAmount = BigDecimal.ZERO;
//    @Column(precision = 20, scale = 2)
//    private BigDecimal sGstAmount = BigDecimal.ZERO;
//    @Column(precision = 20, scale = 2)
//    private BigDecimal itemAmount;
//
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_type_id")
//    private ItemType itemType;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Stock stock;

    @LastModifiedDate
    private LocalDateTime updatedDate;
    @CreatedDate
    private LocalDateTime createdDate;
    @CreatedBy
    private String createdBy;

//    public PurchaseItemDTO toDto() {
//        return PurchaseItemDTO.builder()
//                .id(this.id)
////                .itemAmount(this.itemAmount)
////                .actualPurity(this.actualPurity)
////                .itemType(this.itemType)
////                .purchasePurity(this.purchasePurity)
////                .purchaseRate(this.purchaseRate)
////                .cGstAmount(this.cGstAmount)
////                .sGstAmount(this.sGstAmount)
////                .purchaseMC(this.purchaseMC)
////                .purchaseStnCostPerCt(this.purchaseStnCostPerCt)
//                .stockId(this.stock.getId())
//                .active(this.stock.isActive())
//                .code(this.stock.getCode())
//                .pcs(this.stock.getPcs())
//                .huid(this.stock.getHuid())
//                .saleMC(this.stock.getSaleMC())
//                .name(this.stock.getName())
//                .weight(this.stock.getWeight())
//                .stnWeight(this.stock.getStnWeight())
//                .vaWeight(this.stock.getVaWeight())
//                .saleStnCostPerCt(this.stock.getStnCostPerCt())
//                .stnType(this.stock.getStnType())
//                .purity(this.stock.getPurity())
//                .stockStatus(this.getStock().getStockStatus())
//                .build();
//    }
//
    public void fromDto(PurchaseItemDTO dto) {
//        this.setId(dto.getId());

        this.getStock().setId(dto.getStockId());
        this.getStock().setActive(dto.isActive());
        this.getStock().setCode(dto.getCode());
        this.getStock().setPcs(dto.getPcs());
        this.getStock().setHuid(dto.getHuid());
        this.getStock().setSaleMC(dto.getSaleMC());
        this.getStock().setName(dto.getName());
        this.getStock().setWeight(dto.getWeight());
        this.getStock().setStnWeight(dto.getStnWeight());
        this.getStock().setVaWeight(dto.getVaWeight());
        this.getStock().setStnCostPerCt(dto.getSaleStnCostPerCt());
        this.getStock().setStnType(dto.getStnType());
        this.getStock().setPurity(dto.getPurity());
        this.getStock().setStockStatus(dto.getStockStatus());
    }
}

