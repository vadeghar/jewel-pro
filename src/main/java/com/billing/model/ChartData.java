package com.billing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class ChartData {
    private String label;
    private BigDecimal value;
}
