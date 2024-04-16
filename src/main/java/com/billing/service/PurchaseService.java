package com.billing.service;

import com.billing.constant.Constants;
import com.billing.dto.Error;
import com.billing.dto.ErrorResponse;
import com.billing.dto.EstimationList;
import com.billing.dto.PurchaseDTO;
import com.billing.entity.Purchase;
import com.billing.print.EstimationPrinter2;
import com.billing.repository.PurchaseRepository;
import com.billing.repository.SupplierRepository;
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
    private final SupplierRepository supplierRepository;
    public PurchaseService(PurchaseRepository purchaseRepository, SupplierRepository supplierRepository) {
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
    }

    public Purchase save(Purchase purchase) {
        log.debug("Saving Item {}", purchase);
        return purchaseRepository.save(purchase);
    }

    public List<Purchase> getAll() {
        return purchaseRepository.findAll();
    }

    public Purchase getById(Long id) {
        return purchaseRepository.findById(id).get();
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
        //TODO:
//        if(purchase.getRate() == null ||  purchase.getRate() == BigDecimal.ZERO) {
//            errorResponse.getErrors().add(new Error("Rate not available.", "Error"));
//        }
        

        log.debug("Exiting validate purchase, Found errors: {}", errorResponse.getErrors().size());
        return errorResponse;
    }


    public void savePurchase(PurchaseDTO purchaseDTO) {
        log.debug("PurchaseService >> savePurchase >>");
        Purchase purchase = null;
        if (purchaseDTO.getId() != null && purchaseDTO.getId() > 0) {
            purchase = purchaseRepository.getById(purchaseDTO.getId());
        } else {
            purchase = new Purchase();
        }
        purchase.setPurchaseType(purchaseDTO.getPurchaseType());
        purchase.setPurchaseDate(purchaseDTO.getPurchaseDate());
        purchase.setMetalType(purchaseDTO.getMetalType());
        purchase.setSupplier(purchaseDTO.getSupplierId() != null ? supplierRepository.getById(purchaseDTO.getSupplierId()) : supplierRepository.findByName(Constants.ANONYMOUS));
        purchase.setTotalGrossWeight(purchaseDTO.getTotalGrossWeight());
        purchase.setTotalStnWeight(purchaseDTO.getTotalStnWeight());
        purchase.setActualPurity(purchaseDTO.getActualPurity());
        purchase.setPurchasePurity(purchaseDTO.getPurchasePurity());
        purchase.setTotalPcs(purchaseDTO.getTotalPcs());
        purchase.setPurchaseRate(purchaseDTO.getRate());
        purchase.setPurchaseBillNo(purchaseDTO.getPurchaseBillNo());
        purchase.setIsGstPurchase(purchaseDTO.getIsGstPurchase());
        purchase.setGstNo(purchaseDTO.getGstNo());
        purchase.setTotalNetWeight(purchaseDTO.getTotalNetWeight());
        purchase.setPurchaseAmount(purchaseDTO.getPurchaseAmount());
        purchase.setTotalCgstAmount(purchaseDTO.getTotalCgstAmount());
        purchase.setTotalSGstAmount(purchaseDTO.getTotalSgstAmount());
        purchase.setTotalPurchaseAmount(purchaseDTO.getTotalPurchaseAmount());
        purchase.setPaymentMode(purchaseDTO.getPaymentMode());
        purchase.setPaidAmount(purchaseDTO.getPaidAmount());
        purchase.setBalAmount(purchaseDTO.getBalAmount());
        purchase.setDescription(purchaseDTO.getDescription());

        purchaseRepository.save(purchase);
        log.debug("<< PurchaseService << savePurchase");
    }
}
