package com.billing.service;

import com.billing.dto.ExchangeItemDTO;
import com.billing.dto.SaleDTO;
import com.billing.dto.SaleItemDTO;
import com.billing.entity.*;
import com.billing.enums.SaleType;
import com.billing.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final SaleItemRepository saleItemRepository;
    private final ExchangeItemRepository exchangeItemRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final StockRepository stockRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository, CustomerRepository customerRepository, SaleItemRepository saleItemRepository, ExchangeItemRepository exchangeItemRepository, PurchaseItemRepository purchaseItemRepository, StockRepository stockRepository) {
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.saleItemRepository = saleItemRepository;
        this.exchangeItemRepository = exchangeItemRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.stockRepository = stockRepository;
    }

    public SaleDTO createSale(SaleDTO saleDTO) {
        Sale sale = convertToEntity(saleDTO);

        // Save customer details
        Customer customer = convertToCustomerEntity(saleDTO);
        customer = customerRepository.save(customer);
        sale.setCustomer(customer);
        if (StringUtils.isBlank(sale.getInvoiceNo())) {
            sale.setInvoiceNo(generateInvoiceNumber());
        }
        Sale savedSale = saleRepository.save(sale);

        // Save sale items
        List<SaleItemDTO> saleItemDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(saleDTO.getSaleItemList())) {
            for (SaleItemDTO saleItemDTO : saleDTO.getSaleItemList()) {
                SaleItem saleItem = convertToEntity(saleItemDTO);
                PurchaseItem purchaseItem = purchaseItemRepository.findByStockId(saleItemDTO.getStockId()).orElseThrow(() -> new EntityNotFoundException("Stock not found with ID: "+saleItemDTO.getStockId()));
                Stock stock = purchaseItem.getStock();
                //huid, pcs, purity, stn_type,
                saleItem.setHuid(stock.getHuid());
                saleItem.setPcs(stock.getPcs());
                saleItem.setPurity(stock.getPurity());
                saleItem.setStnType(stock.getStnType());
                saleItem.setStnWeight(stock.getStnWeight());
                saleItem.setMetalType(purchaseItem.getPurchase().getMetalType());
                saleItem.setSale(savedSale);
                SaleItem savedSaleItem = saleItemRepository.save(saleItem);
                saleItemDTOList.add(convertToDTO(savedSaleItem));
            }
        }

        // Save exchange items
        List<ExchangeItemDTO> exchangeItemDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(saleDTO.getExchangeItemList())) {
            for (ExchangeItemDTO exchangeItemDTO : saleDTO.getExchangeItemList()) {
                ExchangeItem exchangeItem = convertToEntity(exchangeItemDTO);
                exchangeItem.setSource("SALE");
                exchangeItem.setSourceId(savedSale.getId());
                exchangeItem.setSale(savedSale);
                ExchangeItem savedExchangeItem = exchangeItemRepository.save(exchangeItem);
                exchangeItemDTOList.add(convertToDTO(savedExchangeItem));
            }
        }
        SaleDTO savedSaleDTO = convertToDTO(savedSale);
        savedSaleDTO.setSaleItemList(saleItemDTOList);
        savedSaleDTO.setExchangeItemList(exchangeItemDTOList);
        return savedSaleDTO;
    }

    private String generateInvoiceNumber() {
        Long maxId = saleRepository.findMaxId();
        int year = LocalDate.now().getYear() % 100; // Last two digits of the year
        int month = LocalDate.now().getMonthValue(); // Current month
        int prefix = 1000 % (year+month);
        return  String.format("%02d%05d", prefix, maxId);
    }

    public List<SaleDTO> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        return sales.stream()
                .map(sale -> convertToDTO(sale))
                .collect(Collectors.toList());
    }

    public SaleDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id).orElse(null);
        return sale != null ? convertToDTO(sale) : null;
    }

    public SaleDTO updateSale(Long id, SaleDTO saleDTO) {
        Sale existingSale = saleRepository.findById(id).orElse(null);
        if (existingSale != null) {
            // Save customer details
            Customer customer = convertToCustomerEntity(saleDTO);
            customer = customerRepository.save(customer);
            existingSale.setCustomer(customer);
            Sale savedSale = saleRepository.save(existingSale);
            // Save sale items
            List<SaleItemDTO> saleItemDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(saleDTO.getSaleItemList())) {
                for (SaleItemDTO saleItemDTO : saleDTO.getSaleItemList()) {
                    SaleItem saleItem = convertToEntity(saleItemDTO);
                    saleItem.setSale(savedSale);
                    SaleItem savedSaleItem = saleItemRepository.save(saleItem);
                    saleItemDTOList.add(convertToDTO(savedSaleItem));
                }
            }

            // Save exchange items
            List<ExchangeItemDTO> exchangeItemDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(saleDTO.getExchangeItemList())) {
                for (ExchangeItemDTO exchangeItemDTO : saleDTO.getExchangeItemList()) {
                    ExchangeItem exchangeItem = convertToEntity(exchangeItemDTO);
                    exchangeItem.setSale(savedSale);
                    ExchangeItem savedExchangeItem = exchangeItemRepository.save(exchangeItem);
                    exchangeItemDTOList.add(convertToDTO(savedExchangeItem));
                }
            }
            SaleDTO savedSaleDTO = convertToDTO(savedSale);
            savedSaleDTO.setSaleItemList(saleItemDTOList);
            savedSaleDTO.setExchangeItemList(exchangeItemDTOList);
            return savedSaleDTO;
        }
        return null;
    }

    public boolean deleteSale(Long id) {
        Sale existingSale = saleRepository.findById(id).orElse(null);
        if (existingSale != null) {
            existingSale.setSaleType(SaleType.DEPROSIONED.name());
            saleRepository.save(existingSale);
            return true;
        }
        return false;
    }

//    private Sale saveOrUpdate() {
//
//    }

    private Customer convertToCustomerEntity(SaleDTO saleDTO) {
        Customer customer = new Customer();
        customer.setId(saleDTO.getCustomer() != null ? saleDTO.getCustomer().getId(): null);
        customer.setPhone(saleDTO.getCustomer().getPhone());
        customer.setAddress(saleDTO.getCustomer().getAddress());
        customer.setEmail(saleDTO.getCustomer().getEmail());
        customer.setName(saleDTO.getCustomer().getName());
        return customer;
    }


    public List<Sale> getAllSales2() {
        return saleRepository.findAll();
    }

    public Sale convertToEntity(SaleDTO saleDTO) {
        Sale sale = new Sale();
        sale.setId(saleDTO.getId());
        sale.setSaleType(saleDTO.getSaleType());
        sale.setCustomer(saleDTO.getCustomer());
        sale.setInvoiceNo(saleDTO.getInvoiceNo());
        sale.setSaleDate(saleDTO.getSaleDate());
//        sale.setLastUpdatedTs(saleDTO.getLastUpdatedTs());
        sale.setIsGstSale(saleDTO.getIsGstSale());
        sale.setCGstAmount(saleDTO.getCgstAmount());
        sale.setSGstAmount(saleDTO.getSgstAmount());
        sale.setTotalSaleAmount(saleDTO.getTotalSaleAmount());
        sale.setTotalExchangeAmount(saleDTO.getTotalExchangeAmount());
        sale.setDiscount(saleDTO.getDiscount());
        sale.setGrandTotalSaleAmount(saleDTO.getGrandTotalSaleAmount());
        sale.setPaymentMode(saleDTO.getPaymentMode());
        sale.setPaidAmount(saleDTO.getPaidAmount());
        sale.setBalAmount(saleDTO.getBalAmount());
        sale.setDescription(saleDTO.getDescription());

//        List<SaleItem> saleItemList = new ArrayList<>();
//        for (SaleItemDTO saleItemDTO : saleDTO.getSaleItemList()) {
//            saleItemList.add(convertToEntity(saleItemDTO));
//        }
//        sale.setSaleItemList(saleItemList);
//
//        List<ExchangeItem> exchangeItemList = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(saleDTO.getExchangeItemList())) {
//            for (ExchangeItemDTO exchangeItemDTO : saleDTO.getExchangeItemList()) {
//                exchangeItemList.add(convertToEntity(exchangeItemDTO));
//            }
//            sale.setExchangeItemList(exchangeItemList);
//        }
        return sale;
    }

    public SaleDTO convertToDTO(Sale sale) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(sale.getId());
        saleDTO.setSaleType(sale.getSaleType());
        saleDTO.setCustomer(sale.getCustomer());
        saleDTO.setInvoiceNo(sale.getInvoiceNo());
        saleDTO.setSaleDate(sale.getSaleDate());
        saleDTO.setLastUpdatedTs(sale.getLastUpdatedTs());
        saleDTO.setIsGstSale(sale.getIsGstSale());
        saleDTO.setCgstAmount(sale.getCGstAmount());
        saleDTO.setSgstAmount(sale.getSGstAmount());
        saleDTO.setTotalSaleAmount(sale.getTotalSaleAmount());
        saleDTO.setTotalExchangeAmount(sale.getTotalExchangeAmount());
        saleDTO.setDiscount(sale.getDiscount());
        saleDTO.setGrandTotalSaleAmount(sale.getGrandTotalSaleAmount());
        saleDTO.setPaymentMode(sale.getPaymentMode());
        saleDTO.setPaidAmount(sale.getPaidAmount());
        saleDTO.setBalAmount(sale.getBalAmount());
        saleDTO.setDescription(sale.getDescription());

        List<SaleItemDTO> saleItemDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sale.getSaleItemList())) {
            for (SaleItem saleItem : sale.getSaleItemList()) {
                saleItemDTOList.add(convertToDTO(saleItem));
            }
        }
        saleDTO.setSaleItemList(saleItemDTOList);

        List<ExchangeItemDTO> exchangeItemDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sale.getExchangeItemList())) {
            for (ExchangeItem exchangeItem : sale.getExchangeItemList()) {
                exchangeItemDTOList.add(convertToDTO(exchangeItem));
            }
        }
        saleDTO.setExchangeItemList(exchangeItemDTOList);

        return saleDTO;
    }



    public SaleItemDTO convertToDTO(SaleItem saleItem) {
        SaleItemDTO saleItemDTO = new SaleItemDTO();
        saleItemDTO.setId(saleItem.getId());
        saleItemDTO.setStockId(saleItem.getStockId());
        saleItemDTO.setName(saleItem.getName());
        saleItemDTO.setCode(saleItem.getCode());
        saleItemDTO.setMetalType(saleItem.getMetalType());
        saleItemDTO.setPurity(saleItem.getPurity());
        saleItemDTO.setWeight(saleItem.getWeight());
        saleItemDTO.setStnWeight(saleItem.getStnWeight());
        saleItemDTO.setNetWeight(saleItem.getNetWeight());
        saleItemDTO.setVaWeight(saleItem.getVaWeight());
        saleItemDTO.setMakingCharge(saleItem.getMakingCharge());
        saleItemDTO.setStnType(saleItem.getStnType());
        saleItemDTO.setStnCostPerCt(saleItem.getStnCostPerCt());
        saleItemDTO.setPcs(saleItem.getPcs());
        saleItemDTO.setHuid(saleItem.getHuid());
        saleItemDTO.setRate(saleItem.getRate());
        saleItemDTO.setItemTotal(saleItem.getItemTotal());
        return saleItemDTO;
    }

    public SaleItem convertToEntity(SaleItemDTO saleItemDTO) {
        SaleItem saleItem = new SaleItem();
        saleItem.setId(saleItemDTO.getId());
        saleItem.setStockId(saleItemDTO.getStockId());
        saleItem.setName(saleItemDTO.getName());
        saleItem.setCode(saleItemDTO.getCode());
        saleItem.setMetalType(saleItemDTO.getMetalType());
        saleItem.setPurity(saleItemDTO.getPurity());
        saleItem.setWeight(saleItemDTO.getWeight());
        saleItem.setStnWeight(saleItemDTO.getStnWeight());
        saleItem.setNetWeight(saleItemDTO.getNetWeight());
        saleItem.setVaWeight(saleItemDTO.getVaWeight());
        saleItem.setMakingCharge(saleItemDTO.getMakingCharge());
        saleItem.setStnType(saleItemDTO.getStnType());
        saleItem.setStnCostPerCt(saleItemDTO.getStnCostPerCt());
        saleItem.setPcs(saleItemDTO.getPcs());
        saleItem.setHuid(saleItemDTO.getHuid());
        saleItem.setRate(saleItemDTO.getRate());
        saleItem.setItemTotal(saleItemDTO.getItemTotal());
        return saleItem;
    }

    public ExchangeItemDTO convertToDTO(ExchangeItem exchangeItem) {
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

    public ExchangeItem convertToEntity(ExchangeItemDTO exchangeItemDTO) {
        ExchangeItem exchangeItem = new ExchangeItem();
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
    
//    public List<SaleDTO> getAllSales() {
//        List<Sale> saleList = saleRepository.findAll();
//        return saleList.stream().map(sale -> sale.toDTO()).collect(Collectors.toList());
//    }


//    public Sale getSaleById2(Long id) {
//        return saleRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Sale not found with id: " + id));
//
//    }
//
//    public SaleDTO getSaleById(Long id) {
//        Sale sale = saleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sale not found with id "+id));
//        return sale.toDTO();
//    }


//    @Transactional
//    public SaleDTO saveSale3(SaleDTO sale) {
//        Sale saleEntity = new Sale();
//        sale.mapDtoToEntity(sale, saleEntity);
//        saleEntity.setInvoiceNo(BillingUtils.generateInvoiceNumber());
//        List<SaleItem> saleItemList = new ArrayList<>(saleEntity.getSaleItemList().size());
//        saleEntity.getSaleItemList().stream()
//                .forEach(si -> {
//                    SaleItem dbSaleItem = null;
//                    if(si.getId() != null && si.getId() > 0) {
//                                dbSaleItem = saleItemRepository.findById(si.getId()).orElseThrow();
//                            } else {
//                                dbSaleItem = si;
//                            }
//                    if (dbSaleItem.getStock().getId() != null && dbSaleItem.getStock().getId() > 0) {
//                        dbSaleItem.setStock(stockRepository.findById(dbSaleItem.getStock().getId()).orElseThrow());
//                    }
//                    saleItemList.add(dbSaleItem);
//
//                        });
//
//
//        saleEntity.setSaleItemList(saleItemList);
//        Customer customer = customerRepository.findByPhone(saleEntity.getCustomer().getPhone());
//        if (null == customer) {
//            customer = customerRepository.save(saleEntity.getCustomer());
//        }
//        saleEntity.setCustomer(customer);
//        Sale dbSale = saleRepository.save(saleEntity);
//        return dbSale.toDTO();
//    }

//    public SaleDTO saveSale(SaleDTO saleDTO) {
//        log.debug("SaleService >> saveSale >> purchaseId: {}, sale item count: {}", saleDTO.getId(), saleDTO.getSaleItemList().size());
//        Sale sale = new Sale();
//        if(saleDTO.getId() != null && saleDTO.getId() > 0) {
//            sale = saleRepository.findById(saleDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Sale not found with id: "+saleDTO.getId()));
//        }
//        Customer customer = null;
//        if (sale.getCustomer() != null && sale.getCustomer().getId() != null) {
//            customer = customerRepository.findById(sale.getCustomer().getId()).orElse(null);
//        }
//        if (customer == null) {
//            customer = customerRepository.findByPhone(saleDTO.getCustomer().getPhone());
//        }
//        if (null == customer) {
//            //sale.setCustomer(customer);
//            customer = new Customer();
//        }
//        //customer = new Customer();
////        customer.setSales(Arrays.asList(sale));
//        customer.setName(saleDTO.getCustomer().getName());
//        customer.setAddress(saleDTO.getCustomer().getAddress());
//        customer.setPhone(saleDTO.getCustomer().getPhone());
//        customer.setEmail(saleDTO.getCustomer().getEmail());
//        sale.setCustomer(customer);
//
//
//        saleDTO.mapDtoToEntity(saleDTO, sale);
//        setSaleItems(saleDTO, sale);
//        setExchangeItems(saleDTO, sale);
//        Sale savedSale = saleRepository.save(sale);
//        log.debug("SaleService << saveSale <<");
//        return mapEntityToDto(savedSale);
//    }
//
//    private void setSaleItems(SaleDTO saleDTO, Sale sale) {
//        List<SaleItem> existingSaleItems = sale.getSaleItemList();
//
//        List<SaleItem> missingItems = existingSaleItems.stream()
//                .filter(existingItem -> saleDTO.getExchangedItems().stream()
//                        .noneMatch(saleItem -> saleItem.getId() == existingItem.getId()))
//                .collect(Collectors.toList());
//
//        List<SaleItem> dbSaleItemList = new ArrayList<>();
//        saleDTO.getSaleItemList().stream().forEach(siDto -> {
//            if (siDto.getId() == null) {
//                SaleItem saleItem = new SaleItem();
//                siDto.mapDtoToEntity(siDto, saleItem);
//                saleItem.setSale(sale);
//                Stock stock = stockRepository.findById(siDto.getStockId()).orElseThrow(() -> new EntityNotFoundException("Stock not exist with id: "+siDto.getStockId()));
//                stock.setStockStatus(StockStatus.OUT_OF_STOCK);
//                saleItem.setStockId(stock.getId());
//                dbSaleItemList.add(saleItem);
//            } else {
//                SaleItem saleItem = existingSaleItems.stream()
//                        .filter(si -> si.getId().equals(siDto.getId()))
//                        .findFirst()
//                        .orElse(new SaleItem());
//                siDto.mapDtoToEntity(siDto, saleItem);
//                saleItem.setSale(sale);
//                Stock stock = stockRepository.findById(siDto.getStockId()).orElseThrow(() -> new EntityNotFoundException("Stock not exist with id: "+siDto.getStockId()));
//                stock.setStockStatus(StockStatus.OUT_OF_STOCK);
//                saleItem.setStockId(stock.getId());
//                dbSaleItemList.add(saleItem);
//            }
//        });
//        if (!CollectionUtils.isEmpty(missingItems)) {
//            missingItems.stream().forEach(saleItem -> {
//                sale.getSaleItemList().remove(saleItem);
//            });
//            saleItemRepository.deleteAll(missingItems);
//        }
//        sale.setSaleItemList(dbSaleItemList);
//    }
//
//
//    private void setExchangeItems(SaleDTO saleDTO, Sale sale) {
//        List<ExchangeItem> exchangeItemList = sale.getExchangeItemList();
//
//        List<ExchangeItem> missingItems = exchangeItemList.stream()
//                .filter(existingItem -> saleDTO.getExchangedItems().stream()
//                        .noneMatch(saleItem -> saleItem.getId() == existingItem.getId()))
//                .collect(Collectors.toList());
//
//        List<ExchangeItem> dbExchnageItemList = new ArrayList<>();
//        saleDTO.getExchangedItems().stream().forEach(exDto -> {
//            if (exDto.getId() == null) {
//                ExchangeItem exchangeItem = new ExchangeItem();
//                exchangeItem.setSale(sale);
//                dbExchnageItemList.add(exchangeItem);
//            } else {
//                ExchangeItem exchangeItem = exchangeItemList.stream()
//                        .filter(si -> si.getId().equals(exDto.getId()))
//                        .findFirst()
//                        .orElse(new ExchangeItem());
//                exchangeItem.setSale(sale);
//                dbExchnageItemList.add(exchangeItem);
//            }
//        });
//        if (!CollectionUtils.isEmpty(missingItems)) {
//            missingItems.stream().forEach(exchangeItem -> {
//                sale.getExchangeItemList().remove(exchangeItem);
//            });
//            exchangeItemRepository.deleteAll(missingItems);
//        }
//        sale.setExchangeItemList(dbExchnageItemList);
//    }

//    public void deleteSale(Long id) {
//        saleRepository.deleteById(id);
//    }

//
//    public SaleDTO mapEntityToDto(Sale sale) {
//        SaleDTO saleDTO = new SaleDTO();
//        saleDTO.setId(sale.getId());
//        saleDTO.setSaleType(sale.getSaleType());
//        //saleDTO.setCustomer(sale.getCustomer());
//        saleDTO.setInvoiceNo(sale.getInvoiceNo());
//        saleDTO.setSaleDate(sale.getSaleDate());
////        saleDTO.setLastUpdatedTs(sale.getLastUpdatedTs());
//        saleDTO.setIsGstSale(sale.getIsGstSale());
//        saleDTO.setCGstAmount(sale.getCGstAmount());
//        saleDTO.setSGstAmount(sale.getSGstAmount());
//        saleDTO.setTotalSaleAmount(sale.getTotalSaleAmount());
//        saleDTO.setTotalExchangeAmount(sale.getTotalExchangeAmount());
//        saleDTO.setDiscount(sale.getDiscount());
//        saleDTO.setGrandTotalSaleAmount(sale.getGrandTotalSaleAmount());
//        saleDTO.setPaymentMode(sale.getPaymentMode());
//        saleDTO.setPaidAmount(sale.getPaidAmount());
//        saleDTO.setBalAmount(sale.getBalAmount());
//        saleDTO.setSaleItemList(!CollectionUtils.isEmpty(sale.getSaleItemList()) ? sale.getSaleItemList().stream().map(saleItem -> {
//            return saleItem.toDto(saleItem);
//        }).collect(Collectors.toList()) : new ArrayList<>());
//        saleDTO.setExchangedItems(!CollectionUtils.isEmpty(sale.getExchangeItemList()) ? sale.getExchangeItemList().stream().map(exchangeItem -> {
//            return exchangeItem.toDto();
//        }).collect(Collectors.toList()) : new ArrayList<>());
//        saleDTO.setDescription(sale.getDescription());
//        return saleDTO;
//    }


    // Implement other methods as needed
}

