package com.billing.controller;

import com.billing.dto.ErrorResponse;
import com.billing.entity.ItemMaster;
import com.billing.service.ItemMasterService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemMasterController {
    private final ItemMasterService itemMasterService;
    public ItemMasterController(ItemMasterService itemMasterService) {
        this.itemMasterService = itemMasterService;
    }

    @GetMapping
    public String get(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("itemMaster") ItemMaster itemMaster,
                      BindingResult result,
                      Model model) {
        if (httpHeaders.containsKey("X-Application-Name")) {
            System.out.println("Found X-Application-Name in header");
        }
        List<ItemMaster> itemMasterList = itemMasterService.getAll();
        //model.addAttribute("errorResponse", new ErrorResponse(new ArrayList<>(), LocalDateTime.now()));
        model.addAttribute("itemMasterList", itemMasterList);
        model.addAttribute("itemMaster", new ItemMaster());
        return "item";
    }

    @PostMapping("/save")
    public String registration(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("itemMaster") ItemMaster itemMaster,
                               BindingResult result,
                               Model model) {
        if (httpHeaders.containsKey("X-Application-Name")) {
            System.out.println("Found X-Application-Name in header");
        }
        ErrorResponse errorResponse = itemMasterService.validateItemMaster(itemMaster);
        model.addAttribute("itemMasterList", itemMasterService.getAll());
        if(errorResponse.hasErrors()) {
            model.addAttribute("itemMaster", itemMaster);
            model.addAttribute("errorResponse", errorResponse);
            return "item";
        }
        model.addAttribute("itemMaster", itemMasterService.save(itemMaster));
        return "redirect:/item?success";
    }
}
