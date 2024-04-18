package com.billing.controller.rest;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PosPrintController {

    @GetMapping("/print")
    public String print() {
        // Get the default print service
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

        // Create a DocFlavor for the receipt
        DocFlavor docFlavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        // Create a Doc containing the receipt data
        byte[] receiptData = "This is the receipt data".getBytes();
        Doc doc = new SimpleDoc(receiptData, docFlavor, null);

        // Create a DocPrintJob
        DocPrintJob printJob = printService.createPrintJob();

        // Print the receipt
        try {
            printJob.print(doc, null);
        } catch (Exception e) {
            // Handle the exception
        }

        return "Receipt printed";
    }
}
