package com.billing.print;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

    public class ThermalPrinter {
        private Socket socket;
        private OutputStream outputStream;
        private static final int PAPER_WIDTH = 203 * 3; // 3 inches * 24 dots per inch

        public ThermalPrinter(String ipAddress, int port) {
//            try {
//                socket = new Socket(ipAddress, port);
//                outputStream = socket.getOutputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        public void print(String text, Alignment alignment) {
            try {
                // Calculate padding for alignment
                int padding = 0;
                if (alignment == Alignment.CENTER) {
                    padding = (PAPER_WIDTH - text.length()) / 2;
                } else if (alignment == Alignment.RIGHT) {
                    padding = PAPER_WIDTH - text.length();
                }

                // Construct padded text
                StringBuilder paddedText = new StringBuilder();
                for (int i = 0; i < padding; i++) {
                    paddedText.append(" ");
                }
                paddedText.append(text);

                // Send text to the printer
                outputStream.write(paddedText.toString().getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void previewBill(String[] items, double[] prices, double total) {
            try {
                // Create a new PDF document
                Document document = new Document(PageSize.LETTER);
                document.setMargins(10, 10, 10, 10);
                PdfWriter.getInstance(document, new FileOutputStream("bill_receipt1.pdf"));

                // Open the document
                document.open();

                // Add bill details to the document
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                document.add(new Paragraph("Bill Receipt"));
                document.add(new Paragraph("Date: " + dateFormat.format(new Date())));
                document.add(new Paragraph("----------------------------------------------"));
                for (int i = 0; i < items.length; i++) {
                    document.add(new Paragraph(String.format("%-30s %10.2f", items[i], prices[i])));
                }
                document.add(new Paragraph("----------------------------------------------"));
                document.add(new Paragraph(String.format("%-30s %10.2f", "Total:", total)));

                // Close the document
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void close() {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public enum Alignment {
            LEFT, CENTER, RIGHT
        }

        public static void main(String[] args) {
            // Example usage
            ThermalPrinter printer = new ThermalPrinter("192.168.1.100", 9100); // Change IP and port accordingly
            String[] items = {"Item 1", "Item 2", "Item 3"};
            double[] prices = {10.00, 20.00, 30.00};
            double total = 60.00;
            printer.previewBill(items, prices, total);
            printer.close();
        }

//    public static void main(String[] args) {
        // Example usage
//        ThermalPrinter printer = new ThermalPrinter("192.168.1.100", 9100); // Change IP and port accordingly
//        printer.print("Left aligned text", Alignment.LEFT);
//        printer.print("Center aligned text", Alignment.CENTER);
//        printer.print("Right aligned text", Alignment.RIGHT);
//        printer.preview("Preview text in PDF", Alignment.LEFT);
//        printer.close();
//        public static void main(String[] args)
//        {
//            Document document = new Document();
//            try
//            {
//                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("HelloWorld.pdf"));
//                document.open();
//                document.add(new Paragraph("A Hello World PDF document."));
//                document.close();
//                writer.close();
//            } catch (DocumentException e)
//            {
//                e.printStackTrace();
//            } catch (FileNotFoundException e)
//            {
//                e.printStackTrace();
//            }
//        }
}

