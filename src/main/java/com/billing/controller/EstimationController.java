package com.billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/estimation")
public class EstimationController {
    @GetMapping
    public String get(Model model, @RequestParam(required = false) Long id) {
        model.addAttribute("estimationId", id);
        return "views/estimation/estimation";
    }

    @GetMapping("list")
    public String estimationList() {
        return "views/estimation/estimation-list";
    }

    @GetMapping("/view")
    public String view(@RequestParam Long id, Model model) {
        model.addAttribute("estimationId", id);
        return "views/estimation/estimation-view";
    }

}
