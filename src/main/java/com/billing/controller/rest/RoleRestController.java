package com.billing.controller.rest;

import com.billing.entity.Permission;
import com.billing.entity.Role;
import com.billing.repository.RoleRepository;
import com.billing.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleRestController {

    @Autowired
    private RoleService roleService; // Inject RoleService

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.ok(createdRole);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id); // Set the id to be updated
        Role updatedRole = roleService.updateRole(role);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("all")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }
}



    // GET all roles

//
//    // GET role by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
//        Optional<Role> optionalRole = roleRepository.findById(id);
//        return optionalRole.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // POST create a new role
//    @PostMapping
//    public ResponseEntity<Role> createRole(@RequestBody Role role) {
//        Role savedRole = roleRepository.save(role);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
//    }
//
//    // PUT update an existing role
//    @PutMapping("/{id}")
//    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
//        if (!roleRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        role.setId(id); // Ensure the ID from path variable is set in the role object
//        Role updatedRole = roleRepository.save(role);
//        return ResponseEntity.ok(updatedRole);
//    }
//
//    // DELETE role by ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
//        if (!roleRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        roleRepository.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // POST add permissions to a role
//    @PostMapping("/{roleId}/permissions/add")
//    public ResponseEntity<Role> addPermissionsToRole(@PathVariable Long roleId, @RequestBody List<Permission> permissions) {
//        Optional<Role> optionalRole = roleRepository.findById(roleId);
//        if (optionalRole.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        Role role = optionalRole.get();
//        for (Permission permission : permissions) {
//            role.addRolePermission(permission);
//        }
//        roleRepository.save(role); // Save the updated role with new permissions
//        return ResponseEntity.ok(role);
//    }
//
//    // POST remove permissions from a role
//    @PostMapping("/{roleId}/permissions/remove")
//    public ResponseEntity<Role> removePermissionsFromRole(@PathVariable Long roleId, @RequestBody List<Permission> permissions) {
//        Optional<Role> optionalRole = roleRepository.findById(roleId);
//        if (optionalRole.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        Role role = optionalRole.get();
//        for (Permission permission : permissions) {
//            role.removeRolePermission(permission);
//        }
//        roleRepository.save(role); // Save the updated role after removing permissions
//        return ResponseEntity.ok(role);
//    }
//}
