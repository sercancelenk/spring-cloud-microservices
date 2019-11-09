package byzas.io.microservice.authservice.repository;

import byzas.io.microservice.authservice.domain.AppPermission;
import byzas.io.microservice.authservice.model.PermissionType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AppPermissionRepository extends CrudRepository<AppPermission, Long> {
    CompletableFuture<List<AppPermission>> findByAppIdAndPermissionType(String appId, PermissionType permissionType);
    CompletableFuture<Integer> countByAppIdAndPermissionTypeAndPermissionVal(String appId, PermissionType permissionType, String permissionVal);
    CompletableFuture<AppPermission> findDistinctByAppIdAndPermissionTypeAndPermissionVal(String appId, PermissionType permissionType, String permissionVal);
}
