package com.billing.service;

import com.billing.dto.SaleDTO;
import com.billing.entity.Customer;
import com.billing.entity.Sale;
import com.billing.entity.SaleItem;
import com.billing.repository.CustomerRepository;
import com.billing.repository.PurchaseItemRepository;
import com.billing.repository.SaleItemRepository;
import com.billing.repository.SaleRepository;
import com.billing.utils.BillingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final SaleItemRepository saleItemRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository, CustomerRepository customerRepository, SaleItemRepository saleItemRepository, PurchaseItemRepository purchaseItemRepository) {
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.saleItemRepository = saleItemRepository;
        this.purchaseItemRepository = purchaseItemRepository;
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Sale getSaleById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Sale not found with id: " + id));
    }
    @Transactional
    public SaleDTO saveSale(SaleDTO sale) {
        Sale saleEntity = sale.toEntity();
        saleEntity.setInvoiceNo(BillingUtils.generateInvoiceNumber());
        Set<SaleItem> saleItemList = new HashSet<>(saleEntity.getSaleItemList().size());
        saleEntity.getSaleItemList().stream()
                .forEach(si -> {
                    SaleItem dbSaleItem = null;
                    if(si.getId() != null && si.getId() > 0) {
                                dbSaleItem = saleItemRepository.findById(si.getId()).orElseThrow();
                            } else {
                                dbSaleItem = si;
                            }
                    if (dbSaleItem.getPurchaseItem().getId() != null && dbSaleItem.getPurchaseItem().getId() > 0) {
                        dbSaleItem.setPurchaseItem(purchaseItemRepository.findById(dbSaleItem.getPurchaseItem().getId()).orElseThrow());
                    }
                    saleItemList.add(dbSaleItem);

                        });


        saleEntity.setSaleItemList(saleItemList);
        Customer customer = customerRepository.findByPhone(saleEntity.getCustomer().getPhone());
        if (null == customer) {
            customer = customerRepository.save(saleEntity.getCustomer());
        }
        saleEntity.setCustomer(customer);
        Sale dbSale = saleRepository.save(saleEntity);
        return dbSale.toDTO();
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    // Implement other methods as needed
}

