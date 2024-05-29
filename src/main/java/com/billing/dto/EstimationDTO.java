package com.billing.dto;

import com.billing.entity.Customer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EstimationDTO {
    private Long id;
    private String estimationType;
    private Customer customer;
    private String estimationNo;
    private LocalDate estimationDate;
    private LocalDateTime lastUpdatedTs;
    private String isGstEstimation;
    private BigDecimal cgstAmount;
    private BigDecimal sgstAmount;
    private BigDecimal totalEstimationAmount;
    private BigDecimal totalExchangeAmount;
    private BigDecimal discount;
    private BigDecimal grandTotalEstimationAmount;
    private String paymentMode;
    private BigDecimal paidAmount;
    private BigDecimal balAmount;
    private String description;
    private List<EstimationItemDTO> estimationItemList;
    private List<ExchangeItemDTO> exchangeItemList;
    private String trnLastFourDigits;

}
