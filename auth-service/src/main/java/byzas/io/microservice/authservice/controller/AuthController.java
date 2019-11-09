package byzas.io.microservice.authservice.controller;

import byzas.io.microservice.authservice.model.AppValidateRequest;
import byzas.io.microservice.authservice.model.AuthResponse;
import byzas.io.microservice.authservice.model.PermissionType;
import byzas.io.microservice.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor @Slf4j
public class AuthController {
    private final AuthService authService;

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @PostMapping("validate")
    public CompletableFuture<AuthResponse> validate(@RequestBody AppValidateRequest appValidateRequest) {
        return authService.checkAppToken(appValidateRequest)
                .thenApply(result -> AuthResponse.builder().data(result).status(true).build());
    }

    @GetMapping("app/{appId}/type/{permissionType}/permissions")
    public CompletableFuture<AuthResponse> getAppPermissions(@PathVariable(name = "appId") String appId, @PathVariable(name = "permissionType") PermissionType permissionType) {
        return authService.getAppPermissions(appId, permissionType)
                .thenApply(result -> AuthResponse.builder().data(result).status(true).build());
    }

    @GetMapping("app/{appId}/type/{permissionType}/service/{service}")
    public CompletableFuture<AuthResponse> getAppPermissions(@PathVariable(name = "appId") String appId,
                                                             @PathVariable(name = "permissionType") PermissionType permissionType,
                                                             @PathVariable(name = "service") String service) {
        log.info("INSTANCE ID {}", instanceId);
        return authService.checkAppPermissionForService(appId, permissionType, service)
                .thenApply(result -> AuthResponse.builder().data(result).status(true).build());
    }

    @GetMapping({"app/{appId}/type/{permissionType}/service/{service}/permission/grant"})
    public CompletableFuture<AuthResponse> givePermission(@PathVariable(name = "appId") String appId,
                                                          @PathVariable(name = "permissionType") PermissionType permissionType,
                                                          @PathVariable(name = "service") String service) {
        return authService.grantPermission(appId, permissionType, service)
                .thenApply(result -> AuthResponse.builder().data(result).status(true).build());
    }

    @GetMapping({"app/{appId}/type/{permissionType}/service/{service}/permission/revoke"})
    public CompletableFuture<AuthResponse> revokePermission(@PathVariable(name = "appId") String appId,
                                                            @PathVariable(name = "permissionType") PermissionType permissionType,
                                                            @PathVariable(name = "service") String service) {
        return authService.revokePermission(appId, permissionType, service)
                .thenApply(result -> AuthResponse.builder().data(result).status(true).build());
    }


}
