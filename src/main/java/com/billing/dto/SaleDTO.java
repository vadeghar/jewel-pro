package com.billing.dto;

import com.billing.entity.ExchangeItem;
import com.billing.entity.Sale;
import com.billing.entity.SaleItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SaleDTO {
    private Long id;

    private String saleType = "SALE"; // Sale / Sale Return
    private String customerName;
    private String invoiceNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate saleDate;
    private LocalDateTime lastUpdatedTs = LocalDateTime.now();
    private String paymentMode;
    private BigDecimal mcAmount;
    private String isGstSale = "NO";
    private BigDecimal cGstAmount;
    private BigDecimal sGstAmount;
    private BigDecimal totalSaleAmount;
    private BigDecimal totalExchangeAmount;
    private BigDecimal discount;
    private BigDecimal grandTotalSaleAmount;
    private BigDecimal paidAmount;
    private BigDecimal balAmount;
    private List<SaleItem> saleItemList = new ArrayList<>();
    private String description;
    private List<ExchangeItem> exchangedItems;

    public Sale toEntity() {
        return Sale.builder()
                .id(this.id)
                .saleType(this.saleType)
//                .customerName(this.customerName)
                .invoiceNo(this.invoiceNo)
                .saleDate(this.saleDate)
                .lastUpdatedTs(this.lastUpdatedTs)
                .paymentMode(this.paymentMode)
//                .mcAmount(this.mcAmount)
                .isGstSale(this.isGstSale)
                .cGstAmount(this.cGstAmount)
                .sGstAmount(this.sGstAmount)
//                .totalSaleAmount(this.totalSaleAmount)
                .saleItemList(this.saleItemList)
                .description(this.description)
                // Exchanged items
                .build();
    }
}
