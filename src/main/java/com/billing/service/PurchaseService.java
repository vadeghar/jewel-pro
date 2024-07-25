package com.billing.service;

import com.billing.constant.Constants;
import com.billing.dto.Error;
import com.billing.dto.ErrorResponse;
import com.billing.dto.PurchaseDTO;
import com.billing.dto.PurchaseItemDTO;
import com.billing.entity.Payment;
import com.billing.entity.Purchase;
import com.billing.entity.PurchaseItem;
import com.billing.entity.Stock;
import com.billing.enums.StockStatus;
import com.billing.model.ChartData;
import com.billing.model.ReportFilters;
import com.billing.model.WeeklyReport;
import com.billing.repository.*;
import com.billing.utils.BillingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.billing.constant.ErrorCode.DATA_ERROR_ESTIMATION;
import static com.billing.enums.ReportTypeEnum.*;

@Service
@Slf4j
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final SupplierRepository supplierRepository;
    private final StockRepository stockRepository;
    private final PaymentRepository paymentRepository;
    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseItemRepository purchaseItemRepository, SupplierRepository supplierRepository, StockRepository stockRepository, PaymentRepository paymentRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.supplierRepository = supplierRepository;
        this.stockRepository = stockRepository;
        this.paymentRepository = paymentRepository;
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

    public void saveAll(List<PurchaseDTO> purchaseDTOList) {
        List<Purchase> purchaseList = new ArrayList<>();
        List<Long> ids = purchaseDTOList.stream()
                .map(purchaseDTO -> savePurchase(purchaseDTO))
                .collect(Collectors.toList());
        System.out.println("Generated ids: "+ids);
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
//        purchase.setGstNo(purchaseDTO.getGstNo());
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
        if (purchaseDTO.getId() == null || purchaseDTO.getId() == 0) {
            Payment payment = new Payment();
            payment.setPaymentStatus(Constants.PAY_STATUS_COMPLETED); // AWAITING
            payment.setSource(Constants.SOURCE_PURCHASE);
            payment.setIrrecoverableDebt(false);
            payment.setSourceId(dbPurchase.getId());
            payment.setPaidAmount(dbPurchase.getPaidAmount());
            payment.setPaymentMode(dbPurchase.getPaymentMode());
            payment.setLastFourDigits(purchaseDTO.getTrnLastFourDigits());
            paymentRepository.save(payment);
        }

        log.debug("<< PurchaseService << savePurchase <<");
        return dbPurchase.getId();
    }

    public List<PurchaseDTO> getAllPurchases(String username) {
        log.debug("PurchaseService >> getAllPurchases");
        List<Purchase> purchaseList = null;
        if (username != null && username.equalsIgnoreCase("admin")) {
            purchaseList = purchaseRepository.findAllByActivePurchase("YES");
        } else {
            purchaseList = purchaseRepository.findAllByActivePurchaseAndCreatedBy("YES", username);
        }
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
                stock.setStockStatus(StockStatus.IN_STOCK);
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

    public List<PurchaseDTO> getAllPurchasesBySupplierId(Long id) {
        List<Purchase> purchaseList = purchaseRepository.findBySupplierId(id);
        return purchaseList.stream()
                .map( p -> toDto(p))
                .collect(Collectors.toList());
    }

    public PurchaseDTO addPaymentAndReturnPurchase(Long id, Payment paymentRequest) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Purchase not found with id "+id));
        Payment payment = new Payment();
        payment.setPaymentStatus(Constants.PAY_STATUS_COMPLETED); // AWAITING
        payment.setSource(Constants.SOURCE_PURCHASE);
        payment.setIrrecoverableDebt(false);
        payment.setSourceId(purchase.getId());
        payment.setPaidAmount(paymentRequest.getPaidAmount());
        payment.setPaymentMode(paymentRequest.getPaymentMode());
        payment.setLastFourDigits(paymentRequest.getLastFourDigits());
        paymentRepository.save(payment);

        purchase.setBalAmount(purchase.getBalAmount().subtract(payment.getPaidAmount()));
        purchase.setPaidAmount(purchase.getPaidAmount().add(payment.getPaidAmount()));
        Purchase savedPurchase = purchaseRepository.save(purchase);
        return toDto(savedPurchase);
    }


    public List<ChartData> getYearlyData(LocalDate startDate, LocalDate endDate) {
//        LocalDateTime start = startDate.atStartOfDay();
//        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return purchaseRepository.findPurchasesByYear(startDate, endDate);
    }

    public List<ChartData> getMonthlyData(LocalDate startDate, LocalDate endDate) {
//        LocalDateTime start = startDate.atStartOfDay();
//        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return purchaseRepository.findPurchasesByMonth(startDate, endDate);
    }

    public List<ChartData> getWeeklyData(LocalDate startDate, LocalDate endDate) {
//        LocalDateTime start = startDate.atStartOfDay();
//        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return purchaseRepository.findPurchasesByWeek(startDate, endDate);
    }


    public BigDecimal getCurrentMonthPurchaseTotal() {
        return purchaseRepository.getCurrentMonthTotalPurchaseAmount();
    }
    public List<ChartData> getTopSuppliers() {
//        Pageable pageable = PageRequest.of(0, 3);
//        Limit.of(3);
        return purchaseRepository.findTopSuppliersByTotalAmountLast30Days();
    }

    public List<WeeklyReport> generateReport(ReportFilters filters) {
        filters.setPurchaseType(BillingUtils.emptyToNull(filters.getPurchaseType()));
        filters.setMetalType(BillingUtils.emptyToNull(filters.getMetalType()));
        filters.setPaymentMode(BillingUtils.emptyToNull(filters.getPaymentMode()));
        filters.setIsGstPurchase(BillingUtils.emptyToNull(filters.getIsGstPurchase()));
//        filters.setSupplierId(filters.getSupplierId() > 0 ? filters.getSupplierId() : null);
        log.info("Report filters: {}", filters);
        switch (filters.getReportType()) {
            case YEARLY:
                return generateYearlyReport(filters);
            case MONTHLY:
                return generateMonthlyReport(filters);
            case WEEKLY:
                return generateWeeklyReport(filters);
            case DAILY:
                return getDailyReport(filters);
            case ALL:
            default:
                return getAllTransactionsReport(filters);
        }

    }

    private List<WeeklyReport> getAllTransactionsReport(ReportFilters filters) {
        List<Object[]> results = purchaseRepository.getAllTransactionsReport(filters.getStartDate(), filters.getEndDate(),
                filters.getMetalType(), filters.getPaymentMode(),
                filters.getSupplierId(), filters.getPurchaseType(), filters.getIsGstPurchase());
        return results.stream().map( r -> mapToWeeklyReport(r, filters)).collect(Collectors.toList());
    }

    private List<WeeklyReport> generateYearlyReport(ReportFilters filters) {
        List<Object[]> results = purchaseRepository.getYearlyReport(filters.getStartDate(), filters.getEndDate(),
                filters.getMetalType(), filters.getPaymentMode(),
                filters.getSupplierId(), filters.getPurchaseType(), filters.getIsGstPurchase());
        return results.stream().map( r -> mapToWeeklyReport(r, filters)).collect(Collectors.toList());
    }
    private List<WeeklyReport> generateMonthlyReport(ReportFilters filters) {
        List<Object[]> results = purchaseRepository.getMonthlyReport(filters.getStartDate(), filters.getEndDate(),
                filters.getMetalType(), filters.getPaymentMode(),
                filters.getSupplierId(), filters.getPurchaseType(), filters.getIsGstPurchase());
        return results.stream().map( r -> mapToWeeklyReport(r, filters)).collect(Collectors.toList());
    }

    public List<WeeklyReport> getDailyReport(ReportFilters filters) {
        List<Object[]> results = purchaseRepository.getDailyReport(filters.getStartDate(), filters.getEndDate(),
                filters.getMetalType(), filters.getPaymentMode(),
                filters.getSupplierId(), filters.getPurchaseType(), filters.getIsGstPurchase());
        return results.stream().map( r -> mapToWeeklyReport(r, filters)).collect(Collectors.toList());
    }

    private WeeklyReport mapToWeeklyReport(Object[] result, ReportFilters filters) {
        switch (filters.getReportType()) {
            case YEARLY:
            case MONTHLY:
            case WEEKLY:
            case ALL: {
                LocalDate startDate = ((java.sql.Date) result[0]).toLocalDate();
                LocalDate endDate = ((java.sql.Date) result[1]).toLocalDate();
                BigDecimal totalNetWeight = (BigDecimal) result[2];
                BigDecimal totalGst = (BigDecimal) result[3];
                BigDecimal totalPurchaseAmount = (BigDecimal) result[4];
                BigDecimal totalPaidAmount = (BigDecimal) result[5];
                BigDecimal totalBalAmount = (BigDecimal) result[6];
                return new WeeklyReport(startDate, endDate, totalNetWeight, totalGst, totalPurchaseAmount, totalPaidAmount, totalBalAmount);
            }
            case DAILY:
            default: {
                LocalDate startDate = ((java.sql.Date) result[0]).toLocalDate();
                LocalDate endDate = startDate;
                BigDecimal totalNetWeight = (BigDecimal) result[1];
                BigDecimal totalGst = (BigDecimal) result[2];
                BigDecimal totalPurchaseAmount = (BigDecimal) result[3];
                BigDecimal totalPaidAmount = (BigDecimal) result[4];
                BigDecimal totalBalAmount = (BigDecimal) result[5];
                return new WeeklyReport(startDate, endDate, totalNetWeight, totalGst, totalPurchaseAmount, totalPaidAmount, totalBalAmount);
            }

        }


    }

    private List<WeeklyReport> generateWeeklyReport(ReportFilters filters) {
        List<Object[]> results = purchaseRepository.getWeeklyReport(filters.getStartDate(), filters.getEndDate(),
                filters.getMetalType(), filters.getPaymentMode(),
                filters.getSupplierId(), filters.getPurchaseType(), filters.getIsGstPurchase());
        return results.stream().map( r -> mapToWeeklyReport(r, filters)).collect(Collectors.toList());
    }
//
//    private List<PurchaseReport> generateMonthlyReport(ReportFilters filters) {
//        List<PurchaseReport> reports = new ArrayList<>();
//        LocalDate periodStartDate = filters.getStartDate().with(TemporalAdjusters.firstDayOfMonth());
//        while (!periodStartDate.isAfter(filters.getEndDate())) {
//            LocalDate periodEndDate = periodStartDate.with(TemporalAdjusters.lastDayOfMonth());
//            filters.setEndDate(periodEndDate);
//            reports.add(calculateAggregatedReport(filters));
//            periodStartDate = periodStartDate.plusMonths(1);
//        }
//        return reports;
//    }
//
//    private List<PurchaseReport> generateYearlyReport(ReportFilters filters) {
//        List<PurchaseReport> reports = new ArrayList<>();
//        LocalDate periodStartDate = filters.getStartDate().with(TemporalAdjusters.firstDayOfYear());
//        while (!periodStartDate.isAfter(filters.getEndDate())) {
//            LocalDate periodEndDate = periodStartDate.with(TemporalAdjusters.lastDayOfYear());
//            reports.add(calculateAggregatedReport(filters));
//            periodStartDate = periodStartDate.plusYears(1);
//        }
//        return reports;
//    }
//
//    private List<PurchaseReport> generateAllTransactionsReport(ReportFilters filters) {
////        List<PurchaseReport> reports = new ArrayList<>();
//        List<Purchase> purchases = purchaseRepository.findAll(PurchaseSpecification.byFilters(filters));
//        return purchases.stream().map(p -> convertToDTO(p)).collect(Collectors.toList());
////        reports.add(calculateAggregatedReport(filters));
////        return reports;
//    }
//
//    private PurchaseReport calculateAggregatedReport(ReportFilters filters) {
//        List<Purchase> purchases = purchaseRepository.findAll(PurchaseSpecification.byFilters(filters));
//
//        PurchaseReport report = new PurchaseReport();
//        report.setPeriodStartDate(filters.getStartDate());
//        report.setPeriodEndDate(filters.getEndDate());
//        report.setTotalNetWeight(purchases.stream().map(Purchase::getTotalNetWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
//        report.setTotalPurchaseAmount(purchases.stream().map(Purchase::getTotalPurchaseAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
//        report.setTotalGstAmount(purchases.stream().map(p -> p.getTotalCgstAmount().add(p.getTotalSGstAmount())).reduce(BigDecimal.ZERO, BigDecimal::add));
//        report.setTotalBalAmount(purchases.stream().map(Purchase::getBalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
//        return report;
//    }
//
//
//
//    private PurchaseReport convertToDTO(Purchase purchase) {
//        PurchaseReport dto = new PurchaseReport();
//        dto.setPurchaseDate(purchase.getPurchaseDate());
//        dto.setMetalType(purchase.getMetalType());
//        dto.setSupplierName(purchase.getSupplier().getName());  // Set supplier name
//        dto.setTotalNetWeight(purchase.getTotalNetWeight());
//        dto.setPurchaseRate(purchase.getPurchaseRate());
//        dto.setIsGstPurchase(purchase.getIsGstPurchase());
//        dto.setTotalGstAmount(purchase.getTotalCgstAmount().add(purchase.getTotalSGstAmount()));
//        dto.setGstNo(purchase.getSupplier().getBusinessGstNo());
//        dto.setTotalPurchaseAmount(purchase.getTotalPurchaseAmount());
//        dto.setTotalBalAmount(purchase.getBalAmount());
//        return dto;
//    }
//
//    private PurchaseItem toEntity(PurchaseItemDTO pi, Purchase purchase) {
//        log.debug("PurchaseService >> toEntity >>");
//        PurchaseItem purchaseItem = purchaseItemRepository.findById(pi.getId())
//                .orElseThrow(() -> new EntityNotFoundException("PurhcaseItem entity not found with id: "+pi.getId()));
//        purchaseItem.setPurchase(purchase);
//        if (pi.getStockId() != null && pi.getStockId() > 0) {
//            Stock stock = stockRepository.getReferenceById(pi.getStockId());
//            stock.fromDto(pi);
//            purchaseItem.setStock(stock);
//        }
//        log.debug("PurchaseService << toEntity <<");
//        return purchaseItem;
//    }

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
