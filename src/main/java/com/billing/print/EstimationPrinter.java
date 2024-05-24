//package com.billing.print;
//
//import com.billing.dto.EstimationList;
//import com.billing.entity.Estimation;
//
//import java.awt.*;
//import java.awt.print.*;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//public class EstimationPrinter  implements Printable {
//
//    private EstimationList estimationList;
//    private static final String RECEIPT_FONT = "Monospaced";
//    public static final String DATE_FORMAT_NOW = "dd/MM/yyyy HH:mm";
//    private static final int PAPER_WIDTH = 3 * 72;
//    public  static String headingRow1[] = new String[] {"Item","","Code",""};
//    public  static String headingRow2[] = new String[] {"Grs.Wt","VA","","Amount"};
//    @Override
//    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
//        if (pageIndex > 0) {
//            return NO_SUCH_PAGE;
//        }
//        Graphics2D g2d = (Graphics2D) graphics;
//        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
//        String text = "Estimation";
//        // Heading
//        FontMetrics metrics = g2d.getFontMetrics();
//        int textWidth = metrics.stringWidth(text);
//        int centerX = (int) ((pageFormat.getImageableWidth() - textWidth) / 2);
////        int centerY = (int) (pageFormat.getImageableHeight() / 2);
//        g2d.setFont(new Font(RECEIPT_FONT, Font.BOLD, 14));
//        int x = 10;
//        int y = 20;
//        g2d.drawString(text, centerX, y);
//
//        g2d.drawString("Dt:"+now()+"       Est #:"+estimationList.getEstimationNo(), x, y);
//        // New line here
//        y += 12;
//        String col1Txt = "G.Rate:  "+estimationList.getGoldRate();
//        String col2Txt = "S.Rate: "+estimationList.getSilverRate();
//        g2d.drawString(col1Txt, x, y);
//        textWidth = metrics.stringWidth(col2Txt);
//        int tmpX1 = PAPER_WIDTH - textWidth-10;
//        g2d.drawString(col2Txt, tmpX1, y);
//        // Line end
//
//        // New line here
//        y += 5;
//        g2d.drawLine(x, y, PAPER_WIDTH-10, y);
//        // Line end
//
//        // New line here
//        y += 10;
//        g2d.drawString(headingRow1[0], 10 ,y);
//        g2d.drawString(headingRow1[1], 50 ,y);
//        g2d.drawString(headingRow1[2], 100 ,y);
//        g2d.drawString(headingRow1[3], 150 ,y);
//        // Line end
//
//        // New line here
//        y += 13;
//        g2d.drawString(headingRow2[0], 10 ,y);
//        g2d.drawString(headingRow2[1], 50 ,y);
//        g2d.drawString(headingRow2[2], 100 ,y);
//        g2d.drawString(headingRow2[3], 150 ,y);
//        // Line end
//
//        y += 5;
//        g2d.drawLine(x, y, PAPER_WIDTH-10, y);
//        y +=15;
//
//        // Loop below method for each estimation
//        for (Estimation estimation : estimationList.getEstimationList()) {
//            y = printEstimation(g2d, y, estimation);
//            y +=15;
//        }
//
//        g2d.drawString(getLineText("SGST Amt (1.5%)", estimationList.getSGst(), 14), 10, y);
//        y += 15;
//        g2d.drawString(getLineText("CGST Amt (1.5%)", estimationList.getCGst(), 14), 10, y);
//        y += 5;
//        g2d.drawLine(10, y, (PAPER_WIDTH/3) - 10, y);
//        y += 15;
//        g2d.drawString(getLineText("FINAL AMOUNT:", ""+estimationList.getFinalAmount(), 0), 10, y);
//        y += 10;
//        g2d.drawLine(x, y, 10, y);
//        return PAGE_EXISTS;
//    }
//
//    public static String now() {
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
//        return sdf.format(cal.getTime());
//
//    }
//
//    public void setEstimationList(EstimationList estimationList) {
//        this.estimationList = estimationList;
//    }
//
//    public void initPrint() {
//        System.setProperty("java.awt.headless", "false");
//        PrinterJob job = PrinterJob.getPrinterJob();
//        PageFormat pageFormat = job.defaultPage();
//        Paper paper = getPaper(pageFormat);
//        pageFormat.setOrientation(PageFormat.PORTRAIT);
//        pageFormat.setPaper(paper);
//        job.setPrintable(this, pageFormat);
//        if (job.printDialog()) {
//            try {
//                job.print();
//            } catch (PrinterException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    protected static double convert_CM_To_PPI(double cm) {
//        return toPPI(cm * 0.393600787);
//    }
//
//    protected static double toPPI(double inch) {
//        return inch * 72d;
//    }
//
//    private int printEstimation(Graphics2D g2d, int currentY, Estimation estimation) {
//        int y = currentY;
//        g2d.drawString(estimation.getItemName()+"  "+estimation.getItemCode(), 10, y);
//        y += 15;
//        g2d.drawString(estimation.getItemWeight()+"   "+estimation.getVaPercentage()+"%("+estimation.getVaPrice()+")    "+estimation.getTotalPriceInclVa(), 10, y);
//        y += 15;
//        g2d.drawString("Stn. Wt   "+estimation.getStoneWeight(), 10, y);
//        y += 15;
//        g2d.drawString("Net. Wt  "+estimation.getNetWeight(), 10, y);
//        y += 15;
//        g2d.drawString(estimation.getStoneName()+"   - pcs   "+estimation.getStoneWtInCts()+"Cts X "+estimation.getStonePricePerCt()+"   "+estimation.getStonePrice(), 10, y);
//        y += 5;
//        g2d.drawLine(10, y, PAPER_WIDTH-10, y);
//        y += 10;
//        g2d.drawString("Weight  "+estimation.getItemWeight()+"       Amount: "+estimation.getTotalPrice(), 10, y);
//        y += 15;
//        g2d.drawString("                          MC "+estimation.getMc(), 10, y);
//        y += 5;
//        g2d.drawLine(PAPER_WIDTH/3, y, (PAPER_WIDTH/3)-10, y);
//        y += 15;
//        g2d.drawString("                     TOTAL "+estimation.getTotalPriceInclMc(), 10, y);
//        return y;
//    }
//
//    private static Paper getPaper(PageFormat pageFormat) {
//        Paper paper = pageFormat.getPaper();
//        double middleHeight = 0;//total_item_count*1.0;  //dynamic----->change with the row count of jtable
//        double headerHeight = 5.0;                  //fixed----->but can be mod
//        double footerHeight = 5.0;                  //fixed----->but can be mod
//        double width = convert_CM_To_PPI(7.62);      //printer know only point per inch.default value is 72ppi
//        double height = convert_CM_To_PPI(headerHeight+middleHeight+footerHeight);
//        paper.setSize(width, height);
//        paper.setImageableArea(
//                convert_CM_To_PPI(0.25),
//                convert_CM_To_PPI(0.5),
//                width - convert_CM_To_PPI(0.35),
//                height );   //define boarder size    after that print area width is about 180 points
//        System.out.println("Paper width: "+paper.getWidth());
//        System.out.println("Paper Imageable width: "+paper.getImageableWidth());
//        System.out.println("paper Height: "+paper.getHeight());
//        System.out.println("paper Imageable Height: "+paper.getImageableHeight());
//        System.out.println("paper Imageable X: "+paper.getImageableX());
//        System.out.println("paper Imageable Y: "+paper.getImageableY());
//
//        return paper;
//    }
//
//    private String getLineText(String label, String value, int preSpaceCount) {
//        int maxCharsInLine = 37;
//        StringBuilder line = new StringBuilder();
//        for (int i = 0; i < preSpaceCount; i++) {
//            line.append(" ");
//        }
//        line.append(label);
//        int labelCharCount = line.length();
//        int valueCharCount = value.length();
//        int currentLineChars = labelCharCount + valueCharCount;
//        if(currentLineChars <= maxCharsInLine) {
//            int spacesAllowedAfterLabel = maxCharsInLine - (labelCharCount + valueCharCount);
//            for (int i = 0; i < spacesAllowedAfterLabel; i++) {
//                line.append(" ");
//            }
//            line.append(value);
//        } else {
//            int removeCharCountFromLabel = currentLineChars - maxCharsInLine;
//            StringBuilder modifiedLine = new StringBuilder();
//            for (int i = 0; i < line.length(); i++) {
//                if (Character.isWhitespace(line.charAt(i)) && i < removeCharCountFromLabel) {
//                    modifiedLine.append("");
//                } else {
//                    modifiedLine.append(line.charAt(i));
//                }
//            }
//            return getLineText(modifiedLine.toString(), value, 0);
//        }
//        return line.toString();
//    }
//}
