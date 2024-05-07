package com.billing.service;

import com.billing.dto.ExchangeItemDTO;
import com.billing.dto.StockDTO;
import com.billing.entity.Stock;
import com.billing.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<StockDTO> getStockByCode(String code) {
        List<StockDTO> stockDTOList = new ArrayList<>();
        log.debug("StockService >> getStockByCode >> code: {}", code);
        List<Stock> stockList = stockRepository.findAllByCodeLike(code);
        log.debug("List size received: {}", stockList.size());
        if (!CollectionUtils.isEmpty(stockList)) {
            for (Stock stock : stockList) {
                stockDTOList.add(toDto(stock));
            }
        }
        return stockDTOList;
    }

    private StockDTO toDto(Stock stock) {
        return StockDTO.builder()
                .id(stock.getId())
                .pcs(stock.getPcs())
                .huid(stock.getHuid())
                .name(stock.getName())
                .saleMC(stock.getSaleMC())
                .stnType(stock.getStnType())
                .stockStatus(stock.getStockStatus())
                .active(stock.isActive())
                .code(stock.getCode())
                .stnCostPerCt(stock.getStnCostPerCt())
                .weight(stock.getWeight())
                .itemType(stock.getItemType())
                .purity(stock.getPurity())
                .stnWeight(stock.getStnWeight())
                .vaWeight(stock.getVaWeight())
                .build();
    }
}
