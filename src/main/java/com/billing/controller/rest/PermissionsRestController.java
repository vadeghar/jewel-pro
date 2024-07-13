package com.billing.controller.rest;

import com.billing.entity.Permission;
import com.billing.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/permissions")
public class PermissionsRestController {

    private final PermissionService permissionService;

    public PermissionsRestController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission createdPermission = permissionService.save(permission);
        return ResponseEntity.ok(createdPermission);
    }

    // Get all Permissions
    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAll();
        return ResponseEntity.ok(permissions);
    }

    // Get a single Permission by ID
    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Permission permission = permissionService.get(id);
        return ResponseEntity.ok(permission);
    }

    // Update an existing Permission by ID
    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permissionDetails) {
        Permission updatedPermission = permissionService.update(permissionDetails, id);
        return ResponseEntity.ok(updatedPermission);
    }

    // Delete a Permission by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
//        if (isDeleted) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }
}
