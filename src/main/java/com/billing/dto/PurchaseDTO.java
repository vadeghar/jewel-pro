package com.billing.dto;

import com.billing.entity.Purchase;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseDTO {

    private Long id;
    private String purchaseType = "PURCHASE"; // Purchase / Purchase Return
    private String purchaseBillNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    private String metalType;
    private BigDecimal totalGrossWeight;
    private String actualPurity;
    private String purchasePurity;
    private BigDecimal totalNetWeight;
    private BigDecimal purchaseAmount;
    private BigDecimal rate;
    private Integer totalPcs;
    private BigDecimal totalStnWeight;
    private String paymentMode;
    private BigDecimal paidAmount;
    private BigDecimal balAmount;
    private BigDecimal totalMcAmount;
    private BigDecimal purchaseRate;
    private String isGstPurchase;
    private String gstNo;
    private BigDecimal totalCgstAmount = BigDecimal.ZERO;
    private BigDecimal totalSgstAmount = BigDecimal.ZERO;
    private BigDecimal totalPurchaseAmount;

    private String description;
    private String supplierName;
    private Long supplierId;

    private String createdBy;

    private List<PurchaseItemDTO> purchaseItems;

    public void toDto(Purchase entity) {
        this.setId(entity.getId());
        this.setPurchaseType(entity.getPurchaseType());
        this.setPurchaseBillNo(entity.getPurchaseBillNo());
        this.setPurchaseDate(entity.getPurchaseDate());
        this.setMetalType(entity.getMetalType());
        this.setTotalGrossWeight(entity.getTotalGrossWeight());
        this.setActualPurity(entity.getActualPurity());
        this.setPurchasePurity(entity.getPurchasePurity());
        this.setTotalNetWeight(entity.getTotalNetWeight());
        this.setPurchaseAmount(entity.getPurchaseAmount());
        this.setRate(entity.getPurchaseRate());
        this.setTotalPcs(entity.getTotalPcs());
        this.setTotalStnWeight(entity.getTotalStnWeight());
        this.setPaymentMode(entity.getPaymentMode());
        this.setPaidAmount(entity.getPaidAmount());
        this.setBalAmount(entity.getBalAmount());
        this.setTotalMcAmount(entity.getTotalMcAmount());
        this.setPurchaseRate(entity.getPurchaseRate());
        this.setIsGstPurchase(entity.getIsGstPurchase());
        this.setGstNo(entity.getGstNo());
        this.setTotalCgstAmount(entity.getTotalCgstAmount());
        this.setTotalSgstAmount(entity.getTotalSGstAmount());
        this.setTotalPurchaseAmount(entity.getTotalPurchaseAmount());
        this.setDescription(entity.getDescription());
        this.setSupplierName(entity.getSupplier().getName());
        this.setSupplierId(entity.getSupplier().getId());
        this.setCreatedBy(entity.getCreatedBy());
    }

}
