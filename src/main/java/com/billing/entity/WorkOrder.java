package com.billing.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "work_order")
@Entity
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Worker worker;

    private String itemName;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private BigDecimal netWeight;
    private BigDecimal vaOrWastage;
    private BigDecimal grossWeight;
    private BigDecimal rate;
    private BigDecimal mc;
    private BigDecimal orderTotal;
    private BigDecimal advancePaid;
    private BigDecimal balance;
}

