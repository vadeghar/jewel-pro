package com.billing.entity;

import com.billing.dto.SaleDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
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
@Entity
@Table(name="sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private String invoiceNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate saleDate;
    private LocalDateTime lastUpdatedTs = LocalDateTime.now();
    private String isGstSale;
    private String saleType = "SALE"; // Sale / Sale Return
    private BigDecimal saleItemsTotal;
    private BigDecimal cGstAmount;
    private BigDecimal sGstAmount;
    private BigDecimal discount;
    private BigDecimal exchangeItemsTotal;
    private BigDecimal grandTotal;
    private String paymentMode;
    private BigDecimal paidAmount;
    private BigDecimal balAmount;
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItem> saleItemList = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = CascadeType.ALL)
    private List<ExchangeItem> exchangeItemList = new ArrayList<>();


    public SaleDTO toDTO() {
        return SaleDTO.builder()
            .id(this.id)
            .saleType(this.saleType)
            //.customerName(this.customerName)
            .invoiceNo(this.invoiceNo)
            .saleDate(this.saleDate)
            .lastUpdatedTs(this.lastUpdatedTs)
            .paymentMode(this.paymentMode)
            //.mcAmount(this.mcAmount)
            .isGstSale(this.isGstSale)
            .cGstAmount(this.cGstAmount)
            .sGstAmount(this.sGstAmount)
            //.totalSaleAmount(this.totalSaleAmount)
            .saleItemList(this.saleItemList)
            .description(this.description)
            // Exchanged items
            .build();
    }
}
