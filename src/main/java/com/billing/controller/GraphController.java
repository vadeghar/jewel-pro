package com.billing.controller;

import com.billing.entity.Estimation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/graph")
public class GraphController {
    @GetMapping("purchases")
    public String purchase() {
        return "views/graph/purchase-chart";
    }
    @GetMapping("sales")
    public String sale() {
        return "views/graph/sale-chart";
    }
}
