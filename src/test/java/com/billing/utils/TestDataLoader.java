//package com.billing.utils;
//import com.billing.dto.PurchaseDTO;
//import com.billing.service.PurchaseService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.opencsv.CSVReader;
//import com.opencsv.exceptions.CsvValidationException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ThreadLocalRandom;
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class TestDataLoader {
//
//    @LocalServerPort
//    private int port;
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    PurchaseService purchaseService;
//    @Autowired
//    ObjectMapper objectMapper;
//    @Test
//    public  void savePurchaseData() {
//        try {
//            InputStream inputStream = new ClassPathResource("purchase_test_data.csv").getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//
//            // Use OpenCSV to read the CSV
//            CSVReader reader = new CSVReader(inputStreamReader);
//            String[] nextLine;
//            List<PurchaseDTO> purchaseDTOList = new ArrayList<>();
//            while ((nextLine = reader.readNext()) != null) {
//                PurchaseDTO purchaseDTO = new PurchaseDTO();
//                purchaseDTO.setActive(nextLine[0]);
//                purchaseDTO.setActualPurity(nextLine[1]);
//                purchaseDTO.setBalAmount(toBD(nextLine[2]));
//                purchaseDTO.setCreatedBy(nextLine[3]);
//                purchaseDTO.setIsGstPurchase(nextLine[4]);
//                purchaseDTO.setMetalType(nextLine[5]);
//                purchaseDTO.setPaidAmount(toBD(nextLine[6]));
//                purchaseDTO.setPaymentMode(nextLine[7]);
//                purchaseDTO.setPurchaseAmount(toBD(nextLine[8]));
//                purchaseDTO.setPurchaseBillNo(nextLine[9]);
//                purchaseDTO.setPurchaseDate(generateRandomDate());
//                purchaseDTO.setPurchasePurity(nextLine[11]);
//                purchaseDTO.setRate(toBD(nextLine[12]));
//                purchaseDTO.setPurchaseType(nextLine[13]);
//                purchaseDTO.setTotalCgstAmount(toBD(nextLine[14]));
//                purchaseDTO.setTotalGrossWeight(toBD(nextLine[15]));
//                purchaseDTO.setTotalMcAmount(toBD(nextLine[16]));
//                purchaseDTO.setTotalNetWeight(toBD(nextLine[17]));
//                purchaseDTO.setTotalPcs(Integer.valueOf(nextLine[18]));
//                purchaseDTO.setTotalPurchaseAmount(toBD(nextLine[19]));
//                purchaseDTO.setTotalSgstAmount(toBD(nextLine[20]));
//                purchaseDTO.setTotalStnWeight(toBD(nextLine[21]));
//                purchaseDTO.setSupplierId(Long.valueOf(nextLine[22]));
//                purchaseDTO.setDescription("");
//
//                //System.out.println(purchaseDTO.getTotalGrossWeight()+"->"+purchaseDTO.getTotalPcs());
//                System.out.println(objectMapper.writeValueAsString(purchaseDTO));
////
////                String jsonRequest = objectMapper.writeValueAsString(purchaseDTO);
////
////                HttpHeaders headers = new HttpHeaders();
////                headers.setContentType(MediaType.APPLICATION_JSON);
////
////                HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);
////
////                String baseUrl = "http://localhost:" + port + "/api/v1/purchase/save";
////
////                ResponseEntity<PurchaseDTO> response = restTemplate.postForEntity(baseUrl, request, PurchaseDTO.class);
////                Assertions.assertTrue(response.getStatusCodeValue() == 200);
//
//            }
//            System.out.println("Size: "+purchaseDTOList.size());
//            //purchaseService.saveAll(purchaseDTOList);
//        } catch (IOException | CsvValidationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static BigDecimal toBD(String s) {
//        if (s == null) {
//            return BigDecimal.ZERO;
//        } else {
//            return BigDecimal.valueOf(Double.valueOf(s));
//        }
//    }
//
//    private static LocalDate generateRandomDate() {
//        LocalDate startDate = LocalDate.of(2024, 1, 1);
//        LocalDate endDate = LocalDate.of(2024, 5, 31);
//        long startEpochDay = startDate.toEpochDay();
//        long endEpochDay = endDate.toEpochDay();
//        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay + 1);
//        return LocalDate.ofEpochDay(randomEpochDay);
//    }
//}
