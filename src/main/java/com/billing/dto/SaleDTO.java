package com.billing.dto;

import com.billing.entity.Customer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SaleDTO {


    private Long id;
    private String saleType;
    private Customer customer;
    private String invoiceNo;
    private LocalDate saleDate;
    private LocalDateTime lastUpdatedTs;
    private String isGstSale;
    private BigDecimal cgstAmount;
    private BigDecimal sgstAmount;
    private BigDecimal totalSaleAmount;
    private BigDecimal totalExchangeAmount;
    private BigDecimal discount;
    private BigDecimal grandTotalSaleAmount;
    private String paymentMode;
    private BigDecimal paidAmount;
    private BigDecimal balAmount;
    private String description;
    private List<SaleItemDTO> saleItemList;
    private List<ExchangeItemDTO> exchangeItemList;
    private String trnLastFourDigits;


//    private Long id;
//    private Long stockId;
//    private String saleType = "SALE"; // Sale / Sale Return
//    private Customer customer;
//    private Long customerId;
//    private String invoiceNo;
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private LocalDate saleDate;
//    private LocalDateTime lastUpdatedTs = LocalDateTime.now();
//
//    private String isGstSale = "NO";
//    private BigDecimal cGstAmount;
//    private BigDecimal sGstAmount;
//    private BigDecimal totalSaleAmount;
//    private BigDecimal totalExchangeAmount;
//    private BigDecimal discount;
//    private BigDecimal grandTotalSaleAmount;
//    private String paymentMode;
//    private BigDecimal paidAmount;
//    private BigDecimal balAmount;
//    private String description;
//    private List<SaleItemDTO> saleItemList = new ArrayList<>();
//    private List<ExchangeItemDTO> exchangedItems = new ArrayList<>();

//    public void mapDtoToEntity(SaleDTO saleDTO, Sale sale) {
//        sale.setId(saleDTO.getId());
//        sale.setSaleType(saleDTO.getSaleType());
//        //sale.setCustomer(saleDTO.getCustomer());
//        sale.setInvoiceNo(saleDTO.getInvoiceNo());
//        sale.setSaleDate(saleDTO.getSaleDate());
////        sale.setLastUpdatedTs(saleDTO.getLastUpdatedTs());
//        sale.setIsGstSale(saleDTO.getIsGstSale());
//        sale.setCGstAmount(saleDTO.getCGstAmount());
//        sale.setSGstAmount(saleDTO.getSGstAmount());
//        sale.setTotalSaleAmount(saleDTO.getTotalSaleAmount());
//        sale.setTotalExchangeAmount(saleDTO.getTotalExchangeAmount());
//        sale.setDiscount(saleDTO.getDiscount());
//        sale.setGrandTotalSaleAmount(saleDTO.getGrandTotalSaleAmount());
//        sale.setPaymentMode(saleDTO.getPaymentMode());
//        sale.setPaidAmount(saleDTO.getPaidAmount());
//        sale.setBalAmount(saleDTO.getBalAmount());
//        sale.setSaleItemList(!CollectionUtils.isEmpty(saleDTO.getSaleItemList()) ? saleDTO.getSaleItemList().stream().map(saleItemDTO -> {
//            SaleItem saleItem = new SaleItem();
//            saleItemDTO.mapDtoToEntity(saleItemDTO, saleItem);
//            return saleItem;
//        }).collect(Collectors.toList()) : new ArrayList<>());
//        sale.setExchangeItemList(!CollectionUtils.isEmpty(saleDTO.getExchangedItems()) ? saleDTO.getExchangedItems().stream().map(exchangeItemDTO -> {
//            ExchangeItem exchangeItem = new ExchangeItem();
//            exchangeItemDTO.mapDtoToEntity(exchangeItemDTO, exchangeItem);
//            return exchangeItem;
//        }).collect(Collectors.toList()) : new ArrayList<>());
//        sale.setDescription(saleDTO.getDescription());
//
//    }
}
