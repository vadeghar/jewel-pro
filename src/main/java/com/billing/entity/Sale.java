package com.billing.entity;

import com.billing.dto.SaleDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@Entity
@Table(name="sale")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(exclude = {"customer"})
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String saleType = "SALE"; // Sale / Sale Return
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private String invoiceNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate saleDate;
    @LastModifiedDate
    private LocalDateTime lastUpdatedTs;
    @CreatedDate
    private LocalDateTime createdDate;
    @CreatedBy
    private String createdBy;

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
    private List<SaleItem> saleItemList = new ArrayList<>();
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExchangeItem> exchangeItemList = new ArrayList<>();

    public SaleDTO toDTO() {
        return SaleDTO.builder()
            .id(this.id)
            .saleType(this.saleType)
//            .customer(this.customerId)
            .invoiceNo(this.invoiceNo)
            .saleDate(this.saleDate)
            .lastUpdatedTs(this.lastUpdatedTs)
            .isGstSale(this.isGstSale)
            .cgstAmount(this.cGstAmount)
            .sgstAmount(this.sGstAmount)
            .totalSaleAmount(this.totalSaleAmount)
            .totalExchangeAmount(this.totalExchangeAmount)
            .discount(this.discount)
            .grandTotalSaleAmount(this.grandTotalSaleAmount)
            .paymentMode(this.paymentMode)
            .paidAmount(this.paidAmount)
            .balAmount(this.balAmount)
//            .saleItemList(!CollectionUtils.isEmpty(this.saleItemList) ? this.saleItemList.stream().map(saleItem -> saleItem.toDto(saleItem)).collect(Collectors.toList()) : new ArrayList<>())
//            .exchangedItems(!CollectionUtils.isEmpty(this.exchangeItemList) ? this.exchangeItemList.stream().map(exchangeItem -> exchangeItem.toDto()).collect(Collectors.toList()) : new ArrayList<>())
            .description(this.description)
            .build();
    }
}
