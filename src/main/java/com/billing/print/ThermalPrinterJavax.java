package com.billing.print;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;

public class ThermalPrinterJavax {

    public static void main(String[] args) {
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
        attrs.add(new PrinterName("localhost", null));
        attrs.add(MediaSizeName.ISO_A7);

        PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, attrs);
        if (services.length > 0) {
            DocPrintJob job = services[0].createPrintJob();
            String billContent = "Your bill content here...";
            Doc doc = new SimpleDoc(billContent.getBytes(), flavor, null);
            try {
                job.print(doc, attrs);
            } catch (PrintException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No suitable printer found.");
        }
    }

//
//    public static void main(String[] args) {
//        // Find the printer service by name
//        String printerName = "localhost"; // Replace with your thermal printer name
//        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//        PrintService thermalPrinter = null;
//
//        for (PrintService service : services) {
//            if (service.getName().equals(printerName)) {
//                thermalPrinter = service;
//                break;
//            }
//        }
//
//        if (thermalPrinter != null) {
//            try {
//                // Create a DocFlavor for plain text
//                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//
//                // Set up printing attributes
//                PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
//
//                // Create a new attribute set and add the PrinterName attribute directly to it
//                PrintRequestAttributeSet printerAttributes = new HashPrintRequestAttributeSet();
//                printerAttributes.add(new PrinterName(printerName, null));
//
//                // Create a Doc for your billing data
//                String billingData = generateBillingData(); // Method to generate your billing data
//                InputStream inputStream = new ByteArrayInputStream(billingData.getBytes());
//                Doc doc = new SimpleDoc(inputStream, flavor, null);
//
//                // Create a DocPrintJob
//                DocPrintJob printJob = thermalPrinter.createPrintJob();
//
//                // Print the document with the printer attributes
//                printJob.print(doc, printerAttributes);
//
//                // Close the input stream
//                inputStream.close();
//
//                System.out.println("Printing completed successfully.");
//            } catch (PrintException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Thermal printer not found.");
//        }
//    }
//
//    // Method to generate your billing data
//    private static String generateBillingData() {
//        // Generate your billing data here, for example:
//        StringBuilder data = new StringBuilder();
//        data.append("Your billing information goes here...\n");
//        data.append("Item 1: $10\n");
//        data.append("Item 2: $20\n");
//        data.append("Total: $30\n");
//        return data.toString();
//    }
}

