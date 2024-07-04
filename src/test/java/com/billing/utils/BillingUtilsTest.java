//package com.billing.utils;
//
//import com.billing.constant.Metal;
//import com.billing.dto.EstRequest;
//import com.billing.dto.EstResponse;
//import com.billing.entity.ItemMaster;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigDecimal;
//
//@SpringBootTest
//public class BillingUtilsTest {
//
//    @Autowired
//    BillingUtils billingUtils;
//
//    @Test
//    public void estimationCalcTest() {
//        EstRequest estRequest = EstRequest.builder()
//                .goldRate(BigDecimal.valueOf(5870))
//                .silverRate(BigDecimal.valueOf(80))
//                .itemMaster(ItemMaster.builder()
//                        .itemName("BELT")
//                        .itemCode("XYZ")
//                        .itemHuid("7865T8")
//                        .itemQuality("916")
//                        .itemMetal(Metal.GOLD)
//                        .stoneName(null)
//                        .pcs(2)
//                        .weight(BigDecimal.valueOf(90.260))
//                        .stoneWeight(BigDecimal.ZERO)
//                        .vaPercentage(BigDecimal.valueOf(11))
//                        .build())
//                .isGstEstimation("NO")
//                .discount(BigDecimal.ZERO)
//                .build();
//        EstResponse estResponse = billingUtils.estimationCalc2(estRequest);
//        System.out.println("========================================================");
//        System.out.print("Date: \t" + estResponse.getDateTime() + "\tEstimation No:\t" + estResponse.getEstimationNo());
//        System.out.println();
//        System.out.print("Gold: \t" + estRequest.getGoldRate() + "\tSilver:\t" + estRequest.getSilverRate());
//        System.out.println();
//        System.out.println("Final Bill Amount: " + estResponse.getGrandTotalAfterDiscount());
//
//
//        System.out.println("");
//        System.out.println("========================================================");
//    }
//
//    @Test
//    public void estimationCalcDefaultMcTest() {
//        EstRequest estRequest = EstRequest.builder()
//                .goldRate(BigDecimal.valueOf(5870))
//                .silverRate(BigDecimal.valueOf(80))
//                .itemMaster(ItemMaster.builder()
//                        .itemName("BELT")
//                        .itemCode("XYZ")
//                        .itemHuid("7865T8")
//                        .itemQuality("916")
//                        .itemMetal(Metal.GOLD)
//                        .stoneName(null)
//                        .pcs(2)
//                        .weight(BigDecimal.valueOf(90.260))
//                        .stoneWeight(BigDecimal.ZERO)
//                        .vaPercentage(BigDecimal.valueOf(11))
//                        .build())
//                .defaultMcEnabled("NO")
//                .isGstEstimation("YES")
//                .discount(BigDecimal.ZERO)
//                .build();
//        EstResponse estResponse = billingUtils.estimationCalc2(estRequest);
//        System.out.println("=============================================================");
//        System.out.println("Date: \t" + estResponse.getDateTime() + "\tEstimation No:\t" + estResponse.getEstimationNo());
//        System.out.println("=============================================================");
//        System.out.println();
//        System.out.println("Gold: \t" + estRequest.getGoldRate() + "\t\t\t\t\tSilver:\t" + estRequest.getSilverRate());
//        System.out.println("=============================================================");
//        System.out.println("Product \t\t Weight\t\t");
//        System.out.println(" \t\t\t Gr.Weight\t\t\t\t Amount");
//        System.out.println("=============================================================");
//        System.out.println(estRequest.getItemMaster().getItemName());
//        System.out.println("\t\t\t\t" + estRequest.getItemMaster().getWeight() + "\t(" + estRequest.getVaPercentage() + " %)\t\t" + estResponse.getVaPrice() + "\t\t" + estResponse.getTotalPrice());
//        System.out.println("Stone Weight\t\t" + estRequest.getItemMaster().getStoneWeight());
//        System.out.println("Net Weight\t\t" + estRequest.getItemMaster().getStoneWeight());
//        System.out.println("Stn:\t\t" + estRequest.getItemMaster().getStoneWeight().multiply(BigDecimal.valueOf(0.2)) + "Ct X " + 550);
//
//
//        System.out.println();
////        System.out.println("VA: \t"+estimation.getv);
//        System.out.println("Final Bill Amount: " + estResponse.getGrandTotalAfterDiscount());
//
//
//        System.out.println("");
//        System.out.println("========================================================");
//    }
//
//    @Test
//    public void estimationCalcWithGstTest() {
//        EstRequest estRequest = EstRequest.builder()
//                .goldRate(BigDecimal.valueOf(5870))
//                .silverRate(BigDecimal.valueOf(80))
//                .itemMaster(ItemMaster.builder()
//                        .itemName("BELT")
//                        .itemCode("XYZ")
//                        .itemHuid("7865T8")
//                        .itemQuality("916")
//                        .itemMetal(Metal.GOLD)
//                        .stoneName(null)
//                        .pcs(2)
//                        .weight(BigDecimal.valueOf(90.260))
//                        .stoneWeight(BigDecimal.ZERO)
//                        .vaPercentage(BigDecimal.valueOf(11))
//                        .build())
//                //.mc(BigDecimal.valueOf(4500))
//                .isGstEstimation("YES")
//                .discount(BigDecimal.ZERO)
//                .build();
//        EstResponse estResponse = billingUtils.estimationCalc2(estRequest);
//        System.out.println("========================================================");
//        System.out.print("Date: \t" + estResponse.getDateTime() + "\tEstimation No:\t" + estResponse.getEstimationNo());
//        System.out.println();
//        System.out.print("Gold: \t" + estRequest.getGoldRate() + "\tSilver:\t" + estRequest.getSilverRate());
//        System.out.println();
//        System.out.println("Final Bill Amount: " + estResponse.getGrandTotalAfterDiscount());
//
//
//        System.out.println("");
//        System.out.println("========================================================");
//    }
//}
