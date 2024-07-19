package com.billing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 25, unique = true)
    private String name;

    @Column(name="`desc`", nullable = false, length = 2000)
    private String desc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_group_id", nullable = true, referencedColumnName = "id")
    @JsonIgnore
    private PermissionGroup permissionGroup;

    @Transient
    private Long permissionGroupId;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermissions> rolePermissions = new HashSet<>();

    // Constructors, getters, setters, etc.

    // Method to add RolePermission
    public void addRolePermission(Role role) {
        RolePermissions rolePermission = new RolePermissions(null, role, this);
        rolePermissions.add(rolePermission);
        role.getRolePermissions().add(rolePermission);
    }

    // Method to remove RolePermission
    public void removeRolePermission(Role role) {
        for (Iterator<RolePermissions> iterator = rolePermissions.iterator(); iterator.hasNext();) {
            RolePermissions rolePermission = iterator.next();
            if (rolePermission.getPermission().equals(this) && rolePermission.getRole().equals(role)) {
                iterator.remove();
                rolePermission.getRole().getRolePermissions().remove(rolePermission);
                rolePermission.setPermission(null);
                rolePermission.setRole(null);
            }
        }
    }
}
