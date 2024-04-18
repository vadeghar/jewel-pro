package com.billing.controller.rest;

import com.billing.entity.ItemType;
import com.billing.service.ItemTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/itemtype")
public class ItemTypeRestController {

    private final ItemTypeService itemTypeService;

    public ItemTypeRestController(ItemTypeService itemTypeService) {
        this.itemTypeService = itemTypeService;
    }

    @GetMapping
    public List<ItemType> getAllItemTypes() {
        return itemTypeService.getAllItemTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemType> getItemTypeById(@PathVariable("id") Long id) {
        ItemType itemType = itemTypeService.getItemTypeById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item Type not found with id: " + id));
        return ResponseEntity.ok().body(itemType);
    }

    @PostMapping
    public ItemType createItemType(@RequestBody ItemType itemType) {
        return itemTypeService.createItemType(itemType);
    }

    @PutMapping("/{id}")
    public ItemType updateItemType(@PathVariable("id") Long id, @RequestBody ItemType itemTypeDetails) {
        return itemTypeService.updateItemType(id, itemTypeDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItemType(@PathVariable("id") Long id) {
        itemTypeService.deleteItemType(id);
        return ResponseEntity.ok().build();
    }
}

