package com.billing.controller.rest;

import com.billing.entity.PermissionGroup;
import com.billing.service.PermissionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/permission-groups")
public class PermissionGroupRestController {

    @Autowired
    private PermissionGroupService permissionGroupService;

    @PostMapping
    public ResponseEntity<PermissionGroup> createPermissionGroup(@RequestBody PermissionGroup permissionGroup) {
        PermissionGroup createdPermissionGroup = permissionGroupService.createPermissionGroup(permissionGroup);
        return ResponseEntity.ok(createdPermissionGroup);
    }

    @GetMapping
    public ResponseEntity<List<PermissionGroup>> getAllPermissionGroups() {
        List<PermissionGroup> permissionGroups = permissionGroupService.getAllPermissionGroups();
        return ResponseEntity.ok(permissionGroups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionGroup> getPermissionGroupById(@PathVariable Long id) {
        Optional<PermissionGroup> permissionGroup = permissionGroupService.getPermissionGroupById(id);
        return permissionGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionGroup> updatePermissionGroup(@PathVariable Long id, @RequestBody PermissionGroup permissionGroupDetails) {
        Optional<PermissionGroup> updatedPermissionGroup = permissionGroupService.updatePermissionGroup(id, permissionGroupDetails);
        return updatedPermissionGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermissionGroup(@PathVariable Long id) {
        PermissionGroup deletePermissionGroup = permissionGroupService.getPermissionGroupById(id).orElseThrow();
        boolean isDeleted = false;
        if (deletePermissionGroup.getPermissions().size() == 0) {
            isDeleted = permissionGroupService.deletePermissionGroup(id);
        }
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().body("Permissions attached to this group");
        }
    }
}

