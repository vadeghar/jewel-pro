package com.billing.entity;

import com.billing.dto.SaleDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String saleType = "SALE"; // Sale / Sale Return
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private String invoiceNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate saleDate;
    private LocalDateTime lastUpdatedTs = LocalDateTime.now();

    private String isGstSale;
    private BigDecimal cGstAmount;
    private BigDecimal sGstAmount;

    private BigDecimal totalSaleAmount;
    private BigDecimal totalExchangeAmount;
    private BigDecimal discount;
    private BigDecimal grandTotalSaleAmount;
    private String paymentMode;
    private BigDecimal paidAmount;
    private BigDecimal balAmount;
    private String description;
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = CascadeType.ALL)
    private Set<SaleItem> saleItemList = new HashSet<>();
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ExchangeItem> exchangeItemList = new HashSet<>();

    public SaleDTO toDTO() {
        return SaleDTO.builder()
            .id(this.id)
            .saleType(this.saleType)
            .customer(this.customer)
            .invoiceNo(this.invoiceNo)
            .saleDate(this.saleDate)
            .lastUpdatedTs(this.lastUpdatedTs)
            .isGstSale(this.isGstSale)
            .cGstAmount(this.cGstAmount)
            .sGstAmount(this.sGstAmount)
            .totalSaleAmount(this.totalSaleAmount)
            .totalExchangeAmount(this.totalExchangeAmount)
            .discount(this.discount)
            .grandTotalSaleAmount(this.grandTotalSaleAmount)
            .paymentMode(this.paymentMode)
            .paidAmount(this.paidAmount)
            .balAmount(this.balAmount)
            .saleItemList(new ArrayList<>(this.saleItemList))
            .exchangedItems(new ArrayList<>(this.exchangeItemList))
            .description(this.description)
            .build();
    }
}
