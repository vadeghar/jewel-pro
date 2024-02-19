package com.billing.utils;

import com.billing.constant.Constants;
import com.billing.constant.Metal;
import com.billing.dto.EstRequest;
import com.billing.dto.EstResponse;
import com.billing.entity.StoneMaster;
import com.billing.exception.EstimationException;
import com.billing.service.StoneMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Slf4j
public class BillingUtils {



//    public void estimationCalc(Estimation estimation) {
//        log.debug("Inside estimationCalc");
//        BigDecimal totalPrice = BigDecimal.ZERO;
//        BigDecimal netWeight = estimation.getWeight().subtract(estimation.getStoneWeight()); // Net weight
//        estimation.setNetWeight(netWeight); // Set netWeight
//        BigDecimal vaWeight = estimation.getNetWeight().multiply(estimation.getVaPercentage()).divide(BigDecimal.valueOf(100));
//        BigDecimal netWeightWithVa = estimation.getNetWeight().add(vaWeight);
//        BigDecimal netWeightAndVaPrice = BigDecimal.ZERO;
//        log.debug("Metal: {}", estimation.getItemMetal());
//        if(estimation.getItemMetal() == Metal.GOLD) {
//            netWeightAndVaPrice = netWeightWithVa.multiply(estimation.getGoldRate());
//        } else if (estimation.getItemMetal() == Metal.SILVER) {
//            netWeightAndVaPrice = netWeightWithVa.multiply(estimation.getSilverRate());
//        } else {
//            throw new EstimationException("Invalid metal.");
//        }
//        totalPrice = totalPrice.add(netWeightAndVaPrice);
//        estimation.setNetWeightAndVaPrice(netWeightAndVaPrice); // Set netWeightAndVaPrice
//        if (Objects.nonNull(estimation.getStoneName()) && Objects.nonNull(estimation.getStoneWeight())) {
//            StoneMaster stoneMaster = stoneMasterService.getStoneMasterByName(estimation.getStoneName());
//            BigDecimal stoneCts = estimation.getStoneWeight().divide(BigDecimal.valueOf(0.2));
//            BigDecimal stonePrice = stoneCts.multiply(BigDecimal.valueOf(stoneMaster.getPricePerCt()));
//            estimation.setStonePrice(stonePrice); // Set stone price
//            totalPrice = totalPrice.add(stonePrice);
//        }
//        if(Constants.YES.contains(estimation.getDefaultMcEnabled())) {
//            if (Objects.isNull(estimation.getMc()) || estimation.getMc() == BigDecimal.ZERO) {
//                totalPrice = totalPrice.add(estimation.getNetWeightAndVaPrice());
//            }
//        } else {
//            totalPrice = totalPrice.add(estimation.getMc());
//        }
//
//        estimation.setTotalPrice(totalPrice); // Set totalPrice
//        BigDecimal grandTotal = estimation.getTotalPrice();
//
//        if (Constants.YES.contains(estimation.getIsGstEstimation())) {
//            // total + (total * (cGst/100))
//            BigDecimal cGstPrice = estimation.getTotalPrice().multiply(BigDecimal.valueOf(estimation.getCGstPercentage()).divide(BigDecimal.valueOf(100)));
//            BigDecimal sGstPrice = estimation.getTotalPrice().multiply(BigDecimal.valueOf(estimation.getSGstPercentage()).divide(BigDecimal.valueOf(100)));
//            grandTotal = grandTotal.add(cGstPrice).add(sGstPrice);
//        }
//        estimation.setGrandTotal(grandTotal); // Set grandTotal
//        BigDecimal grandTotalAfterDiscount = estimation.getGrandTotal().subtract(estimation.getDiscount());
//        estimation.setGrandTotalAfterDiscount(grandTotalAfterDiscount); // Set grandTotalAfterDiscount
//        log.debug("Exiting estimationCalc");
//    }


    public EstResponse estimationCalc2(EstRequest estimation) {
        log.debug("Inside estimationCalc");
        EstResponse estResponse = new EstResponse();
        int randomNumber = (int) (Math.random() * 90000) + 10000;
        estResponse.setEstimationNo(String.valueOf(randomNumber));
        BigDecimal metalPrice = BigDecimal.ZERO;
        if (estimation.getItemMetal() == Metal.GOLD) {
            metalPrice = estimation.getGoldRate();
        } else if (estimation.getItemMetal() == Metal.SILVER) {
            metalPrice = estimation.getSilverRate();
        } else {
            throw new EstimationException("Invalid metal.");
        }
        BigDecimal totalPrice = BigDecimal.ZERO;
        log.info("Item gross weight: {}", estimation.getWeight());
        BigDecimal vaWeight = roundToNearestForWeight(estimation.getWeight().multiply(estimation.getVaPercentage()).divide(BigDecimal.valueOf(100)));
        log.info("VA weight for {}% is {}", estimation.getVaPercentage(), vaWeight);
//        log.info("After rounding: {}", roundToNearestForWeight(vaWeight));
        BigDecimal vaPrice = roundToNearestForCurrency(vaWeight.multiply(metalPrice));
        log.info("VA Rate: {} ", vaPrice);
        estResponse.setVaPrice(vaPrice);
        log.info("Stone weight {}", estimation.getStoneWeight());
        BigDecimal netWeight = estimation.getWeight();
        estResponse.setStonePrice(BigDecimal.ZERO);
        if (Objects.nonNull(estimation.getStoneWeight()) && estimation.getStoneWeight() != BigDecimal.ZERO) {
            netWeight = estimation.getWeight().subtract(estimation.getStoneWeight()); // Net weight
            BigDecimal stoneCts = estimation.getStoneWeight().divide(BigDecimal.valueOf(0.2)); // Stone weight in cts
            log.info("Stone Wt in cts: {}", stoneCts);
            estResponse.setStoneWtInCts(stoneCts);
            StoneMaster stoneMaster = new StoneMaster();
            stoneMaster.setStoneName("Stones");
            stoneMaster.setPricePerCt(550d);
            log.info("Stone cost per ct: {}", stoneMaster.getPricePerCt());
            estResponse.setStonePricePerCt(BigDecimal.valueOf(stoneMaster.getPricePerCt()));
            BigDecimal stonePrice = roundToNearestForCurrency(stoneCts.multiply(BigDecimal.valueOf(stoneMaster.getPricePerCt())));
            log.info("Stone price: {}", stonePrice);
            estResponse.setStonePrice(stonePrice); // Set stone price
        }
        log.info("Net weight: {}", netWeight);
        estResponse.setNetWeight(netWeight); // Set netWeight
        BigDecimal netWeightPrice = roundToNearestForCurrency(estResponse.getNetWeight().multiply(metalPrice));
        log.info("Net weight price: {}", netWeightPrice);
        estResponse.setNetWeightPrice(netWeightPrice);
        estResponse.setTotalPriceInclVa(netWeightPrice.add(vaPrice));

        totalPrice = estResponse.getVaPrice().add(estResponse.getStonePrice()).add(estResponse.getNetWeightPrice());
        log.info("Total price: {}", totalPrice);
        estResponse.setTotalPrice(totalPrice);
        BigDecimal mc = Objects.nonNull(estResponse.getMc()) ? estResponse.getMc() : BigDecimal.ZERO;
        if (Constants.YES.contains(estimation.getDefaultMcEnabled())) {
            if (Objects.isNull(estResponse.getMc()) || estResponse.getMc() == BigDecimal.ZERO) {
                mc = roundToNearestForCurrency(mc.add(estResponse.getVaPrice().multiply(BigDecimal.valueOf(0.09))));
                log.info("Making charge auto calculated: {}", mc);
            }
        }
        log.info("Making charge: {}", mc);
        estResponse.setMc(mc);
        estResponse.setTotalPriceInclMc(estResponse.getTotalPrice().add(estResponse.getMc()));
        log.info("Total price Incl of MC: {}", estResponse.getTotalPriceInclMc());
        BigDecimal totalPriceInclGst = estResponse.getTotalPriceInclMc();

        if (Constants.YES.contains(estimation.getIsGstEstimation())) {
            // total + (total * (cGst/100))
            BigDecimal cGstPrice = roundToNearestForCurrency(estResponse.getTotalPrice().multiply(BigDecimal.valueOf(estResponse.getCGstPercentage()).divide(BigDecimal.valueOf(100))));
            BigDecimal sGstPrice = roundToNearestForCurrency(estResponse.getTotalPrice().multiply(BigDecimal.valueOf(estResponse.getSGstPercentage()).divide(BigDecimal.valueOf(100))));
            estResponse.setCGstPrice(cGstPrice);
            estResponse.setSGstPrice(sGstPrice);
            log.info("CGST: {}, SGST: {}", cGstPrice, sGstPrice);
            totalPriceInclGst = totalPriceInclGst.add(cGstPrice).add(sGstPrice);
        }
        log.info("Total price Incl of GST: {}", totalPriceInclGst);
        estResponse.setTotalPriceInclGst(totalPriceInclGst);
        log.info("Discount: {}", estimation.getDiscount());
        BigDecimal grandTotalAfterDiscount = estResponse.getTotalPriceInclGst().subtract(estimation.getDiscount());
        estResponse.setGrandTotalAfterDiscount(grandTotalAfterDiscount); // Set grandTotalAfterDiscount
        log.info("Grand total after discount: {}", estResponse.getGrandTotalAfterDiscount());
        log.debug("Exiting estimationCalc");
        return estResponse;
    }


    public static BigDecimal roundToNearestForCurrency(BigDecimal value) {
        BigDecimal nickelValue = new BigDecimal("0.1");
        BigDecimal divided = value.divide(nickelValue, 0, RoundingMode.HALF_UP);
        return divided.multiply(nickelValue).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal roundToNearestForWeight1(BigDecimal value) {
        BigDecimal nickelValue = new BigDecimal("0.05");
        BigDecimal divided = value.divide(nickelValue, 0, RoundingMode.HALF_UP);
        return divided.multiply(nickelValue);
    }

    public static BigDecimal roundToNearestForWeight(BigDecimal value) {
        BigDecimal nickelValue = new BigDecimal("0.001");
        return value.divide(nickelValue, 0, RoundingMode.HALF_UP).multiply(nickelValue);
    }
}
