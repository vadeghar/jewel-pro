package com.billing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permission")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Column(name="`desc`", nullable = false, length = 2000)
    private String desc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_group_id", nullable = true, referencedColumnName = "id")
    @JsonIgnore
    private PermissionGroup permissionGroup;

    @Transient
    private Long permissionGroupId;
    @Transient
    private String permissionGroupName;

    @ManyToMany(fetch = FetchType.EAGER) // Eager fetching ensures permissions are loaded with role
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @JsonIgnore
    private Set<Role> roles;

//    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<RolePermissions> rolePermissions = new HashSet<>();

    // Constructors, getters, setters, etc.

    // Method to add RolePermission
//    public void addRolePermission(Role role) {
//        RolePermissions rolePermission = new RolePermissions(null, role, this);
//        rolePermissions.add(rolePermission);
//        role.getRolePermissions().add(rolePermission);
//    }
//
//    // Method to remove RolePermission
//    public void removeRolePermission(Role role) {
//        for (Iterator<RolePermissions> iterator = rolePermissions.iterator(); iterator.hasNext();) {
//            RolePermissions rolePermission = iterator.next();
//            if (rolePermission.getPermission().equals(this) && rolePermission.getRole().equals(role)) {
//                iterator.remove();
//                rolePermission.getRole().getRolePermissions().remove(rolePermission);
//                rolePermission.setPermission(null);
//                rolePermission.setRole(null);
//            }
//        }
//    }
}
