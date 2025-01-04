package net.javaguides.identity_service.repository;

import net.javaguides.identity_service.domain.User;
import net.javaguides.identity_service.utils.constant.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * File: null.java
 * Author: Le Van Hoang
 * Date: 12/27/2024 (27/12/2024)
 * Time: 9:56 AM
 * Version: 1.0
 * <p>
 * Copyright © 2024 Le Van Hoang. All rights reserved.
 */

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findByRefreshTokenAndEmail(String token, String email);

    Page<User> findAllByRole_NameNot(String roleName, Pageable pageable);


    public Page<User> findAllByRoleName(Pageable pageable, String roleName);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.role.name = :roleName")
    Optional<User> findByIdAndRoleName(@Param("id") UUID id, @Param("roleName") String roleName);


    @Query("SELECT u FROM User u WHERE u.id = :id")
    Page<User> findByIdWithPagination(@Param("id") UUID id, Pageable pageable);

    //    @Query("SELECT u FROM User u WHERE u.role IS NULL AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    @Query("SELECT u FROM User u " +
            "WHERE u.role.name = 'USER' AND " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<User> searchUsersByRoleNameIsUser(@Param("keyword") String keyword);


    @Query("SELECT u FROM User u " +
            "LEFT JOIN u.role r " +
            "WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR :keyword IS NULL) " +
            "AND (:roleId IS NULL OR r.id = :roleId) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "AND (r.name != 'USER')"
    )
    List<User> findByNameOrEmailOrStatusOrRoleId(@Param("keyword") String keyword,
                                                 @Param("status") StatusEnum status,
                                                 @Param("roleId") UUID roleId);

//    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createTime ASC")
//    Page<Order> findOrdersByUserIdSortedByCreateTime(UUID userId, Pageable pageable);
}
