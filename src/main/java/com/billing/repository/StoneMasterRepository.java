package com.billing.repository;

import com.billing.entity.StoneMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoneMasterRepository extends JpaRepository<StoneMaster, Long> {

    StoneMaster findByStoneName(String stoneName);
}
