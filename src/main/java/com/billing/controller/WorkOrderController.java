package com.billing.controller;

import com.billing.entity.WorkOrder;
import com.billing.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/workorder")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @GetMapping
    public String showWorkOrderList(Model model) {
        List<WorkOrder> workOrders = workOrderService.getAllWorkOrders();
        model.addAttribute("workOrderList", workOrders);
        model.addAttribute("workOrder", new WorkOrder());
        return "views/worker/workOrder";
    }

    @PostMapping("/save")
    public String addWorkOrder(@ModelAttribute WorkOrder workOrder) {
        workOrderService.createWorkOrder(workOrder);
        return "redirect:/workorder";
    }


    @PostMapping("/edit/{id}")
    public String editWorkOrder(@PathVariable Long id, @ModelAttribute WorkOrder workOrder) {
        workOrderService.updateWorkOrder(id, workOrder);
        return "redirect:/workorder";
    }

    @GetMapping("/delete/{id}")
    public String deleteWorkOrder(@PathVariable Long id) {
        workOrderService.deleteWorkOrder(id);
        return "redirect:/workorder";
    }
}

