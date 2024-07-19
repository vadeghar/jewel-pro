package com.billing.service;

import com.billing.entity.Permission;
import com.billing.repository.PermissionGroupRepository;
import com.billing.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionGroupRepository permissionGroupRepository;

    public PermissionService(PermissionRepository permissionRepository, PermissionGroupRepository permissionGroupRepository) {
        this.permissionRepository = permissionRepository;
        this.permissionGroupRepository = permissionGroupRepository;
    }

    public Permission get(Long id) {
       return permissionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Permission Not found"));
    }

    public List<Permission> getAll() {
        return permissionRepository.findAll();
    }

    public Permission save(Permission permission) {
        if (permission.getPermissionGroupId() != null) {
            permission.setPermissionGroup(permissionGroupRepository.findById(permission.getPermissionGroupId()).orElse(null));
        }
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
