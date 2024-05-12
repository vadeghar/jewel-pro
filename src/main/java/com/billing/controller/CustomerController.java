package com.billing.controller;

import com.billing.dto.ErrorResponse;
import com.billing.entity.Customer;
import com.billing.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String showCustomer(@RequestParam(value = "id", required = false) Long id, Model model) {
        return "views/customer/customer";
    }

    @GetMapping("customer-list")
    public String showCustomerList(Model model) {
        return "views/customer/customer-list";
    }

    @GetMapping("customer-purchase")
    public String customerPurchase(Model model) {
        return "views/customer/customer-purchase";
    }

    @GetMapping("/{id}")
    public String getById(Model model) {
        return "views/customer/customer";
    }

    @PostMapping("/save")
    public String addCustomer(@ModelAttribute Customer customer, Model model) {
        ErrorResponse errorResponse = customerService.validateCustomer(customer);
        model.addAttribute("customerList", customerService.getAllCustomers());
        if(errorResponse.hasErrors()) {
            model.addAttribute("customer", customer);
            model.addAttribute("errorResponse", errorResponse);
            return "customer";
        }

        customerService.createCustomer(customer);
        return "redirect:/customers";
    }

//    @GetMapping("/edit/{id}")
//    public String showEditCustomerForm(@PathVariable Long id, Model model) {
//        Optional<Customer> customer = customerService.getCustomerById(id);
//        customer.ifPresent(value -> model.addAttribute("customer", value));
//        model.addAttribute("customerList", customerService.getAllCustomers());
//        return "customer";
//    }

    @PostMapping("/edit/{id}")
    public String editCustomer(@PathVariable Long id, @ModelAttribute Customer customer) {
        customerService.updateCustomer(id, customer);
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}

