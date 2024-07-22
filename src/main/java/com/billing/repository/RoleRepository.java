package com.billing.repository;

import com.billing.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @Query(value = "SELECT r.* FROM roles r", nativeQuery = true)
    List<Role> getAll();
}
