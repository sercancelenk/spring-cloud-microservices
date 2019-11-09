package byzas.io.microservice.authservice.service;

import byzas.io.microservice.authservice.domain.AppPermission;
import byzas.io.microservice.authservice.extensions.CryptoSupport;
import byzas.io.microservice.authservice.model.AppValidateRequest;
import byzas.io.microservice.authservice.model.PermissionType;
import byzas.io.microservice.authservice.repository.AppPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AuthService implements CryptoSupport {
    private final AppPermissionRepository appPermissionRepository;

    public CompletableFuture<Boolean> checkAppToken(AppValidateRequest appValidateRequest) {
        return CompletableFuture.completedFuture(appValidateRequest)
                .thenApply(request -> {
                    String calculatedToken = encrypt(request.getAppId(), request.getUsername(), request.getPassword());
                    return StringUtils.equals(calculatedToken, request.getToken());
                });
    }

    public CompletableFuture<List<AppPermission>> getAppPermissions(String appId, PermissionType permissionType) {
        return appPermissionRepository.findByAppIdAndPermissionType(appId, permissionType);
    }

    public CompletableFuture<Boolean> checkAppPermissionForService(String appId, PermissionType permissionType, String service) {
        return appPermissionRepository.countByAppIdAndPermissionTypeAndPermissionVal(appId, permissionType, service)
                .thenApply(result -> result > 0);
    }

    public CompletableFuture<Void> revokePermission(String appId, PermissionType permissionType, String service) {

        return CompletableFuture.allOf(appPermissionRepository.findDistinctByAppIdAndPermissionTypeAndPermissionVal(appId, permissionType, service)
                .thenApply(appPermission -> {
                    appPermissionRepository.delete(appPermission);
                    return true;
                }));
    }

    public CompletableFuture<Void> grantPermission(String appId, PermissionType permissionType, String service) {

        return CompletableFuture.allOf(appPermissionRepository.findDistinctByAppIdAndPermissionTypeAndPermissionVal(appId, permissionType, service)
                .thenApply(appPermission -> {
                    if (appPermission == null) {
                        addPermission(appId, permissionType, service);
                    }
                    return true;
                }));
    }

    private CompletableFuture<Boolean> addPermission(String appId, PermissionType permissionType, String service) {
        appPermissionRepository.save(AppPermission.builder().appId(appId).permissionType(permissionType).permissionVal(service).build());
        return CompletableFuture.completedFuture(true);
    }

    public static void main(String[] args) {
        AuthService x = new AuthService(null);
        System.out.println(x.encrypt("FIZY", "fizyapp", "123"));;

    }

}
