package net.javaguides.identity_service.repository;

import net.javaguides.identity_service.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByName(String name);

    Role findByName(String name);

    List<Role> findAllByNameContaining(String name);
}