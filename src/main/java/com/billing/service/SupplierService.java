package com.billing.service;

import com.billing.dto.Error;
import com.billing.dto.ErrorResponse;
import com.billing.entity.Supplier;
import com.billing.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.billing.constant.ErrorCode.DATA_ERROR_ITEM;

@Service
@Slf4j
public class SupplierService {
    private final SupplierRepository supplierRepository;
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier save(Supplier supplier) {
        log.debug("Saving Item {}", supplier);
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    public Supplier update(Long id, Supplier supplier) {
        log.debug("Saving Item id {} with data {}", id, supplier);
        Supplier supplierDb = supplierRepository.getReferenceById(id);
        if (supplierDb != null) {
            BeanUtils.copyProperties(supplier, supplierDb);
            log.debug("Saving to db supplier {}", supplierDb);
            return supplierRepository.save(supplierDb);
        } else {
            new EntityNotFoundException("Supplier not found with id "+id);
        }
        return null;
    }

    public void delete(Long id) {
        log.debug("Deleting supplier id {}", id);
        supplierRepository.deleteById(id);
    }

    public List<Supplier> getSuppliersNameLike(String like) {
        return supplierRepository.findByNameContainingIgnoreCase(like);
    }

    public ErrorResponse validateSupplier(Supplier supplier) {
        log.debug("Validating supplier {}", supplier);
        List<Error> errors = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse(errors, LocalDateTime.now(), DATA_ERROR_ITEM.getCode());
        
        log.debug("Exiting validate supplier, Found errors: {}", errorResponse.getErrors().size());
        return errorResponse;
    }
}
