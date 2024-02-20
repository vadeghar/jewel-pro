package com.billing.entity;

import com.billing.constant.Metal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="metal_rate")
public class MetalRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Metal itemMetal;
    private BigDecimal rate;
    private LocalDateTime lastUpdateAt;
    private String lastUpdatedBy;
}
