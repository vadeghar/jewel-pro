package com.billing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
//    public PurchaseDTO(String jsonString) {
//        // You can use a JSON library like Jackson to deserialize the JSON string into this object
//        // For simplicity, let's assume you have a Jackson ObjectMapper instance called objectMapper
//        try {
//            System.out.println("Inside constructor: "+jsonString);
//            PurchaseDTO purchaseDTO = new ObjectMapper().readValue(jsonString, PurchaseDTO.class);
//            System.out.println("PurchaseDTO: "+purchaseDTO);
//            // Copy the fields from the deserialized purchaseDTO to this object
//            // Example: this.purchaseType = purchaseDTO.getPurchaseType();
//        } catch (IOException e) {
//            e.printStackTrace(); // Handle the exception appropriately
//        }
//    }

}
