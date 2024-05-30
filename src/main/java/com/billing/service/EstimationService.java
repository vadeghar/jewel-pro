package com.billing.service;

import com.billing.dto.EstimationDTO;
import com.billing.dto.EstimationItemDTO;
import com.billing.dto.ExchangeItemDTO;
import com.billing.entity.*;
import com.billing.enums.StockStatus;
import com.billing.repository.EstimationExchangeItemRepository;
import com.billing.repository.EstimationItemRepository;
import com.billing.repository.EstimationRepository;
import com.billing.repository.PurchaseItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EstimationService {
    private final EstimationRepository estimationRepository;
    private final EstimationItemRepository estimationItemRepository;
    private final EstimationExchangeItemRepository estimationExchangeItemRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    public EstimationService(EstimationRepository estimationRepository, EstimationItemRepository estimationItemRepository, EstimationExchangeItemRepository estimationExchangeItemRepository, PurchaseItemRepository purchaseItemRepository) {
        this.estimationRepository = estimationRepository;
        this.estimationItemRepository = estimationItemRepository;
        this.estimationExchangeItemRepository = estimationExchangeItemRepository;
        this.purchaseItemRepository = purchaseItemRepository;
    }

    public Estimation save(Estimation estimation) {
        log.debug("Saving Item {}", estimation);
        return estimationRepository.save(estimation);
    }

    public List<Estimation> getAll() {
        return estimationRepository.findAll();
    }
    public List<EstimationDTO> getAllEstimations() {
        List<Estimation> estimations = estimationRepository.findAll();
        return estimations.stream()
                .map(estimation -> convertToDTO(estimation))
                .collect(Collectors.toList());
    }

    public EstimationDTO getEstimationById(Long id) {
        Estimation estimation = estimationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Estimation Not found "+id));
        return convertToDTO(estimation);
    }

    public EstimationDTO createEstimation(EstimationDTO estimationDTO) {
        Estimation estimation = convertToEntity(estimationDTO);

        // Save customer details
//        Customer customer = convertToCustomerEntity(estimationDTO);
//        customer = customerRepository.save(customer);
//        estimation.setCustomer(customer);
        if (StringUtils.isBlank(estimation.getEstimationNo())) {
            estimation.setEstimationNo(generateEstimationNumber());
        }
        estimation.setStatus("ESTIMATED");
        Estimation savedEstimation = estimationRepository.save(estimation);
        // Save estimation items
        List<EstimationItemDTO> estimationItemDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(estimationDTO.getEstimationItemList())) {
            for (EstimationItemDTO estimationItemDTO : estimationDTO.getEstimationItemList()) {
                if (estimationItemDTO.getWeight() == null || estimationItemDTO.getWeight() == BigDecimal.ZERO) {
                    continue;
                }
                EstimationItem estimationItem = convertToEntity(estimationItemDTO);
                PurchaseItem purchaseItem = purchaseItemRepository.findByStockId(estimationItemDTO.getStockId()).orElseThrow(() -> new EntityNotFoundException("Stock not found with ID: "+estimationItemDTO.getStockId()));
                Stock stock = purchaseItem.getStock();
                estimationItem.setHuid(stock.getHuid());
                estimationItem.setPcs(stock.getPcs());
                estimationItem.setPurity(stock.getPurity());
                estimationItem.setStnType(stock.getStnType());
                estimationItem.setStnWeight(stock.getStnWeight());
                estimationItem.setMetalType(purchaseItem.getPurchase().getMetalType());
                estimationItem.setEstimation(savedEstimation);
                stock.setStockStatus(StockStatus.OUT_OF_STOCK);
                EstimationItem savedEstimationItem = estimationItemRepository.save(estimationItem);
                estimationItemDTOList.add(convertToDTO(savedEstimationItem));
            }
        }

        // Save exchange items
        List<ExchangeItemDTO> exchangeItemDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(estimationDTO.getExchangeItemList())) {
            for (ExchangeItemDTO exchangeItemDTO : estimationDTO.getExchangeItemList()) {
                if (exchangeItemDTO.getWeight() == null || exchangeItemDTO.getWeight() == BigDecimal.ZERO) {
                    continue;
                }
                EstimationExchangeItem exchangeItem = convertToEntity(exchangeItemDTO);
                exchangeItem.setSource("SALE");
                exchangeItem.setSourceId(savedEstimation.getId());
                exchangeItem.setEstimation(savedEstimation);
                EstimationExchangeItem savedExchangeItem = estimationExchangeItemRepository.save(exchangeItem);
                exchangeItemDTOList.add(convertToDTO(savedExchangeItem));
            }
        }
        EstimationDTO savedEstimationDTO = convertToDTO(savedEstimation);
        savedEstimationDTO.setEstimationItemList(estimationItemDTOList);
        savedEstimationDTO.setExchangeItemList(exchangeItemDTOList);
        return savedEstimationDTO;
    }

    private String generateEstimationNumber() {
        Long maxId = estimationRepository.findMaxId();
        int year = LocalDate.now().getYear() % 100; // Last two digits of the year
        int month = LocalDate.now().getMonthValue(); // Current month
        int prefix = 1000 % (year+month);
        return  String.format("%02d%05d", prefix, maxId);
    }

    public EstimationDTO updateEstimation(Long id, EstimationDTO estimationDTO) {
        Estimation existingEstimation = estimationRepository.findById(id).orElse(null);
        if (existingEstimation != null) {
            // Save customer details
//            Customer customer = convertToCustomerEntity(estimationDTO);
//            customer = customerRepository.save(customer);
//            existingEstimation.setCustomer(customer);
            existingEstimation.setStatus("ESTIMATION_UPDATED");
            Estimation savedEstimation = estimationRepository.save(existingEstimation);
            // Save estimation items
            List<EstimationItemDTO> estimationItemDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(estimationDTO.getEstimationItemList())) {
                for (EstimationItemDTO estimationItemDTO : estimationDTO.getEstimationItemList()) {
                    EstimationItem estimationItem = convertToEntity(estimationItemDTO);
                    estimationItem.setEstimation(savedEstimation);
                    EstimationItem savedEstimationItem = estimationItemRepository.save(estimationItem);
                    estimationItemDTOList.add(convertToDTO(savedEstimationItem));
                }
            }

            // Save exchange items
            List<ExchangeItemDTO> exchangeItemDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(estimationDTO.getExchangeItemList())) {
                for (ExchangeItemDTO exchangeItemDTO : estimationDTO.getExchangeItemList()) {
                    EstimationExchangeItem exchangeItem = convertToEntity(exchangeItemDTO);
                    exchangeItem.setEstimation(savedEstimation);
                    EstimationExchangeItem savedExchangeItem = estimationExchangeItemRepository.save(exchangeItem);
                    exchangeItemDTOList.add(convertToDTO(savedExchangeItem));
                }
            }
            EstimationDTO savedEstimationDTO = convertToDTO(savedEstimation);
            savedEstimationDTO.setEstimationItemList(estimationItemDTOList);
            savedEstimationDTO.setExchangeItemList(exchangeItemDTOList);
            return savedEstimationDTO;
        }
        return null;
    }

    public boolean deleteEstimation(Long id) {
        Estimation existingEstimation = estimationRepository.findById(id).orElse(null);
        if (existingEstimation != null) {
            existingEstimation.setStatus("DELETED");
            estimationRepository.save(existingEstimation);
            return true;
        }
        return false;
    }
    
    public Estimation update(Long id, Estimation estimation) {
        log.debug("Saving Item id {} with data {}", id, estimation);
        Estimation estimationDb = estimationRepository.getReferenceById(id);
        if (estimationDb != null) {
            BeanUtils.copyProperties(estimation, estimationDb);
            log.debug("Saving to db item {}", estimationDb);
            return estimationRepository.save(estimationDb);
        } else {
            throw new EntityNotFoundException("Estimation not found with id "+id);
        }
    }

    public void delete(Long id) {
        log.debug("Deleting item id {}", id);
        estimationRepository.deleteById(id);
    }

    public void print(Estimation estimation) {
//        EstimationList estimationList = new EstimationList();
//        estimationList.setEstimationList(Arrays.asList(estimation));
//        EstimationPrinter2 estimationPrinter = new EstimationPrinter2();
//        estimationPrinter.setEstimationList(estimationList);
//        estimationPrinter.initPrint();
    }

    public EstimationDTO convertToDTO(Estimation estimation) {
        EstimationDTO estimationDTO = new EstimationDTO();
        estimationDTO.setId(estimation.getId());
        estimationDTO.setEstimationNo(estimation.getEstimationNo());
        estimationDTO.setEstimationDate(estimation.getEstimationDate());
        estimationDTO.setLastUpdatedTs(estimation.getLastUpdatedTs());
        estimationDTO.setIsGstEstimation(estimation.getIsGstEstimation());
        estimationDTO.setCgstAmount(estimation.getCGstAmount());
        estimationDTO.setSgstAmount(estimation.getSGstAmount());
        estimationDTO.setTotalEstimationAmount(estimation.getTotalEstimationAmount());
        estimationDTO.setTotalExchangeAmount(estimation.getTotalExchangeAmount());
        estimationDTO.setDiscount(estimation.getDiscount());
        estimationDTO.setGrandTotalEstimationAmount(estimation.getGrandTotalEstimationAmount());
        estimationDTO.setPaymentMode(estimation.getPaymentMode());
        estimationDTO.setPaidAmount(estimation.getPaidAmount());
        estimationDTO.setBalAmount(estimation.getBalAmount());
        estimationDTO.setDescription(estimation.getDescription());

        List<EstimationItemDTO> estimationItemDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(estimation.getEstimationItemList())) {
            for (EstimationItem estimationItem : estimation.getEstimationItemList()) {
                estimationItemDTOList.add(convertToDTO(estimationItem));
            }
        }
        estimationDTO.setEstimationItemList(estimationItemDTOList);

        List<ExchangeItemDTO> exchangeItemDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(estimation.getExchangeItemList())) {
            for (EstimationExchangeItem exchangeItem : estimation.getExchangeItemList()) {
                exchangeItemDTOList.add(convertToDTO(exchangeItem));
            }
        }
        estimationDTO.setExchangeItemList(exchangeItemDTOList);

        return estimationDTO;
    }

    public Estimation convertToEntity(EstimationDTO estimationDTO) {
        Estimation estimation = new Estimation();
        estimation.setId(estimationDTO.getId());
//        estimation.setCustomer(estimationDTO.getCustomer());
        estimation.setEstimationNo(estimationDTO.getEstimationNo());
        estimation.setEstimationDate(estimationDTO.getEstimationDate());
//        estimation.setLastUpdatedTs(estimationDTO.getLastUpdatedTs());
        estimation.setIsGstEstimation(estimationDTO.getIsGstEstimation());
        estimation.setCGstAmount(estimationDTO.getCgstAmount());
        estimation.setSGstAmount(estimationDTO.getSgstAmount());
        estimation.setTotalEstimationAmount(estimationDTO.getTotalEstimationAmount());
        estimation.setTotalExchangeAmount(estimationDTO.getTotalExchangeAmount());
        estimation.setDiscount(estimationDTO.getDiscount());
        estimation.setGrandTotalEstimationAmount(estimationDTO.getGrandTotalEstimationAmount());
        estimation.setPaymentMode(estimationDTO.getPaymentMode());
        estimation.setPaidAmount(estimationDTO.getPaidAmount());
        estimation.setBalAmount(estimationDTO.getBalAmount());
        estimation.setDescription(estimationDTO.getDescription());
        return estimation;
    }

    public EstimationItemDTO convertToDTO(EstimationItem estimationItem) {
        EstimationItemDTO estimationItemDTO = new EstimationItemDTO();
        estimationItemDTO.setId(estimationItem.getId());
        estimationItemDTO.setStockId(estimationItem.getStockId());
        estimationItemDTO.setName(estimationItem.getName());
        estimationItemDTO.setCode(estimationItem.getCode());
        estimationItemDTO.setMetalType(estimationItem.getMetalType());
        estimationItemDTO.setPurity(estimationItem.getPurity());
        estimationItemDTO.setWeight(estimationItem.getWeight());
        estimationItemDTO.setStnWeight(estimationItem.getStnWeight());
        estimationItemDTO.setNetWeight(estimationItem.getNetWeight());
        estimationItemDTO.setVaWeight(estimationItem.getVaWeight());
        estimationItemDTO.setMakingCharge(estimationItem.getMakingCharge());
        estimationItemDTO.setStnType(estimationItem.getStnType());
        estimationItemDTO.setStnCostPerCt(estimationItem.getStnCostPerCt());
        estimationItemDTO.setPcs(estimationItem.getPcs());
        estimationItemDTO.setHuid(estimationItem.getHuid());
        estimationItemDTO.setRate(estimationItem.getRate());
        estimationItemDTO.setItemTotal(estimationItem.getItemTotal());
        return estimationItemDTO;
    }

    public EstimationItem convertToEntity(EstimationItemDTO estimationItemDTO) {
        EstimationItem estimationItem = new EstimationItem();
        estimationItem.setId(estimationItemDTO.getId());
        estimationItem.setStockId(estimationItemDTO.getStockId());
        estimationItem.setName(estimationItemDTO.getName());
        estimationItem.setCode(estimationItemDTO.getCode());
        estimationItem.setMetalType(estimationItemDTO.getMetalType());
        estimationItem.setPurity(estimationItemDTO.getPurity());
        estimationItem.setWeight(estimationItemDTO.getWeight());
        estimationItem.setStnWeight(estimationItemDTO.getStnWeight());
        estimationItem.setNetWeight(estimationItemDTO.getNetWeight());
        estimationItem.setVaWeight(estimationItemDTO.getVaWeight());
        estimationItem.setMakingCharge(estimationItemDTO.getMakingCharge());
        estimationItem.setStnType(estimationItemDTO.getStnType());
        estimationItem.setStnCostPerCt(estimationItemDTO.getStnCostPerCt());
        estimationItem.setPcs(estimationItemDTO.getPcs());
        estimationItem.setHuid(estimationItemDTO.getHuid());
        estimationItem.setRate(estimationItemDTO.getRate());
        estimationItem.setItemTotal(estimationItemDTO.getItemTotal());
        return estimationItem;
    }

    public ExchangeItemDTO convertToDTO(EstimationExchangeItem exchangeItem) {
        ExchangeItemDTO exchangeItemDTO = new ExchangeItemDTO();
        exchangeItemDTO.setId(exchangeItem.getId());
        exchangeItemDTO.setWeight(exchangeItem.getWeight());
        exchangeItemDTO.setMeltPercentage(exchangeItem.getMeltPercentage());
        exchangeItemDTO.setWastageInGms(exchangeItem.getWastageInGms());
        exchangeItemDTO.setNetWeight(exchangeItem.getNetWeight());
        exchangeItemDTO.setItemDesc(exchangeItem.getItemDesc());
        exchangeItemDTO.setSource(exchangeItem.getSource());
        exchangeItemDTO.setSourceId(exchangeItem.getSourceId());
        exchangeItemDTO.setRate(exchangeItem.getRate());
        exchangeItemDTO.setExchangeValue(exchangeItem.getExchangeValue());
        return exchangeItemDTO;
    }

    public EstimationExchangeItem convertToEntity(ExchangeItemDTO exchangeItemDTO) {
        EstimationExchangeItem exchangeItem = new EstimationExchangeItem();
        exchangeItem.setId(exchangeItemDTO.getId());
        exchangeItem.setWeight(exchangeItemDTO.getWeight());
        exchangeItem.setMeltPercentage(exchangeItemDTO.getMeltPercentage());
        exchangeItem.setWastageInGms(exchangeItemDTO.getWastageInGms());
        exchangeItem.setNetWeight(exchangeItemDTO.getNetWeight());
        exchangeItem.setItemDesc(exchangeItemDTO.getItemDesc());
        exchangeItem.setSource(exchangeItemDTO.getSource());
        exchangeItem.setSourceId(exchangeItemDTO.getSourceId());
        exchangeItem.setRate(exchangeItemDTO.getRate());
        exchangeItem.setExchangeValue(exchangeItemDTO.getExchangeValue());
        return exchangeItem;
    }



}
