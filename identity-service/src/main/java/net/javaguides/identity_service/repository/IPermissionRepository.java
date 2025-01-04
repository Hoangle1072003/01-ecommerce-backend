package net.javaguides.identity_service.repository;

import net.javaguides.identity_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, UUID> {
    boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);

    List<Permission> findByIdIn(List<UUID> ids);

    List<Permission> findByModule(String module);
}