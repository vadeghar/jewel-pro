package com.billing.service;

import com.billing.entity.PermissionGroup;
import com.billing.repository.PermissionGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionGroupService {

    @Autowired
    private PermissionGroupRepository permissionGroupRepository;

    public PermissionGroup createPermissionGroup(PermissionGroup permissionGroup) {
        return permissionGroupRepository.save(permissionGroup);
    }

    public List<PermissionGroup> getAllPermissionGroups() {
        return permissionGroupRepository.findAll();
    }

    public Optional<PermissionGroup> getPermissionGroupById(Long id) {
        return permissionGroupRepository.findById(id);
    }

    public Optional<PermissionGroup> updatePermissionGroup(Long id, PermissionGroup permissionGroupDetails) {
        return permissionGroupRepository.findById(id).map(permissionGroup -> {
            permissionGroup.setName(permissionGroupDetails.getName());
//            permissionGroup.setPermissions(permissionGroupDetails.getPermissions());
            return permissionGroupRepository.save(permissionGroup);
        });
    }

    public boolean deletePermissionGroup(Long id) {
        if (permissionGroupRepository.existsById(id)) {
            permissionGroupRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

