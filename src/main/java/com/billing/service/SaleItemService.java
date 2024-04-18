package com.billing.service;

import com.billing.entity.Sale;
import com.billing.entity.SaleItem;
import com.billing.repository.SaleItemRepository;
import com.billing.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SaleItemService {
    private final SaleItemRepository saleItemRepository;

    @Autowired
    public SaleItemService(SaleItemRepository saleItemRepository) {
        this.saleItemRepository = saleItemRepository;
    }

    public List<SaleItem> getAllSaleItems() {
        return saleItemRepository.findAll();
    }

    public SaleItem getSaleItemById(Long id) {
        return saleItemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SaleItem not found with id: " + id));
    }

    public void saveSaleItem(SaleItem saleItem) {
        saleItemRepository.save(saleItem);
    }

    public void deleteSaleItem(Long id) {
        saleItemRepository.deleteById(id);
    }

    // Implement other methods as needed
}

