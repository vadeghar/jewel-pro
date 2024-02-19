package com.billing.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EstimationList {
    private List<EstResponse> estimations;
    private LocalDateTime dateTime;
    private String estimationNo;
    private BigDecimal goldRate;
    private BigDecimal silverRate;
    private String cGst;
    private String sGst;
    private String finalAmount;
}
