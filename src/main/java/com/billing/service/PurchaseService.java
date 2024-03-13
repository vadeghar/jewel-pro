package com.billing.service;

import com.billing.dto.Error;
import com.billing.dto.ErrorResponse;
import com.billing.dto.EstimationList;
import com.billing.entity.Purchase;
import com.billing.print.EstimationPrinter2;
import com.billing.repository.PurchaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.billing.constant.ErrorCode.DATA_ERROR_ESTIMATION;

@Service
@Slf4j
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase save(Purchase purchase) {
        log.debug("Saving Item {}", purchase);
        return purchaseRepository.save(purchase);
    }

    public List<Purchase> getAll() {
        return purchaseRepository.findAll();
    }

    public Purchase getById(Long id) {
        return purchaseRepository.getById(id);
    }

    public Purchase update(Long id, Purchase purchase) {
        log.debug("Saving Item id {} with data {}", id, purchase);
        Purchase estimationDb = purchaseRepository.getReferenceById(id);
        if (estimationDb != null) {
            BeanUtils.copyProperties(purchase, estimationDb);
            log.debug("Saving to db item {}", estimationDb);
            return purchaseRepository.save(estimationDb);
        } else {
            new EntityNotFoundException("Purchase not found with id "+id);
        }
        return null;
    }

    public void delete(Long id) {
        log.debug("Deleting item id {}", id);
        purchaseRepository.deleteById(id);
    }

    public ErrorResponse validatePurchase(Purchase purchase) {
        log.debug("Validating purchase {}", purchase);
        List<Error> errors = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse(errors, LocalDateTime.now(), DATA_ERROR_ESTIMATION.getCode());
        if(purchase.getRate() == null ||  purchase.getRate() == BigDecimal.ZERO) {
            errorResponse.getErrors().add(new Error("Rate not available.", "Error"));
        }
        

        log.debug("Exiting validate purchase, Found errors: {}", errorResponse.getErrors().size());
        return errorResponse;
    }


}
