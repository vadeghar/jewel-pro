package com.billing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
@Table(name="estimation")
public class Estimation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estimationNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate estimationDate;
    @LastModifiedDate
    private LocalDateTime lastUpdatedTs;
    @CreatedDate
    private LocalDateTime createdDate;
    @CreatedBy
    private String createdBy;
    private String isGstEstimation;
    private BigDecimal cGstAmount;
    private BigDecimal sGstAmount;
    private BigDecimal totalEstimationAmount;
    private BigDecimal totalExchangeAmount;
    private BigDecimal discount;
    private BigDecimal grandTotalEstimationAmount;
    private String paymentMode;
    private BigDecimal paidAmount;
    private BigDecimal balAmount;
    private String description;
    @OneToMany(mappedBy = "estimation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EstimationItem> estimationItemList = new ArrayList<>();
    @OneToMany(mappedBy = "estimation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EstimationExchangeItem> exchangeItemList = new ArrayList<>();
    private String status;
    

}
