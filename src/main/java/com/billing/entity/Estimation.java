//package com.billing.entity;
//
//import com.billing.constant.Metal;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name="estimation")
//public class Estimation {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private LocalDateTime dateTime; // OUT Param
//    private String estimationNo; // OUT Param
//    private BigDecimal goldRate; // OUT Param
//    private BigDecimal silverRate; // OUT Param
//    private String itemDesc; // IN Param
//    private String tagNo;
//    private String itemCode;
//    private int pcs; // IN Param
//    private BigDecimal weight; // IN Param
//    private BigDecimal vaPercentage; // IN/OUT Param
//    private BigDecimal vaPrice; // IN/OUT Param
//    private BigDecimal stoneWeight; // IN Param
//    private BigDecimal netWeight; // OUT Param
//    private BigDecimal netWeightPrice;
//    private String stoneName; // IN Param
//    private int stonePcs; // IN Param
//    private BigDecimal stoneWtInCts; // OUT Param
//    private BigDecimal stonePricePerCt; // OUT Param
//    private BigDecimal stonePrice; // OUT Param
//    private BigDecimal totalPriceInclMc;
//    private BigDecimal totalPriceInclVa;
//    private String isGstEstimation = "NO";
//    private Double cGstPercentage = 1.5;
//    private Double sGstPercentage = 1.5;
//    private BigDecimal totalPriceInclGst; // OUT Param
//    private BigDecimal discount = BigDecimal.ZERO;
//    private BigDecimal grandTotalAfterDiscount; // OUT Param
//    private String itemHuid;
//    private String itemQuality;
//    private Metal itemMetal;
//    private BigDecimal totalPrice;
//    private String defaultMcEnabled = "NO";
//    private BigDecimal mc;
//}
