package com.billing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permission_group")
public class PermissionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String desc;

    @OneToMany(mappedBy = "permissionGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Permission> permissions;

}

