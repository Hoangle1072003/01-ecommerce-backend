package net.javaguides.identity_service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * File: Role.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 9:43 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Table(name = "roles")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "char(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    private Boolean active;

    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"roles"})
    @JoinTable(
            name = "permission_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

//    @PrePersist
//    public void handleBeforeCreate() {
//        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
//        this.createdAt = Instant.now();
//    }
//
//    @PreUpdate
//    public void handleBeforeUpdate() {
//        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
//        this.updatedAt = Instant.now();
//    }


}
