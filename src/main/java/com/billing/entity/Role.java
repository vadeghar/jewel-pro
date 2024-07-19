package com.billing.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermissions> rolePermissions = new HashSet<>();

    // Constructors, getters, setters, etc.

    // Method to add RolePermission
    public void addRolePermission(Permission permission) {
        RolePermissions rolePermission = new RolePermissions(null,this, permission);
        rolePermissions.add(rolePermission);
        permission.getRolePermissions().add(rolePermission);
    }

    // Method to remove RolePermission
    public void removeRolePermission(Permission permission) {
        for (Iterator<RolePermissions> iterator = rolePermissions.iterator(); iterator.hasNext();) {
            RolePermissions rolePermission = iterator.next();
            if (rolePermission.getRole().equals(this) && rolePermission.getPermission().equals(permission)) {
                iterator.remove();
                rolePermission.getPermission().getRolePermissions().remove(rolePermission);
                rolePermission.setRole(null);
                rolePermission.setPermission(null);
            }
        }
    }

}
