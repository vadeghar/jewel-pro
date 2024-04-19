package com.billing.dto;

import com.billing.entity.ItemType;
import com.billing.entity.PurchaseItem;
import com.billing.entity.Stock;
import com.billing.enums.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemDTO {
    private Long id;
    private BigDecimal purchaseMC;
    private BigDecimal actualPurity;
    private BigDecimal purchasePurity;
    private BigDecimal purchaseRate;
    private BigDecimal purchaseStnCostPerCt;
    private BigDecimal cGstAmount = BigDecimal.ZERO;
    private BigDecimal sGstAmount = BigDecimal.ZERO;
    private BigDecimal itemAmount;
    private Integer pcs;
    private ItemType itemType;
    private String createdBy;

    // Stock attributes
    private String name;
    private String code;
    private BigDecimal weight;
    private BigDecimal stnWeight;
    private String stnType;
    private BigDecimal saleStnCostPerCt;
    private BigDecimal vaWeight;
    private BigDecimal itemTotalWeight;
    private BigDecimal saleMC;
    private String huid;
    private String purity;
    private boolean active;
    private Long itemTypeId;
    private Long stockId;
    private StockStatus stockStatus;


    public void toDto(PurchaseItem purchaseItem) {
        this.id = purchaseItem.getId();
//        this.purchaseMC = purchaseItem.;
//        this.actualPurity = ;
//        this.purchasePurity = ;
//        this.purchaseRate = ;
//        this.purchaseStnCostPerCt = ;
//        this.cGstAmount = BigDecimal.ZERO = ;
//        this.sGstAmount = BigDecimal.ZERO = ;
//        this.itemAmount = ;
        this.createdBy = purchaseItem.getCreatedBy();

        // Stock attributes
        this.name = purchaseItem.getStock().getName();
        this.code = purchaseItem.getStock().getCode();
        this.weight = purchaseItem.getStock().getWeight();
        this.stnWeight = purchaseItem.getStock().getStnWeight();
        this.stnType = purchaseItem.getStock().getStnType();
        this.saleStnCostPerCt = purchaseItem.getStock().getStnCostPerCt();
        this.vaWeight = purchaseItem.getStock().getVaWeight();
//        this.itemTotalWeight = purchaseItem.getStock().get;
        this.saleMC = purchaseItem.getStock().getSaleMC();
        this.huid = purchaseItem.getStock().getHuid();
        this.purity = purchaseItem.getStock().getPurity();
        this.active = purchaseItem.getStock().isActive();
        this.itemTypeId = purchaseItem.getStock().getItemType() != null ? purchaseItem.getStock().getItemType().getId() : null;
        this.stockId = purchaseItem.getStock().getId();
        this.stockStatus = purchaseItem.getStock().getStockStatus();
        this.pcs = purchaseItem.getStock().getPcs();
    }

    public PurchaseItem toEntity() {
        return PurchaseItem.builder()
                .id(this.id)
//                .itemAmount(this.itemAmount)
//                .actualPurity(this.actualPurity)
//                .itemType(this.itemType)
//                .purchasePurity(this.purchasePurity)
//                .purchaseRate(this.purchaseRate)
//                .cGstAmount(this.cGstAmount)
//                .sGstAmount(this.sGstAmount)
//                .purchaseMC(this.purchaseMC)
//                .purchaseStnCostPerCt(this.purchaseStnCostPerCt)
//                .stock(Stock.builder()
//                        .id(this.stockId)
//                        .active(this.active)
//                        .code(this.code)
//                        .pcs(this.pcs)
//                        .huid(this.huid)
//                        .saleMC(this.saleMC)
//                        .name(this.name)
//                        .weight(this.weight)
//                        .stnWeight(this.stnWeight)
//                        .vaWeight(this.vaWeight)
//                        .stnCostPerCt(this.saleStnCostPerCt)
//                        .stnType(this.stnType)
//                        .purity(this.purity)
//                        .stockStatus(StockStatus.IN_STOCK)
////                        .itemTotalWeight(this.itemTotalWeight)
//                        .build())
                .build();
    }
}
