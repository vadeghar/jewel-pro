package com.billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("reports")
public class ReportsController {

    //payment_mode
    //supplier_id
    //metal_type
    // purchase_type
    //purchase_date between two dates
    //purchase_amount between a range
    // bal_amount between a range



    // OUTPUT Table:
    //purchase_date, metal_type, supplier_id (name), total_net_weight, purchase_rate, is_gst_purchase, total_gst(cgst+sgst), gst_no, total_purchase_amount, bal_amount
    @GetMapping("purchase")
    public String purchase() {
        return "views/reports/purchase";
    }

    @GetMapping("purchase-gst")
    public String purchaseGst() {
        return "views/reports/purchase-gst";
    }

    @GetMapping("sale")
    public String sale() {
        return "views/reports/sale";
    }

    @GetMapping("sale-gst")
    public String saleGst() {
        return "views/reports/sale-gst";
    }
}
