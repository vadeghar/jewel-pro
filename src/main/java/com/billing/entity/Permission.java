package com.billing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(nullable = false, length = 2000)
    private String desc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_group_id", nullable = false, referencedColumnName = "id")
    @JsonIgnore
    private PermissionGroup permissionGroup;
}
