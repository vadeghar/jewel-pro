package com.billing.service;

import com.billing.entity.ItemType;
import com.billing.repository.ItemTypeRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ItemTypeService {
    private final ItemTypeRepository itemTypeRepository;
    public ItemTypeService(ItemTypeRepository itemTypeRepository) {
        this.itemTypeRepository = itemTypeRepository;
    }

    // Create operation
    public ItemType createItemType(ItemType itemType) {
        return itemTypeRepository.save(itemType);
    }

    // Read operation
    public List<ItemType> getAllItemTypes() {
        return itemTypeRepository.findAll();
    }

    public Optional<ItemType> getItemTypeById(Long id) {
        return itemTypeRepository.findById(id);
    }

    // Update operation
    public ItemType updateItemType(Long id, ItemType itemTypeDetails) {
        ItemType itemType = itemTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item Type not found with id: " + id));
        itemType.setItemType(itemTypeDetails.getItemType());
        itemType.setDescription(itemTypeDetails.getDescription());
        return itemTypeRepository.save(itemType);
    }

    // Delete operation
    public void deleteItemType(Long id) {
        ItemType itemType = itemTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item Type not found with id: " + id));
        itemTypeRepository.delete(itemType);
    }
}
