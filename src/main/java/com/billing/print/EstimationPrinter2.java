//package com.billing.print;
//
//import com.billing.dto.EstimationList;
//import com.billing.entity.Estimation;
//
//import java.awt.*;
//import java.awt.print.*;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//public class EstimationPrinter2 implements Printable {
//
//    public static final String DATE_FORMAT_NOW = "dd/MM/yyyy HH:mm";
//    private EstimationList estimationList;
//    private static final int INCH_IN_POINTS = 72; // 1 inch = 72 points
//    private static final String RECEIPT_FONT = "Monospaced";
//    private static final int PAPER_WIDTH = 3 * INCH_IN_POINTS;
//    public  static String headingRow1[] = new String[] {"Item","","Code","Wts/Amts"};
//    public void setEstimationList(EstimationList estimationList) {
//        this.estimationList = estimationList;
//    }
//
//    @Override
//    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
//        if (pageIndex > 0) {
//            return NO_SUCH_PAGE;
//        }
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setBackground(Color.BLUE);
//        g2d.translate(pf.getImageableX(), pf.getImageableY());
//
//        String text = "Estimation#:"+estimationList.getEstimationList().get(0).getEstimationNo();
//
//        FontMetrics metrics = g2d.getFontMetrics();
//        int textWidth = metrics.stringWidth(text);
//        int centerX = (int) ((pf.getImageableWidth() - textWidth) / 2);
//        int centerY = (int) (pf.getImageableHeight() / 2);
//
//        String col1Txt = "";
//        String col2Txt = "";
//        g2d.setFont(new Font(RECEIPT_FONT, Font.PLAIN, 10));
//
//        int x = 10;
//        int y = 20;
//        g2d.drawString(text, centerX, y);
//        g2d.setFont(new Font(RECEIPT_FONT, Font.PLAIN, 8));
//        y += 20;
//        g2d.drawString("Dt:"+now(), x, y);
//        // New line here
//        y += 12;
////        g2d.drawString(estimationList.getEstimationList().get(0).getItemMetal()+" item", x, y);
////        String txt_1 = "Rate: "+estimationList.getEstimationList().get(0).getRate().toPlainString();
////        int x1 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_1);
////        g2d.drawString(txt_1, x1, y);
//
//        // New line here
//        y += 5;
//        drawLine(g2d, 10, y);
//        // Line end
//
//        // New line here
//        y += 10;
//        x = 10;
//        g2d.drawString(headingRow1[0], 10 ,y);
//        g2d.drawString(headingRow1[1], 50 ,y);
//        g2d.drawString(headingRow1[2], 100 ,y);
//        g2d.drawString(headingRow1[3], 150 ,y);
//        // Line end
//
//        y += 5;
//        drawLine(g2d, 10, y);
//        y +=5;
//        BigDecimal totalCgst = BigDecimal.ZERO;
//        BigDecimal totalSgst = BigDecimal.ZERO;
//        BigDecimal total = BigDecimal.ZERO;
//        // Loop below method for each estimation
//        for (Estimation estimation : estimationList.getEstimationList()) {
//            y = printEstimation(g2d, y, estimation, pf);
//            y +=15;
//        }
//        return PAGE_EXISTS;
//    }
//
//    private int printEstimation(Graphics2D g2d, int currentY, Estimation estimation, PageFormat pf) {
//        FontMetrics metrics = g2d.getFontMetrics();
//        int y = currentY;
//        g2d.drawString(estimation.getItemName()+"  "+estimation.getItemCode(), 10, y);
//        String txt_1 = "Pcs: "+estimation.getPcs();
//        int x1 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_1);
//        g2d.drawString(txt_1, x1, y);
//        y += 10;
//        String txt_2 = "Weight: "+estimation.getItemWeight().setScale(3, RoundingMode.HALF_UP);
//        int x2 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_2);
//        g2d.drawString(txt_2, x2, y);
//
//
//        if(estimation.getStoneWeight() != null) {
//            y += 10;
//            String txt_4 = "Stone Wt.: "+estimation.getStoneWeight().setScale(3, RoundingMode.HALF_UP);
//            int x4 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_4);
//            g2d.drawString(txt_4, x4, y);
//            y+=5;
//            drawLine(g2d, 10, y);
//            y += 10;
//            String txt_5 = "Net Wt.: "+estimation.getNetWeight().setScale(3, RoundingMode.HALF_UP);
//            int x5 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_5);
//            g2d.drawString(txt_5, x5, y);
//        }
//
//        y += 10;
//        String txt_3 = "VA (Tarugu): "+estimation.getVaWeight().setScale(3, RoundingMode.HALF_UP);
//        int x3 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_3);
//        g2d.drawString(txt_3, x3, y);
//
//        y+=5;
//        drawLine(g2d, PAPER_WIDTH/3, y);
//
//        y += 10;
//        String txt_6 = "Total Wt.: "+estimation.getWeightInclVaWt().setScale(3, RoundingMode.HALF_UP);
//        int x6 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_6);
//        g2d.drawString(txt_6, x6, y);
//
//        y += 10;
//        String txt_7 = "Price.: "+estimation.getTotalPriceInclVa().setScale(2, RoundingMode.HALF_UP);
//        int x7 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_7);
//        g2d.drawString(txt_7, x7, y);
//        if(estimation.getStoneWeight() != null) {
//            y += 10;
//            String txt_8 = "Stn Price.: " + estimation.getStoneWtInCts().setScale(2, RoundingMode.HALF_UP) + "Cts X "
//                    + estimation.getStonePricePerCt().setScale(0, RoundingMode.HALF_UP).toString();
//            g2d.drawString(txt_8, 10, y);
//
//            String txt_9 = estimation.getStonePrice().setScale(2, RoundingMode.HALF_UP).toString();
//            int x9 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_9);
//            g2d.drawString(txt_9, x9, y);
//        }
//
//        y += 10;
//        String txt_10 = "MC : "+estimation.getMc().setScale(2, RoundingMode.HALF_UP);
//        int x10 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_10);
//        g2d.drawString(txt_10, x10, y);
//
//        y+=5;
//        drawLine(g2d, PAPER_WIDTH/3, y);
//
//        y += 10;
//        String txt_11 = "Total : "+estimation.getTotalPriceInclMc().setScale(2, RoundingMode.HALF_UP);
//        int x11 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_11);
//        g2d.drawString(txt_11, x11, y);
//
//        if("YES".equalsIgnoreCase(estimation.getIsGstEstimation())) {
//            y += 10;
//            String txt_12 = "CGST(1.5%) : "+estimation.getCGstPrice();
//            int x12 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_12);
//            g2d.drawString(txt_12, x12, y);
//
//            y += 10;
//            String txt_13 = "SGST(1.5%) : "+estimation.getSGstPrice();
//            int x13 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_13);
//            g2d.drawString(txt_13, x13, y);
//
//            y+=5;
//            drawLine(g2d, PAPER_WIDTH/3, y);
//            y += 10;
//            String txt_14 = "Total : "+estimation.getTotalPrice().setScale(2, RoundingMode.HALF_UP).toString();
//            int x14 = (int) pf.getImageableWidth() - metrics.stringWidth(txt_14);
//            g2d.drawString(txt_14, x14, y);
//        }
//
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
//    public void drawLine(Graphics2D g2d, int x, int y) {
//        g2d.drawLine(x, y, PAPER_WIDTH-10, y);
//    }
//
//    protected static double convert_CM_To_PPI(double cm) {
//        return toPPI(cm * 0.393600787);
//    }
//
//    protected static double toPPI(double inch) {
//        return inch * 72d;
//    }
//    public static String now() {
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
//        return sdf.format(cal.getTime());
//
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
//}
