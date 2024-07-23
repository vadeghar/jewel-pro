package com.billing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ChartData {
    private String label;
    private BigDecimal value;

    public ChartData(String label, BigDecimal value) {
        this.label = label;
        this.value = value;
    }
}
