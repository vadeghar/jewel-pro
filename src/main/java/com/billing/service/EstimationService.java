package com.billing.service;

import com.billing.constant.Metal;
import com.billing.dto.Error;
import com.billing.dto.ErrorResponse;
import com.billing.entity.Estimation;
import com.billing.repository.EstimationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EstimationService {
    private final EstimationRepository estimationRepository;
    public EstimationService(EstimationRepository estimationRepository) {
        this.estimationRepository = estimationRepository;
    }

    public Estimation save(Estimation estimation) {
        log.debug("Saving Item {}", estimation);
        return estimationRepository.save(estimation);
    }

    public List<Estimation> getAll() {
        return estimationRepository.findAll();
    }

    public Estimation update(Long id, Estimation estimation) {
        log.debug("Saving Item id {} with data {}", id, estimation);
        Estimation estimationDb = estimationRepository.getReferenceById(id);
        if (estimationDb != null) {
            BeanUtils.copyProperties(estimation, estimationDb);
            log.debug("Saving to db item {}", estimationDb);
            return estimationRepository.save(estimationDb);
        } else {
            new EntityNotFoundException("Estimation not found with id "+id);
        }
        return null;
    }

    public void delete(Long id) {
        log.debug("Deleting item id {}", id);
        estimationRepository.deleteById(id);
    }

    public ErrorResponse validateEstimation(Estimation estimation) {
        log.debug("Validating estimation {}", estimation);
        List<Error> errors = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse(errors, LocalDateTime.now());
        if(estimation.getRate() == null ||  estimation.getRate() == BigDecimal.ZERO) {
            errorResponse.getErrors().add(new Error("Rate not available.", "Error"));
        }
        if(estimation.getItemMetal() == null) {
            errorResponse.getErrors().add(new Error("Select item metal.", "Error"));
        }
        if(estimation.getPcs() == 0) {
            errorResponse.getErrors().add(new Error("Enter no. of pcs.", "Error"));
        }
        if (estimation.getItemWeight() == null) {
            errorResponse.getErrors().add(new Error("Enter item weight.", "Error"));
        }
        if((estimation.getVaPercentage() == null ||  estimation.getVaPercentage() == BigDecimal.ZERO)
         && (estimation.getVaWeight() == null ||  estimation.getVaWeight() == BigDecimal.ZERO)) {
            errorResponse.getErrors().add(new Error("Enter VA (Wastage) details.", "Error"));
        }
        if(estimation.getStoneWeight() != null && estimation.getStoneWeight() != BigDecimal.ZERO) {
            if(estimation.getStonePricePerCt() == null || estimation.getStonePricePerCt() == BigDecimal.ZERO) {
                errorResponse.getErrors().add(new Error("Enter Stone price per Ct.", "Error"));
            }
        }

//        if(estimation.getItemMetal() == Metal.GOLD) {
//            if((estimation.getGoldRate() == null ||  estimation.getGoldRate() == BigDecimal.ZERO)) {
//                errorResponse.getErrors().add(new Error("Enter gold rate today.", "Error"));
//            }
//        }
//        if(estimation.getItemMetal() == Metal.SILVER) {
//            if((estimation.getSilverRate() == null ||  estimation.getSilverRate() == BigDecimal.ZERO)) {
//                errorResponse.getErrors().add(new Error("Enter silver rate today.", "Error"));
//            }
//        }

        if (StringUtils.isBlank(estimation.getDefaultMcEnabled())) {
            errorResponse.getErrors().add(new Error("Select YES/NO for auto calc for MC", "Error"));
        }
        if (StringUtils.isBlank(estimation.getIsGstEstimation())) {
            errorResponse.getErrors().add(new Error("Select YES/NO for GST calc.", "Error"));
        }

        log.debug("Exiting validate estimation, Found errors: {}", errorResponse.getErrors().size());
        return errorResponse;
    }
}
