package com.billing.service;

import com.billing.entity.Role;
import com.billing.exception.RoleAlreadyExistsException;
import com.billing.exception.RoleNotFoundException;
import com.billing.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(Role role) throws RoleAlreadyExistsException {
        Optional<Role> existingRole = roleRepository.findByName(role.getName());
        if (existingRole.isPresent()) {
            throw new RoleAlreadyExistsException("Role with name '" + role.getName() + "' already exists");
        }
        return roleRepository.save(role);
    }

    public Role getRoleById(Long id) throws RoleNotFoundException {
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new RoleNotFoundException("Role with ID " + id + " not found");
        }
        return role.get();
    }

    public Role updateRole(Role role) throws RoleNotFoundException {
        if (!roleRepository.existsById(role.getId())) {
            throw new RoleNotFoundException("Role with ID " + role.getId() + " not found");
        }
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) throws RoleNotFoundException {
        if (!roleRepository.existsById(id)) {
            throw new RoleNotFoundException("Role with ID " + id + " not found");
        }
        roleRepository.deleteById(id);
    }

    public List<Role> findAll() {
        return roleRepository.getAll();
    }
}
