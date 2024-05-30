package com.billing.print;


import com.billing.constant.Metal;
import com.billing.dto.EstRequest;
import com.billing.dto.EstResponse;
import com.billing.dto.EstimationList;
import com.billing.entity.ItemMaster;
import com.billing.utils.BillingUtils;
import org.springframework.beans.BeanUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class POSPrinter implements Printable {
    public static final String DATE_FORMAT_NOW = "dd/MM/yyyy HH:mm";
    private EstimationList estimationList;
    private static final int INCH_IN_POINTS = 72; // 1 inch = 72 points
    private static final String RECEIPT_FONT = "Monospaced";
    private static final int PAPER_WIDTH = 3 * INCH_IN_POINTS;
    public  static String headingRow1[] = new String[] {"Item","","Code",""};
    public  static String headingRow2[] = new String[] {"Grs.Wt","VA","","Amount"};
    static JTable itemsTable;

    public void setEstimationList(EstimationList estimationList) {
        this.estimationList = estimationList;
    }

    public static void setHeadingRow1(Object[][] printitem){
        Object data[][]=printitem;
        DefaultTableModel model = new DefaultTableModel();
        //assume jtable has 4 columns.
        model.addColumn(headingRow1[0]);
        model.addColumn(headingRow1[1]);
        model.addColumn(headingRow1[2]);
        model.addColumn(headingRow1[3]);
        int rowcount=printitem.length;
        addToModel(model, data, rowcount);
        itemsTable = new JTable(model);
    }

    public static void addToModel(DefaultTableModel model, Object [][]data, int rowcount){
        int count=0;
        while (count < rowcount) {
            model.addRow(data[count]);
            count++;
        }
        if(model.getRowCount()!=rowcount)
            addToModel(model, data, rowcount);
        System.out.println("Check Passed.");
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.BLUE);
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        String text = "Estimation";

        FontMetrics metrics = g2d.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        int centerX = (int) ((pf.getImageableWidth() - textWidth) / 2);
        int centerY = (int) (pf.getImageableHeight() / 2);

        String col1Txt = "";
        String col2Txt = "";
        g2d.setFont(new Font(RECEIPT_FONT, Font.BOLD, 14));

        int x = 10;
        int y = 20;
        g2d.drawString(text, centerX, y);
        g2d.setFont(new Font(RECEIPT_FONT, Font.PLAIN, 8));
        y += 20;
        col1Txt = "Dt:"+now();
        col2Txt = "Est #: "+estimationList.getEstimationNo();
        g2d.drawString("Dt:"+now()+"       Est #:"+estimationList.getEstimationNo(), x, y);


        // New line here
        y += 12;
        x = 10;
        col1Txt = "G.Rate:  "+estimationList.getGoldRate();
        col2Txt = "S.Rate: "+estimationList.getSilverRate();
        g2d.drawString(col1Txt, x, y);
        textWidth = metrics.stringWidth(col2Txt);
        x = PAPER_WIDTH - textWidth-10;
        g2d.drawString(col2Txt, x, y);
        // Line end

        // New line here
        y += 5;
        drawLine(g2d, 10, y);
        // Line end

        // New line here
        y += 10;
        x = 10;
        g2d.drawString(headingRow1[0], 10 ,y);
        g2d.drawString(headingRow1[1], 50 ,y);
        g2d.drawString(headingRow1[2], 100 ,y);
        g2d.drawString(headingRow1[3], 150 ,y);
        // Line end

        // New line here
        y += 13;
        x = 10;
        g2d.drawString(headingRow2[0], 10 ,y);
        g2d.drawString(headingRow2[1], 50 ,y);
        g2d.drawString(headingRow2[2], 100 ,y);
        g2d.drawString(headingRow2[3], 150 ,y);

        y += 5;
        drawLine(g2d, 10, y);
        y +=15;

        // Loop below method for each estimation
        for (EstResponse estResponse : estimationList.getEstimations()) {
            y = printEstimation(g2d, y, estResponse);
            y +=15;
        }


//        g2d.drawString("              SGST Amt (1.5%) 2990.77", 10, y);
        g2d.drawString(getLineText("SGST Amt (1.5%)", estimationList.getSGst(), 14), 10, y);
        y += 15;
//        g2d.drawString("              CGST Amt (1.5%) 2990.77", 10, y);
        g2d.drawString(getLineText("CGST Amt (1.5%)", estimationList.getCGst(), 14), 10, y);
        y += 5;
        drawLine(g2d, PAPER_WIDTH/3, y);
        y += 15;
//        g2d.drawString("FINAL AMOUNT                205366.74", 10, y);
        g2d.drawString(getLineText("FINAL AMOUNT:", ""+estimationList.getFinalAmount(), 0), 10, y);
        y += 10;
        drawLine(g2d, 10, y);
        return PAGE_EXISTS;
    }

    private int printEstimation(Graphics2D g2d, int currentY, EstResponse estResponse) {
        int y = currentY;
        g2d.drawString(estResponse.getItemDesc()+"  "+estResponse.getItemCode(), 10, y);
        y += 15;
        g2d.drawString(estResponse.getWeight()+"   "+estResponse.getVaPercentage()+"%("+estResponse.getVaPrice()+")    "+estResponse.getTotalPriceInclVa(), 10, y);
        y += 15;
        g2d.drawString("Stn. Wt   "+estResponse.getStoneWeight(), 10, y);
        y += 15;
        g2d.drawString("Net. Wt  "+estResponse.getNetWeight(), 10, y);
        y += 15;
        g2d.drawString(estResponse.getStoneName()+"  "+estResponse.getStonePcs()+"pcs   "+estResponse.getStoneWtInCts()+"CtsX"+estResponse.getStonePricePerCt()+"   "+estResponse.getStonePrice(), 10, y);
        y += 5;
        drawLine(g2d, 10, y);
        y += 10;
        g2d.drawString("Weight  "+estResponse.getWeight()+"       Amount: "+estResponse.getTotalPrice(), 10, y);
        y += 15;
        g2d.drawString("                          MC "+estResponse.getMc(), 10, y);
        y += 5;
        drawLine(g2d, PAPER_WIDTH/3, y);
        y += 15;
        g2d.drawString("                     TOTAL "+estResponse.getTotalPriceInclMc(), 10, y);
        return y;
    }


    /**
     *
     * @param label append the spaces to this label to make total chars of label and value should be 37
     * @param value this value has to be append at last in the line
     * @param preSpaceCount if it's greater than 0 append the spaces prior to label
     * @return
     */
    private String getLineText(String label, String value, int preSpaceCount) {
        int maxCharsInLine = 37;
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < preSpaceCount; i++) {
            line.append(" ");
        }
        line.append(label);
        int labelCharCount = line.length();
        int valueCharCount = value.length();
        int currentLineChars = labelCharCount + valueCharCount;
        if(currentLineChars <= maxCharsInLine) {
            int spacesAllowedAfterLabel = maxCharsInLine - (labelCharCount + valueCharCount);
            for (int i = 0; i < spacesAllowedAfterLabel; i++) {
                line.append(" ");
            }
            line.append(value);
        } else {
            int removeCharCountFromLabel = currentLineChars - maxCharsInLine;
            StringBuilder modifiedLine = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                if (Character.isWhitespace(line.charAt(i)) && i < removeCharCountFromLabel) {
                    modifiedLine.append("");
                } else {
                    modifiedLine.append(line.charAt(i));
                }
            }
            return getLineText(modifiedLine.toString(), value, 0);
        }
        return line.toString();
    }

    private static Paper getPaper(PageFormat pageFormat) {
        Paper paper = pageFormat.getPaper();
        double middleHeight = 0;//total_item_count*1.0;  //dynamic----->change with the row count of jtable
        double headerHeight = 5.0;                  //fixed----->but can be mod
        double footerHeight = 5.0;                  //fixed----->but can be mod
        double width = convert_CM_To_PPI(7.62);      //printer know only point per inch.default value is 72ppi
        double height = convert_CM_To_PPI(headerHeight+middleHeight+footerHeight);
        paper.setSize(width, height);
        paper.setImageableArea(
                convert_CM_To_PPI(0.25),
                convert_CM_To_PPI(0.5),
                width - convert_CM_To_PPI(0.35),
                height );   //define boarder size    after that print area width is about 180 points
        System.out.println("Paper width: "+paper.getWidth());
        System.out.println("Paper Imageable width: "+paper.getImageableWidth());
        System.out.println("paper Height: "+paper.getHeight());
        System.out.println("paper Imageable Height: "+paper.getImageableHeight());
        System.out.println("paper Imageable X: "+paper.getImageableX());
        System.out.println("paper Imageable Y: "+paper.getImageableY());

        return paper;
    }

    public void drawLine(Graphics2D g2d, int x, int y) {
        g2d.drawLine(x, y, PAPER_WIDTH-10, y);
    }

    protected static double convert_CM_To_PPI(double cm) {
        return toPPI(cm * 0.393600787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());

    }


    public static void main(String[] args) {
        POSPrinter printer = new POSPrinter();

        printer.setEstimationList(getEstimationList());
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pageFormat = job.defaultPage();
        Paper paper = getPaper(pageFormat);
        pageFormat.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
        pageFormat.setPaper(paper);
        job.setPrintable(printer, pageFormat);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }


    private static EstResponse getEstResponse() {
        BillingUtils billingUtils = new BillingUtils();
        EstRequest estRequest = EstRequest.builder()
                .goldRate(BigDecimal.valueOf(5800))
                .silverRate(BigDecimal.valueOf(80))
                .itemMaster(ItemMaster.builder()
                        .itemName("POTA DESIGNER")
                        .itemCode("-")
                        .itemHuid("7865T8")
                        .itemQuality("916")
                        .itemMetal(Metal.GOLD)
                        .stonePcs(1)
                        .stoneName("Stones")
                        .pcs(1)
                        .weight(BigDecimal.valueOf(14.851))
                        .stoneWeight(BigDecimal.valueOf(0.256))
                        .vaPercentage(BigDecimal.valueOf(16.49))
                        .build())
                .defaultMcEnabled("YES")
                .isGstEstimation("YES")
                .discount(BigDecimal.ZERO)
                .build();
        EstResponse estResponse = billingUtils.estimationCalc2(estRequest);
        BeanUtils.copyProperties(estRequest, estResponse);
        return estResponse;
    }

    private static EstimationList getEstimationList() {
        EstResponse estResponse1 = getEstResponse();
        EstimationList estimationList = EstimationList.builder()
                .estimations(Arrays.asList(estResponse1))
                .cGst(estResponse1.getCGstPrice().toString())
                .sGst(estResponse1.getSGstPrice().toString())
                .finalAmount(estResponse1.getGrandTotalAfterDiscount().toString())
                .build();
        return estimationList;
    }
}

