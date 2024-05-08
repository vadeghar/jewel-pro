package com.billing.service;

import com.billing.dto.SaleItemDTO;
import com.billing.entity.Sale;
import com.billing.entity.SaleItem;
import com.billing.repository.SaleItemRepository;
import com.billing.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SaleItemService {
    private final SaleItemRepository saleItemRepository;
    private final SaleRepository saleRepository;

    @Autowired
    public SaleItemService(SaleItemRepository saleItemRepository, SaleRepository saleRepository) {
        this.saleItemRepository = saleItemRepository;
        this.saleRepository = saleRepository;
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

    public List<SaleItemDTO> getAllSaleItemsForSale(Long saleId) {
        List<SaleItemDTO> saleItemDTOList = new ArrayList<>();
        List<SaleItem> saleItemList = saleItemRepository.findBySaleId(saleId);
        if (!CollectionUtils.isEmpty(saleItemList)) {
            for (SaleItem saleItem : saleItemList) {
                saleItemDTOList.add(convertToDTO(saleItem));
            }
        }
        return saleItemDTOList;
    }

    public Optional<SaleItemDTO> getSaleItemByIdForSale(Long saleId, Long itemId) {
        SaleItem saleItem = saleItemRepository.findByIdAndSaleId(itemId, saleId);
        return Optional.of(convertToDTO(saleItem));
    }

    public SaleItemDTO createSaleItemForSale(Long saleId, SaleItemDTO saleItemDTO) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new EntityNotFoundException("Sale is not found with id: "+saleId));
        SaleItem saleItem = convertToEntity(saleItemDTO);
        saleItem.setSale(sale);
        SaleItem saleItem1 = saleItemRepository.save(saleItem);
        return convertToDTO(saleItem1);
    }

    public SaleItemDTO updateSaleItemForSale(Long saleId, Long itemId, SaleItemDTO saleItemDTO) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new EntityNotFoundException("Sale is not found with id: "+saleId));
        SaleItem saleItem = convertToEntity(saleItemDTO);
        saleItem.setSale(sale);
        SaleItem saleItem1 = saleItemRepository.save(saleItem);
        return convertToDTO(saleItem1);
    }

    public boolean deleteSaleItemForSale(Long saleId, Long itemId) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new EntityNotFoundException("Sale is not found with id: "+saleId));
        SaleItem saleItem = saleItemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("SaleItem is not found with id: "+itemId));
        saleItemRepository.delete(saleItem);
        return true;
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




    // Implement other methods as needed
}

