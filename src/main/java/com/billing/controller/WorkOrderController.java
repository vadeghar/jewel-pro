package com.billing.controller;

import com.billing.entity.WorkOrder;
import com.billing.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

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
        return "workOrder";
    }

//    @GetMapping("/add")
//    public String showAddWorkOrderForm(Model model) {
//        model.addAttribute("workOrder", new WorkOrder());
//        return "workorder";
//    }

    @PostMapping("/save")
    public String addWorkOrder(@ModelAttribute WorkOrder workOrder) {
        workOrderService.createWorkOrder(workOrder);
        return "redirect:/workorder";
    }

//    @GetMapping("/edit/{id}")
//    public String showEditWorkOrderForm(@PathVariable Long id, Model model) {
//        Optional<WorkOrder> workOrder = workOrderService.getWorkOrderById(id);
//        workOrder.ifPresent(value -> model.addAttribute("workOrder", value));
//        return workOrder.isPresent() ? "workorder" : "workOrderNotFound";
//    }

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

