package com.billing.controller;

import com.billing.entity.ItemType;
import com.billing.service.ItemTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/itemtype")
public class ItemTypeController {

    private final ItemTypeService itemTypeService;

    public ItemTypeController(ItemTypeService itemTypeService) {
        this.itemTypeService = itemTypeService;
    }

    @GetMapping
    public String getAllItemTypes(Model model) {
        return "views/item-type/item-type-list";
    }

    @GetMapping("/{id}")
    public String getItemTypeById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("itemTypeId", id);
        return "views/item-type/item-type";
    }

    @GetMapping("/new")
    public String newItemTypeById(Model model) {
        model.addAttribute("itemTypeId", null);
        return "views/item-type/item-type";
    }

    @PostMapping("/edit")
    public String newItemTypeById(@ModelAttribute ItemType itemType, Model model) {
        model.addAttribute("itemTypeId", itemType.getId());
        return "views/item-type/item-type";
    }


    // Add methods for create, update, and delete operations
}

