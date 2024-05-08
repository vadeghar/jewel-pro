package com.billing.service;

import com.billing.constant.Constants;
import com.billing.dto.*;
import com.billing.dto.Error;
import com.billing.entity.Purchase;
import com.billing.entity.PurchaseItem;
import com.billing.entity.Stock;
import com.billing.repository.PurchaseItemRepository;
import com.billing.repository.PurchaseRepository;
import com.billing.repository.StockRepository;
import com.billing.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        log.debug("PurchaseService >> save {} >>", purchase);
        return purchaseRepository.save(purchase);
    }

    public List<Purchase> getAll() {
        log.debug("PurchaseService >> getAll >>");
        return purchaseRepository.findAll();
    }

    public Purchase getById(Long id) {
        log.debug("PurchaseService >> getById >>");
        return purchaseRepository.findById(id).get();
    }

    public Purchase update(Long id, Purchase purchase) {
        log.debug("PurchaseService >> update >> purchase: {} >> {} >>", id, purchase);
        Purchase estimationDb = purchaseRepository.getReferenceById(id);
        if (estimationDb != null) {
            BeanUtils.copyProperties(purchase, estimationDb);
            log.debug("Saving to db item {}", estimationDb);
            log.debug("PurchaseService << update << purchase <<");
            return purchaseRepository.save(estimationDb);
        } else {
            new EntityNotFoundException("Purchase not found with id "+id);
        }
        return null;
    }

    public void delete(Long id) {
        log.debug("PurchaseService >> delete >> id: {} >>", id);
        purchaseRepository.deleteById(id);
    }

    public ErrorResponse validatePurchase(Purchase purchase) {
        log.debug("PurchaseService >> validatePurchase >> purchase: {} >>", purchase);
        List<Error> errors = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse(errors, LocalDateTime.now(), DATA_ERROR_ESTIMATION.getCode());
        //TODO:
//        if(purchase.getRate() == null ||  purchase.getRate() == BigDecimal.ZERO) {
//            errorResponse.getErrors().add(new Error("Rate not available.", "Error"));
//        }
        log.debug("PurchaseService << validatePurchase << purchase << errors: {} <<", errorResponse.getErrors().size());
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
        log.debug("<< PurchaseService << savePurchase <<");
        return dbPurchase.getId();
    }

    public List<PurchaseDTO> getAllPurchases() {
        log.debug("PurchaseService >> getAllPurchases");
        List<Purchase> purchaseList = purchaseRepository.findAllByActivePurchase("YES");
        List<PurchaseDTO> purchaseDTOList = purchaseList.stream().map(p -> toDto(p)).collect(Collectors.toList());
        log.debug("PurchaseService << getAllPurchases <<");
        return purchaseDTOList;
    }

    public PurchaseDTO getByPurchaseId(Long id) {
        log.debug("PurchaseService >> getByPurchaseId >> id: {}", id);
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));
        PurchaseDTO purchaseDTO = toDto(purchase);
        if (!CollectionUtils.isEmpty(purchase.getPurchaseItems())) {
            List<PurchaseItemDTO> purchaseItemDTOS = purchase.getPurchaseItems().stream()
                    .map(pi -> toItemDto(pi))
                    .collect(Collectors.toList());
            purchaseDTO.setPurchaseItems(purchaseItemDTOS);
        }
        log.debug("PurchaseService << getByPurchaseId <<");
        return purchaseDTO;
    }


    public PurchaseDTO savePurchaseItems(Long purchaseId, List<PurchaseItemDTO> purchaseItems) {
        log.debug("PurchaseService >> savePurchaseItems >> purchaseId: {}, item count: {}", purchaseId, purchaseItems.size());
        if(purchaseId == null || purchaseId == 0) {
            throw new EntityNotFoundException("Invalid purchaseId.");
        }
        Purchase purchase = purchaseRepository.getReferenceById(purchaseId);
        List<PurchaseItem> existingPurchaseItems = purchase.getPurchaseItems();

        List<PurchaseItem> missingItems = existingPurchaseItems.stream()
                .filter(existingItem -> purchaseItems.stream()
                        .noneMatch(purchaseItem -> purchaseItem.getId() == existingItem.getId()))
                .collect(Collectors.toList());
        List<PurchaseItem> dbPurchaseItemList = new ArrayList<>();
        purchaseItems.stream().forEach(piDto -> {
            if (piDto.getId() == null) {
                PurchaseItem purchaseItem = new PurchaseItem();
                purchaseItem.setPurchase(purchase);
                Stock stock = new Stock();
                stock = stock.fromDto(piDto);
                stock.setPurchaseItem(purchaseItem);
                purchaseItem.setStock(stock);
                dbPurchaseItemList.add(purchaseItem);
            } else {
                PurchaseItem purchaseItem = existingPurchaseItems.stream()
                        .filter(pi -> pi.getId().equals(piDto.getId()))
                        .findFirst()
                        .orElse(new PurchaseItem());
                purchaseItem.setPurchase(purchase);
                Stock stock = purchaseItem.getStock();
                stock = stock.fromDto(piDto);
                stock.setPurchaseItem(purchaseItem);
                purchaseItem.setStock(stock);
                dbPurchaseItemList.add(purchaseItem);
            }
        });
        if (!CollectionUtils.isEmpty(missingItems)) {
            missingItems.stream().forEach(purchaseItem -> {
                purchase.getPurchaseItems().remove(purchaseItem);
            });
            purchaseItemRepository.deleteAll(missingItems);
        }
        purchase.setPurchaseItems(dbPurchaseItemList);
        Purchase savedPurchase = purchaseRepository.save(purchase);
        log.debug("PurchaseService << savePurchaseItems <<");
        return toDto(savedPurchase);

    }


    public void softDelete(Long id) {
        log.debug("PurchaseService >> softDelete >> id: {}", id);
        Purchase purchase = purchaseRepository.getReferenceById(id);
        purchase.setActivePurchase("NO");
        purchaseRepository.save(purchase);
        log.debug("PurchaseService << softDelete <<");
    }

    private PurchaseItem toEntity(PurchaseItemDTO pi, Purchase purchase) {
        log.debug("PurchaseService >> toEntity >>");
        PurchaseItem purchaseItem = purchaseItemRepository.findById(pi.getId())
                .orElseThrow(() -> new EntityNotFoundException("PurhcaseItem entity not found with id: "+pi.getId()));
        purchaseItem.setPurchase(purchase);
        if (pi.getStockId() != null && pi.getStockId() > 0) {
            Stock stock = stockRepository.getReferenceById(pi.getStockId());
            stock.fromDto(pi);
            purchaseItem.setStock(stock);
        }
        log.debug("PurchaseService << toEntity <<");
        return purchaseItem;
    }

    private PurchaseDTO toDto(Purchase entity) {
        log.debug("PurchaseService >> toDto >>");
        PurchaseDTO dto = PurchaseDTO.builder().build();
        dto.toDto(entity);
        log.debug("PurchaseService << toDto <<");
        return dto;
    }
    private PurchaseItemDTO toItemDto(PurchaseItem pi) {
        log.debug("PurchaseService >> toItemDto >>");
        PurchaseItemDTO purchaseItemDTO = PurchaseItemDTO.builder().build();
        purchaseItemDTO.toDto(pi);
        log.debug("PurchaseService << toItemDto <<");
        return purchaseItemDTO;
    }
}
