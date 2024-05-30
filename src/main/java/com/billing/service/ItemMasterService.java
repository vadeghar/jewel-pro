package com.billing.service;

import com.billing.dto.Error;
import com.billing.dto.ErrorResponse;
import com.billing.entity.ItemMaster;
import com.billing.repository.ItemMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.billing.constant.ErrorCode.DATA_ERROR_ITEM;

@Service
@Slf4j
public class ItemMasterService {
    private final ItemMasterRepository itemMasterRepository;
    public ItemMasterService(ItemMasterRepository itemMasterRepository) {
        this.itemMasterRepository = itemMasterRepository;
    }

    public ItemMaster save(ItemMaster itemMaster) {
        log.debug("Saving Item {}", itemMaster);
        return itemMasterRepository.save(itemMaster);
    }

    public List<ItemMaster> getAll() {
        return itemMasterRepository.findAll();
    }

    public ItemMaster update(Long id, ItemMaster itemMaster) {
        log.debug("Saving Item id {} with data {}", id, itemMaster);
        ItemMaster itemMasterDb = itemMasterRepository.getReferenceById(id);
        if (itemMasterDb != null) {
            BeanUtils.copyProperties(itemMaster, itemMasterDb);
            log.debug("Saving to db item {}", itemMasterDb);
            return itemMasterRepository.save(itemMasterDb);
        } else {
            new EntityNotFoundException("ItemMaster not found with id "+id);
        }
        return null;
    }

    public void delete(Long id) {
        log.debug("Deleting item id {}", id);
        itemMasterRepository.deleteById(id);
    }

    public ErrorResponse validateItemMaster(ItemMaster itemMaster) {
        log.debug("Validating itemMaster {}", itemMaster);
        List<Error> errors = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse(errors, LocalDateTime.now(), DATA_ERROR_ITEM.getCode());
        if (itemMaster.getItemMetal() == null) {
            errorResponse.getErrors().add(new Error("Select item metal.", "Error"));
        }
        if(StringUtils.isBlank(itemMaster.getItemName())) {
            errorResponse.getErrors().add(new Error("Item name is required.", "Error"));
        }
        if (StringUtils.isBlank(itemMaster.getItemCode())) {
            errorResponse.getErrors().add(new Error("Enter item code.", "Error"));
        }
        if (itemMaster.getPcs() <= 0) {
            errorResponse.getErrors().add(new Error("Enter No. of pieces greater than zero.", "Error"));
        }
        if (itemMaster.getWeight() == null || itemMaster.getWeight() == BigDecimal.ZERO) {
            errorResponse.getErrors().add(new Error("Enter item weight.", "Error"));
        }
        if (itemMaster.getVaPercentage() == null && itemMaster.getWastageInGms() == null) {
            errorResponse.getErrors().add(new Error("Enter VA or wastage of item.", "Error"));
        }
        if(StringUtils.isNotBlank(itemMaster.getStoneName())) {
            if (itemMaster.getStoneWeight() == null || itemMaster.getStoneWeight() == BigDecimal.ZERO) {
                errorResponse.getErrors().add(new Error("Enter stone weight in grams.", "Error"));
            }
            if (itemMaster.getStoneWeight() == null || itemMaster.getStoneWeight() == BigDecimal.ZERO) {
                errorResponse.getErrors().add(new Error("Enter VA or wastage of item.", "Error"));
            }
            if (itemMaster.getStonePcs() <= 0) {
                errorResponse.getErrors().add(new Error("Enter no. of stones.", "Error"));
            }
        }
        if (StringUtils.isBlank(itemMaster.getItemQuality())) {
            errorResponse.getErrors().add(new Error("Item quality is required.", "Error"));
        }
        log.debug("Exiting validate itemMaster, Found errors: {}", errorResponse.getErrors().size());
        return errorResponse;
    }
}
