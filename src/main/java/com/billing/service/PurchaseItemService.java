package com.billing.service;

import com.billing.dto.Error;
import com.billing.dto.ErrorResponse;
import com.billing.entity.PurchaseItem;
import com.billing.repository.PurchaseItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.billing.constant.ErrorCode.DATA_ERROR_PURCHASE_ITEM;

@Service
@Slf4j
public class PurchaseItemService {
    private final PurchaseItemRepository purchaseItemRepository;
    public PurchaseItemService(PurchaseItemRepository purchaseItemRepository) {
        this.purchaseItemRepository = purchaseItemRepository;
    }

    public PurchaseItem save(PurchaseItem purchaseItem) {
        log.debug("Saving Purchase Item {}", purchaseItem);
        return purchaseItemRepository.save(purchaseItem);
    }

    public List<PurchaseItem> getAll() {
        return purchaseItemRepository.findAll();
    }

    public PurchaseItem getById(Long id) {
        return purchaseItemRepository.getById(id);
    }

    public PurchaseItem update(Long id, PurchaseItem purchaseItem) {
        log.debug("Saving Purchase Item id {} with data {}", id, purchaseItem);
        PurchaseItem estimationDb = purchaseItemRepository.getReferenceById(id);
        if (estimationDb != null) {
            BeanUtils.copyProperties(purchaseItem, estimationDb);
            log.debug("Saving to db item {}", estimationDb);
            return purchaseItemRepository.save(estimationDb);
        } else {
            new EntityNotFoundException("PurchaseItem not found with id "+id);
        }
        return null;
    }

    public void delete(Long id) {
        log.debug("Deleting item id {}", id);
        purchaseItemRepository.deleteById(id);
    }

    public ErrorResponse validatePurchaseItem(PurchaseItem purchaseItem) {
        log.debug("Validating purchaseItem {}", purchaseItem);
        List<Error> errors = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse(errors, LocalDateTime.now(), DATA_ERROR_PURCHASE_ITEM.getCode());

        

        log.debug("Exiting validate purchaseItem, Found errors: {}", errorResponse.getErrors().size());
        return errorResponse;
    }

    public List<PurchaseItem> findPurchaseItemByCodeOrName(String nameOrCode) {
        return null; //purchaseItemRepository.findByNameOrCode(nameOrCode);
    }


}
