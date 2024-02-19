package com.billing.service;

import com.billing.entity.StoneMaster;
import com.billing.repository.StoneMasterRepository;
import org.springframework.stereotype.Service;

@Service
public class StoneMasterService {

    private final StoneMasterRepository stoneMasterRepository;

    public StoneMasterService(StoneMasterRepository stoneMasterRepository) {
        this.stoneMasterRepository = stoneMasterRepository;
    }

    public StoneMaster getStoneMasterByName(String stoneName) {
        return stoneMasterRepository.findByStoneName(stoneName);
    }
}
