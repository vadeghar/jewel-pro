package com.billing.entity;

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

    private String saleType = "SALE"; // Sale / Sale Return
    private String customerName;
    private String invoiceNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate saleDate;
    private LocalDateTime lastUpdatedTs = LocalDateTime.now();



    private String paymentMode;
    private BigDecimal mcAmount;

    private String isGstSale;
    private BigDecimal cGstAmount;
    private BigDecimal sGstAmount;
    private BigDecimal totalSaleAmount;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItem> saleItemList = new ArrayList<>();
    private String description;
}
