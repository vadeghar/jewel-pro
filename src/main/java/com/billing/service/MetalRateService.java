package com.billing.service;

import com.billing.constant.Metal;
import com.billing.dto.Error;
import com.billing.dto.ErrorResponse;
import com.billing.entity.MetalRate;
import com.billing.repository.MetalRateRepository;
import com.billing.utils.BillingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.billing.constant.ErrorCode.DATA_ERROR_METAL_RATE;

@Service
@Slf4j
public class MetalRateService {
    private final MetalRateRepository metalRateRepository;
    public MetalRateService(MetalRateRepository metalRateRepository) {
        this.metalRateRepository = metalRateRepository;
    }

    public MetalRate save(MetalRate metalRate) {
        log.debug("Saving Item {}", metalRate);
        metalRate.setLastUpdateAt(LocalDateTime.now());
        metalRate.setLastUpdatedBy(BillingUtils.getCurrentUsername());
        return metalRateRepository.save(metalRate);
    }

    public List<MetalRate> getAll() {
        return metalRateRepository.findAll();
    }

    public MetalRate update(Long id, MetalRate metalRate) {
        log.debug("Saving Item id {} with data {}", id, metalRate);
        MetalRate metalRateDb = metalRateRepository.getReferenceById(id);
        if (metalRateDb != null) {
            BeanUtils.copyProperties(metalRate, metalRateDb);
            log.debug("Saving to db item {}", metalRateDb);
            return metalRateRepository.save(metalRateDb);
        } else {
            new EntityNotFoundException("MetalRate not found with id "+id);
        }
        return null;
    }

    public void delete(Long id) {
        log.debug("Deleting item id {}", id);
        metalRateRepository.deleteById(id);
    }

    public BigDecimal getRate(Metal metal) {
        Optional<MetalRate> metalRateOptional = metalRateRepository.findTopByItemMetalOrderByLastUpdateAtDesc(metal);
        if (metalRateOptional.isPresent()) {
            MetalRate metalRate = metalRateOptional.get();
            log.debug("RATE for {} : {}", metal, metalRate.getRate());
            return metalRate.getRate();
        }
        return null;
    }

    public ErrorResponse validateMetalRate(MetalRate metalRate) {
        log.debug("Validating metalRate {}", metalRate);
        List<Error> errors = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse(errors, LocalDateTime.now(), DATA_ERROR_METAL_RATE.getCode());
        if (metalRate.getItemMetal() == null) {
            errorResponse.getErrors().add(new Error("Select metal.", "Error"));
        }
        if((metalRate.getRate() == null ||  metalRate.getRate() == BigDecimal.ZERO)) {
            errorResponse.getErrors().add(new Error("Enter rate.", "Error"));
        }
        log.debug("Exiting validate metalRate, Found errors: {}", errorResponse.getErrors().size());
        return errorResponse;
    }
}
