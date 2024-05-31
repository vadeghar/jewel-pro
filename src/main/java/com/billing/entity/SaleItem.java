package com.billing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "stock_id", referencedColumnName = "id")
//    private Stock stock;
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
    private Sale sale;

//    public SaleItemDTO toDto(SaleItem saleItem) {
//        SaleItemDTO saleItemDTO = new SaleItemDTO();
//        saleItemDTO.setId(saleItem.getId());
//        saleItemDTO.setName(saleItem.getName());
//        saleItemDTO.setCode(saleItem.getCode());
//        saleItemDTO.setMetalType(saleItem.getMetalType());
//        saleItemDTO.setPurity(saleItem.getPurity());
//        saleItemDTO.setWeight(saleItem.getWeight());
//        saleItemDTO.setStnWeight(saleItem.getStnWeight());
//        saleItemDTO.setNetWeight(saleItem.getNetWeight());
//        saleItemDTO.setVaWeight(saleItem.getVaWeight());
//        saleItemDTO.setMakingCharge(saleItem.getMakingCharge());
//        saleItemDTO.setStnType(saleItem.getStnType());
//        saleItemDTO.setStnCostPerCt(saleItem.getStnCostPerCt());
//        saleItemDTO.setPcs(saleItem.getPcs());
//        saleItemDTO.setHuid(saleItem.getHuid());
//        saleItemDTO.setRate(saleItem.getRate());
//        saleItemDTO.setItemTotal(saleItem.getItemTotal());
//        saleItemDTO.setStockId(saleItem.getStockId());
////        saleItemDTO.setSaleId(saleItem.getSale() != null ? saleItem.getSale().getId() : null);
//        return saleItemDTO;
//    }
}
