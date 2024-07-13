package com.billing.service;

import com.billing.entity.Permission;
import com.billing.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission get(Long id) {
       return permissionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Permission Not found"));
    }

    public List<Permission> getAll() {
        return permissionRepository.findAll();
    }

    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission update(Permission permission, Long permissionId) {
        Permission permission1 = permissionRepository.findById(permissionId).orElseThrow(() -> new EntityNotFoundException("Permission Not found"));
        permission1.setDesc(permission.getDesc());
        permission1.setName(permission.getName());
        return permissionRepository.save(permission1);
    }

    public void delete(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new EntityNotFoundException("Permission Not found"));
        permissionRepository.delete(permission);
    }
}
