package com.billing.service;


import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.io.*;

@Service
public class MyPrintService {

    public void printContent(String content) {
        // Calculate the number of lines in the content
        int numberOfLines = content.split("\n").length;

        // Define paper size based on content height
        float widthInInches = 3.0f;
        float heightInInches = 0.1f + (numberOfLines * 0.2f); // Adjust this multiplier as needed
        heightInInches = widthInInches > heightInInches ? 11f : heightInInches;
        // Create custom media size
//        MediaSizeName mediaSizeName = new MediaSizeName("Custom Size", "Custom");
        MediaSize mediaSize = new MediaSize(widthInInches * 72, heightInInches * 72, MediaSize.INCH);

        // Create attributes for print job
        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
        attributeSet.add(MediaSizeName.ISO_A4);
        attributeSet.add(new MediaPrintableArea(0, 0, widthInInches, heightInInches, MediaPrintableArea.INCH));
        attributeSet.add(Sides.ONE_SIDED);
        attributeSet.add(PrintQuality.HIGH);
        attributeSet.add(OrientationRequested.PORTRAIT);


        // Find the thermal printer
//        PrintService service = findThermalPrinter();
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        PrintService service = ServiceUI.printDialog(null, 200, 200, services, defaultService,
                DocFlavor.INPUT_STREAM.GIF, attributeSet);
        Media[] res = (Media[]) service.getSupportedAttributeValues(Media.class, null, null);
        for (Media media : res) {
            if (media instanceof MediaSizeName) {
                MediaSizeName msn = (MediaSizeName) media;
                MediaSize ms = MediaSize.getMediaSizeForName(msn);
                float width = ms.getX(MediaSize.INCH);
                float height = ms.getY(MediaSize.INCH);
                System.out.println(media + ": width = " + width + "; height = " + height);
            }
        }

        // If no printer found, return
        if (service == null) {
            System.out.println("No thermal printer found.");
            return;
        }

        // Create print job
        DocPrintJob job = service.createPrintJob();

        // Prepare content for printing
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        // Create Doc
        Doc doc = new SimpleDoc(inputStream, DocFlavor.INPUT_STREAM.AUTOSENSE, null);

        // Add print job listener
        job.addPrintJobListener(new PrintJobAdapter() {
            public void printJobCompleted(PrintJobEvent event) {
                System.out.println("Print job completed.");
            }
            public void printJobFailed(PrintJobEvent event) {
                System.out.println("Print job failed.");
            }
        });

        try {

            // Print the document
            job.print(doc, attributeSet);
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }

    // Method to find thermal printer
    private PrintService findThermalPrinter() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : services) {
            if (service.getName().toLowerCase().contains("localhost")) {
                return service;
            }
        }
        return null;
    }

    // Sample content
    public static void main(String[] args) throws IOException {
        MyPrintService printerService = new MyPrintService();
        String content = "Sample item 1: $5.00\nSample item 2: $10.00\nTotal: $15.00";
        printerService.printContent(content);
//        printMain(args);
    }

    public static void printToFile(String outputFileName, String outputFileType,
                                   String inputFileName, PrintRequestAttributeSet attributes) throws IOException {

        // Determine whether the system can print to the specified type, and
        // get a factory object if so.
        // The name of this static method is way too long!
        StreamPrintServiceFactory[] factories = StreamPrintServiceFactory
                .lookupStreamPrintServiceFactories(null, outputFileType);

        // Error message if we can't print to the specified output type
        if (factories.length == 0) {
            System.out.println("Unable to print files of type: " + outputFileType);
            return;
        }

        // Open the output file
        FileOutputStream out = new FileOutputStream(outputFileName);
        // Get a PrintService object to print to that file
        StreamPrintService service = factories[0].getPrintService(out);
        // Print using the method below
        printToService(service, inputFileName, attributes);
        // And remember to close the output file
        out.close();
    }

    // Print the contents of the named file to the specified PrintService,
    // requesting the specified attributes.
    // This is shared code used by print() and printToFile() above.
    public static void printToService(PrintService service, String filename,
                                      PrintRequestAttributeSet attributes) throws IOException {
        // Figure out what type of file we're printing
        DocFlavor flavor = getFlavorFromFilename(filename);
        // Open the file
        InputStream in = new FileInputStream(filename);
        // Create a Doc object to print from the file and flavor.
        Doc doc = new SimpleDoc(in, flavor, null);
        // Create a print job from the service
        DocPrintJob job = service.createPrintJob();

        // Monitor the print job with a listener
        job.addPrintJobListener(new PrintJobAdapter() {
            public void printJobCompleted(PrintJobEvent e) {
                System.out.println("Print job complete");
                System.exit(0);
            }

            public void printDataTransferCompleted(PrintJobEvent e) {
                System.out.println("Document transfered to printer");
            }

            public void printJobRequiresAttention(PrintJobEvent e) {
                System.out.println("Print job requires attention");
                System.out.println("Check printer: out of paper?");
            }

            public void printJobFailed(PrintJobEvent e) {
                System.out.println("Print job failed");
                System.exit(1);
            }
        });

        // Now print the document, catching errors
        try {
            job.print(doc, attributes);
        } catch (PrintException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    // A utility method to return a DocFlavor object matching the
    // extension of the filename.
    public static DocFlavor getFlavorFromFilename(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1);
        extension = extension.toLowerCase();
        if (extension.equals("gif"))
            return DocFlavor.INPUT_STREAM.GIF;
        else if (extension.equals("jpeg"))
            return DocFlavor.INPUT_STREAM.JPEG;
        else if (extension.equals("jpg"))
            return DocFlavor.INPUT_STREAM.JPEG;
        else if (extension.equals("png"))
            return DocFlavor.INPUT_STREAM.PNG;
        else if (extension.equals("ps"))
            return DocFlavor.INPUT_STREAM.POSTSCRIPT;
        else if (extension.equals("txt"))
            return DocFlavor.INPUT_STREAM.TEXT_PLAIN_HOST;
            // Fallback: try to determine flavor from file content
        else
            return DocFlavor.INPUT_STREAM.AUTOSENSE;
    }

    // List names of all PrintServices that can support the attributes
    public static void queryServices(PrintRequestAttributeSet attributes) {
        // Find all services that can support the specified attributes
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, attributes);
        // Loop through available services
        for (int i = 0; i < services.length; i++) {
            // Print service name
            System.out.print(services[i].getName());

            // Then query and print the document types it can print
            DocFlavor[] flavors = services[i].getSupportedDocFlavors();
            for (int j = 0; j < flavors.length; j++) {
                // Filter out DocFlavors that have a representation class other
                // than java.io.InputStream.
                String repclass = flavors[j].getRepresentationClassName();
                if (!repclass.equals("java.io.InputStream"))
                    continue;
                System.out.println("\t" + flavors[j].getMimeType());
            }
        }
    }

    // List details about the named printer
    public static void queryPrinter(String printerName, PrintRequestAttributeSet attributes) {
        // Find the named printer
        PrintService service = getNamedPrinter(printerName, attributes);
        if (service == null) {
            System.out.println(printerName + ": no such printer capable of "
                    + "handling the specified attributes");
            return;
        }

        // Print status and other information about the printer
        System.out.println(printerName + " status:");
        Attribute[] attrs = service.getAttributes().toArray();
        for (int i = 0; i < attrs.length; i++)
            System.out.println("\t" + attrs[i].getName() + ": " + attrs[i]);

    }

    // Print the contents of the named file to the named printer (or to a
    // default printer if printerName is null) requesting the specified
    // attributes.
    public static void print(String printerName, String filename, PrintRequestAttributeSet attributes)
            throws IOException {
        // Look for a printer that can support the attributes
        PrintService service = getNamedPrinter(printerName, attributes);
        if (service == null) {
            System.out.println("Can't find a printer " + "with specified attributes");
            return;
        }
        // Print the file to that printer. See method definition below
        printToService(service, filename, attributes);
        // Let the user know where to pick up their printout
        System.out.println("Printed " + filename + " to " + service.getName());
    }

    // A utility method to look up printers that can support the specified
    // attributes and return the one that matches the specified name.
    public static PrintService getNamedPrinter(String name, PrintRequestAttributeSet attrs) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, attrs);
        if (services.length > 0) {
            if (name == null)
                return services[0];
            else {
                for (int i = 0; i < services.length; i++) {
                    if (services[i].getName().equals(name))
                        return services[i];
                }
            }
        }
        return null;
    }

    public static void printMain(String[] args) throws IOException {
        // These are values we'll set from the command-line arguments
        boolean query = false;
        String printerName = null;
        String inputFileName = null;
        String outputFileName = null;
        String outputFileType = null;
        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

        // Loop through the arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-q"))
                query = true; // Is this is query?
            else if (args[i].equals("-p")) // Specific printer name
                printerName = args[++i];
            else if (args[i].equals("-i")) // The file to print
                inputFileName = args[++i];
            else if (args[i].equals("-ps")) { // Print it to this file
                // Sun's Java 1.4 implementation only supports PostScript
                // output. Other implementations might offer PDF, for example.
                outputFileName = args[++i];
                outputFileType = "application/postscript";
            }
            // The rest of the arguments represent common printing attributes
            else if (args[i].equals("-color")) // Request a color printer
                attributes.add(Chromaticity.COLOR);
            else if (args[i].equals("-landscape")) // Request landscape mode
                attributes.add(OrientationRequested.LANDSCAPE);
            else if (args[i].equals("-letter")) // US Letter-size paper
                attributes.add(MediaSizeName.NA_LETTER);
            else if (args[i].equals("-a4")) // European A4 paper
                attributes.add(MediaSizeName.ISO_A4);
            else if (args[i].equals("-staple")) // Request stapling
                attributes.add(Finishings.STAPLE);
            else if (args[i].equals("-collate")) // Collate multiple copies
                attributes.add(SheetCollate.COLLATED);
            else if (args[i].equals("-duplex")) // Request 2-sided
                attributes.add(Sides.DUPLEX);
            else if (args[i].equals("-2")) // 2 pages to a sheet
                attributes.add(new NumberUp(2));
            else if (args[i].equals("-copies")) // how many copies
                attributes.add(new Copies(Integer.parseInt(args[++i])));
            else {
                System.out.println("Unknown argument: " + args[i]);
                System.exit(1);
            }
        }

        if (query) {
            // If the -q argument was specified, but no printer was named,
            // then list all available printers that can support the attributes
            if (printerName == null)
                queryServices(attributes);
                // Otherwise, look for a named printer that can support the
                // attributes and print its status
            else
                queryPrinter(printerName, attributes);
        } else if (outputFileName != null)
            // If this is not a query and we have a filename, print to a file
            printToFile(outputFileName, outputFileType, inputFileName, attributes);
        else
            // Otherwise, print to the named printer, or to the default
            // printer otherwise.
            print(printerName, inputFileName, attributes);

        // The main() method ends here, but there may be a printing thread
        // operating in the background. So the program may not terminate
        // until printing completes.
    }
}
