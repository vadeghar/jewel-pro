package com.billing.entity;

import com.billing.constant.Metal;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name="item_master")
public class ItemMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Item attrs
    private String itemCode;
    private String itemName;
    private String tagNo;
    private int pcs;
    private BigDecimal weight;
    private BigDecimal vaPercentage;
    private BigDecimal wastageInGms;
    private String itemHuid;
    private String itemQuality;
    @Enumerated(EnumType.STRING)
    private Metal itemMetal;
    private BigDecimal mc;

    // Stone attrs
    private String stoneName;
    private int stonePcs;
    private BigDecimal stoneWeight;
    private BigDecimal stoneWtInCts;

}
