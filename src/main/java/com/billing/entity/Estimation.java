package com.billing.entity;

import com.billing.constant.Metal;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

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

    private String existingItem;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime estimationDateTs;
    private String estimationNo;
    @Enumerated(EnumType.STRING)
    private Metal itemMetal;
    private BigDecimal rate;

    private String itemCode;
    private String itemName;
    private String tagNo;
    private int pcs;
    private String stoneName;
    private Long itemMasterId;

    private BigDecimal itemWeight;



    private BigDecimal vaPercentage; //if vaWeight is not null, (vaWeight / itemWeight) x 100
    private BigDecimal vaWeight; // if vaPercentage is not null, itemWeight x (vaPercentage / 100)
    private BigDecimal vaPrice; // vaWeight x GOLD Rate
    private BigDecimal weightInclVaWt; // vaWeight x GOLD Rate


    private BigDecimal stoneWeight;
    private BigDecimal stoneWtInCts; // IF stoneWeight is > 0, stoneWeight x 0.2
    private BigDecimal stonePricePerCt;
    private BigDecimal stonePrice; // stoneWtInCts x stonePricePerCt

    private BigDecimal netWeight; // (itemWeight - stoneWeight)
    private BigDecimal netWeightPrice; // netWeight x Rate

    private String defaultMcEnabled = "NO";
    private BigDecimal mc; // IF defaultMcEnabled = YES, (vaPercentage x 10/100) x Rate

    private String isGstEstimation = "NO";
    private Double cGstPercentage = 1.5;
    private Double sGstPercentage = 1.5;
    private BigDecimal sGstPrice;// IF isGstEstimation =  YES, grandTotalAfterDiscount x (sGstPercentage/100) x Rate
    private BigDecimal cGstPrice; // IF isGstEstimation =  YES, grandTotalAfterDiscount x (cGstPercentage/100) x Rate



    private BigDecimal totalPriceInclVa; // total-I (netWeightPrice + vaPrice )
    private BigDecimal totalPriceInclStn;// total-II (totalPriceInclVa + stonePrice)
    private BigDecimal totalPriceInclMc; // total-III (totalPriceInclStn + mc)
    private BigDecimal totalPriceInclGst;// total-IV (totalPriceInclMc + sGstPrice + cGstPrice)
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal totalPrice; // total-V (totalPriceInclVa + totalPriceInclStn + totalPriceInclMc)
    private BigDecimal grandTotalAfterDiscount; // total-VI (totalPrice - discount)


    public void calculate() {
        if (itemWeight != null && stoneWeight != null) {
            stoneWtInCts = stoneWeight.compareTo(BigDecimal.ZERO) > 0 ? stoneWeight.multiply(new BigDecimal("5")).setScale(3, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        } else {
            stoneWtInCts = BigDecimal.ZERO;
        }

        estimationDateTs = LocalDateTime.now();

        estimationNo = String.valueOf (((int) (Math.random() * 90000) + 10000));

        vaWeight = vaPercentage != null ? itemWeight.multiply(vaPercentage).divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP) : vaWeight.setScale(3, RoundingMode.HALF_UP);

        vaPercentage = vaPercentage == null ? vaWeight.divide(itemWeight, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")) : vaPercentage;

        vaPrice = rate != null && vaWeight != null ? vaWeight.multiply(rate).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        stonePricePerCt = stonePricePerCt != null ? stonePricePerCt.setScale(2, RoundingMode.HALF_UP) : null;

        stonePrice = stoneWtInCts != null && stonePricePerCt != null ? stoneWtInCts.multiply(stonePricePerCt).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        netWeight = itemWeight != null && stoneWeight != null ? itemWeight.subtract(stoneWeight).setScale(3, RoundingMode.HALF_UP) : itemWeight;
        weightInclVaWt = vaWeight != null ? netWeight.add(vaWeight) : netWeight;
        netWeightPrice = netWeight != null && rate != null ? netWeight.multiply(rate).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        mc = "YES".equals(defaultMcEnabled) && vaPercentage != null && rate != null ? vaPercentage.multiply(new BigDecimal("10")).divide(new BigDecimal("100")).multiply(rate).setScale(2, RoundingMode.HALF_UP) : mc.setScale(2, RoundingMode.HALF_UP) ;


        totalPriceInclVa = netWeightPrice != null && vaPrice != null ? netWeightPrice.add(vaPrice).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        totalPriceInclStn = totalPriceInclVa != null && stonePrice != null ? totalPriceInclVa.add(stonePrice).setScale(2, RoundingMode.HALF_UP) : totalPriceInclVa;

        totalPriceInclMc = totalPriceInclStn != null && mc != null ? totalPriceInclStn.add(mc).setScale(2, RoundingMode.HALF_UP) : totalPriceInclStn;

        sGstPrice = "YES".equals(isGstEstimation) && totalPriceInclMc != null ? totalPriceInclMc.multiply(new BigDecimal(sGstPercentage / 100)).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        cGstPrice = "YES".equals(isGstEstimation) && totalPriceInclMc != null ? totalPriceInclMc.multiply(new BigDecimal(cGstPercentage / 100)).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        totalPriceInclGst = totalPriceInclMc != null && sGstPrice != null && cGstPrice != null ? totalPriceInclMc.add(sGstPrice).add(cGstPrice).setScale(2, RoundingMode.HALF_UP) : totalPriceInclMc;

        totalPrice = totalPriceInclGst;
                //totalPriceInclVa != null && totalPriceInclStn != null && totalPriceInclMc != null ? totalPriceInclVa.add(totalPriceInclStn).add(totalPriceInclMc).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        grandTotalAfterDiscount = totalPrice != null && discount != null ? totalPrice.subtract(discount).setScale(2, RoundingMode.HALF_UP) : totalPrice;


    }

}
