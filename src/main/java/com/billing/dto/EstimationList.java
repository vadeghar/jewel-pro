package com.billing.dto;

import com.billing.entity.Estimation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstimationList {
    private List<EstResponse> estimations;
    private List<Estimation> estimationList;
    private LocalDateTime dateTime;
    private String estimationNo;
    private BigDecimal goldRate;
    private BigDecimal silverRate;
    private String cGst;
    private String sGst;
    private String finalAmount;
}
