package com.billing.service;

import com.billing.constant.Constants;
import com.billing.dto.*;
import com.billing.dto.Error;
import com.billing.entity.Purchase;
import com.billing.entity.PurchaseItem;
import com.billing.entity.Stock;
import com.billing.print.EstimationPrinter2;
import com.billing.repository.PurchaseItemRepository;
import com.billing.repository.PurchaseRepository;
import com.billing.repository.StockRepository;
import com.billing.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.billing.constant.ErrorCode.DATA_ERROR_ESTIMATION;

@Service
@Slf4j
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final SupplierRepository supplierRepository;
    private final StockRepository stockRepository;
    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseItemRepository purchaseItemRepository, SupplierRepository supplierRepository, StockRepository stockRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.supplierRepository = supplierRepository;
        this.stockRepository = stockRepository;
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


    public Long savePurchase(PurchaseDTO purchaseDTO) {
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

        Purchase dbPurchase = purchaseRepository.save(purchase);
        log.debug("<< PurchaseService << savePurchase");
        return dbPurchase.getId();
    }

    public List<PurchaseDTO> getAllPurchases() {
        List<Purchase> purchaseList = purchaseRepository.findAllByActivePurchase("YES");
        List<PurchaseDTO> purchaseDTOList = purchaseList.stream().map(p -> toDto(p)).collect(Collectors.toList());
        return purchaseDTOList;
    }

    public PurchaseDTO getByPurchaseId(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));
        PurchaseDTO purchaseDTO = toDto(purchase);
        if (!CollectionUtils.isEmpty(purchase.getPurchaseItems())) {
            List<PurchaseItemDTO> purchaseItemDTOS = purchase.getPurchaseItems().stream()
                    .map(pi -> toItemDto(pi))
                    .collect(Collectors.toList());
            purchaseDTO.setPurchaseItems(purchaseItemDTOS);
        }
        return purchaseDTO;
    }


    public PurchaseDTO savePurchaseItems(Long purchaseId, List<PurchaseItemDTO> purchaseItems) {
//        if(purchaseId == null || purchaseId == 0) {
//            throw new EntityNotFoundException("Invalid purchaseId.");
//        }
//        Purchase purchase = purchaseRepository.getReferenceById(purchaseId);
//
//        purchase.getPurchaseItems().clear();
//        for (PurchaseItemDTO purchaseItemDTO : purchaseItems) {
//            PurchaseItem purchaseItem = purchaseItemDTO.toEntity();
//            if (purchaseItem.getId() != null) {
//                PurchaseItem existingPurchaseItem = purchaseItemRepository.findById(purchaseItem.getId())
//                        .orElseThrow(() -> new EntityNotFoundException("PurchaseItem not found"));
//                existingPurchaseItem.fromDto(purchaseItemDTO); // Update PurchaseItem fields
//                purchaseItem = existingPurchaseItem;
//            }
//            purchaseItem.setPurchase(purchase);
//
//            Stock stock = purchaseItem.getStock();
//            if (stock != null && stock.getId() != null) {
//                Stock existingStock = stockRepository.findById(stock.getId())
//                        .orElseThrow(() -> new EntityNotFoundException("Stock not found"));
//                existingStock.fromDto(purchaseItemDTO);
//                purchaseItem.setStock(existingStock);
//
//            }
//            purchase.getPurchaseItems().add(purchaseItem);
//        }
//
//        Purchase savedPurchase = purchaseRepository.save(purchase);
//        return savedPurchase.toDTO();


        if(purchaseId == null || purchaseId == 0) {
            throw new EntityNotFoundException("Invalid purchaseId.");
        }
        Purchase purchase = purchaseRepository.getReferenceById(purchaseId);
        List<PurchaseItem> purhcaseItemList = purchaseItems.stream()
                .map(pi -> toEntity(pi))
                .collect(Collectors.toList());
        purchase.setPurchaseItems(purhcaseItemList);

        purchase.getPurchaseItems().stream()
                        .forEach(purchaseItem -> {
                            purchaseItem.setPurchase(purchase);
                            purchaseItem.getStock().setPurchaseItem(purchaseItem);
                        });

        Purchase dbPurchase = purchaseRepository.save(purchase);
        return toDto(dbPurchase);
    }


    public void softDelete(Long id) {
        Purchase purchase = purchaseRepository.getReferenceById(id);
        purchase.setActivePurchase("NO");
        purchaseRepository.save(purchase);
    }

    private PurchaseItem toEntity(PurchaseItemDTO pi) {
        PurchaseItem purchaseItem = pi.toEntity();
        if (pi.getStockId() != null && pi.getStockId() > 0) {
            purchaseItem.setStock(stockRepository.getReferenceById(pi.getStockId()));
        }
        return purchaseItem;
    }

    private PurchaseDTO toDto(Purchase entity) {
        PurchaseDTO dto = PurchaseDTO.builder().build();
        dto.toDto(entity);
//        entity.toDTO();
        return dto;
    }
    private PurchaseItemDTO toItemDto(PurchaseItem pi) {
        PurchaseItemDTO purchaseItemDTO = PurchaseItemDTO.builder().build();
        purchaseItemDTO.toDto(pi);
        return purchaseItemDTO;
    }


}
