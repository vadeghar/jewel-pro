package com.billing.dto;

import com.billing.entity.SaleItem;
import lombok.*;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SaleItemDTO {

    private Long id;
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

//    private Long id;
//    private String name;
//    private String code;
//    private String metalType;
//    private String purity;
//    private BigDecimal weight;
//    private BigDecimal stnWeight;
//    private BigDecimal netWeight;
//    private BigDecimal vaWeight;
//    private BigDecimal makingCharge;
//    private String stnType;
//    private BigDecimal stnCostPerCt;
//    private int pcs;
//    private String huid;
//    private BigDecimal rate;
//    private BigDecimal itemTotal;
//    private Long stockId;
//    private Long saleId;
//
//    public SaleItem mapDtoToEntity(SaleItemDTO saleItemDTO, SaleItem saleItem) {
//        saleItem.setId(saleItemDTO.getId());
//        saleItem.setName(saleItemDTO.getName());
//        saleItem.setCode(saleItemDTO.getCode());
//        saleItem.setMetalType(saleItemDTO.getMetalType());
//        saleItem.setPurity(saleItemDTO.getPurity());
//        saleItem.setWeight(saleItemDTO.getWeight());
//        saleItem.setStnWeight(saleItemDTO.getStnWeight());
//        saleItem.setNetWeight(saleItemDTO.getNetWeight());
//        saleItem.setVaWeight(saleItemDTO.getVaWeight());
//        saleItem.setMakingCharge(saleItemDTO.getMakingCharge());
//        saleItem.setStnType(saleItemDTO.getStnType());
//        saleItem.setStnCostPerCt(saleItemDTO.getStnCostPerCt());
//        saleItem.setPcs(saleItemDTO.getPcs());
//        saleItem.setHuid(saleItemDTO.getHuid());
//        saleItem.setRate(saleItemDTO.getRate());
//        saleItem.setItemTotal(saleItemDTO.getItemTotal());
//        return saleItem;
//
//    }
}
