package com.billing.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleTest {


        // Define constants for prefix and pattern
        private static final String PREFIX = "INV-";
        private static final String PATTERN = "yyMMdd";
    private static int serialNumber = 0;

        // Method to generate invoice number
        private static String generateInvoiceNumber() {
            Long maxId = 34l;
            int year = LocalDate.now().getYear() % 100; // Last two digits of the year
            int month = LocalDate.now().getMonthValue(); // Current month
            int prefix = 1000 % (year+month);
//            maxId = (maxId + 1) % 100000;

            // Format the unique number with leading zeros if needed
            return  String.format("%02d%05d", prefix, maxId);
        }
    public static void main(String[] args) {
        // Generate a unique 6-digit number
        String uniqueNumber = generateInvoiceNumber();

        // Output the generated unique number
        System.out.println("Generated Unique Number: " + uniqueNumber);














    }


}
