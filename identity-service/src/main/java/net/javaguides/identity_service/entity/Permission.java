package net.javaguides.identity_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * File: Permission.java
 * Author: Le Van Hoang
 * Date: 1/1/2025 (01/01/2025)
 * Time: 9:44 PM
 * Version: 1.0
 * <p>
 * Copyright © 2025 Le Van Hoang. All rights reserved.
 */

@Table(name = "permissions")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "char(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "API Path is required")
    private String apiPath;
    @NotBlank(message = "Method is required")
    private String method;
    @NotBlank(message = "Module is required")
    private String module;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;

    public Permission(String name, String apiPath, String method, String module) {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }

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

