package byzas.io.microservice.apigateway.filter;

import byzas.io.microservice.apigateway.model.ApiResponse;
import byzas.io.microservice.apigateway.model.ApiStatus;
import byzas.io.microservice.apigateway.model.request.AppValidateRequest;
import byzas.io.microservice.apigateway.model.request.AuthServiceResponse;
import byzas.io.microservice.apigateway.util.GateWayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class AuthorizationCheckFilter extends AbstractGatewayFilterFactory<AuthorizationCheckFilter.NameConfig> {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public AuthorizationCheckFilter() {
        super(NameConfig.class);
    }

    //    @Override
    public GatewayFilter apply(NameConfig config) {
        return null;
    }

    @Override
    public GatewayFilter apply(String routeId, NameConfig config) {

        return (exchange, chain) -> {

            log.info("Auth filter applied");

            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey("APP_ID") ||
                    !request.getHeaders().containsKey("APP_USERNAME") ||
                    !request.getHeaders().containsKey("APP_PASSWORD") ||
                    !request.getHeaders().containsKey("APP_TOKEN")) {

                return GateWayResponse.onError(exchange, ApiResponse.builder().status(ApiStatus.FAILE).message("No authentication given").build(), HttpStatus.UNAUTHORIZED);
            }
            String appId = request.getHeaders().getFirst("APP_ID");
            String appUsername = request.getHeaders().getFirst("APP_USERNAME");
            String appPassword = request.getHeaders().getFirst("APP_PASSWORD");
            String appToken = request.getHeaders().getFirst("APP_TOKEN");

            Mono<AuthServiceResponse> tokenValidate = webClientBuilder.build()
                    .post()
                    .uri(String.format("http://auth-service/validate"))
                    .body(BodyInserters.fromValue(AppValidateRequest.builder().appId(appId).username(appUsername).password(appPassword).token(appToken).build()))
                    .retrieve()
                    .bodyToMono(AuthServiceResponse.class);

            Mono<AuthServiceResponse> permissionCheck = webClientBuilder.build()
                    .get()
                    .uri(String.format("http://auth-service/app/%s/type/microservice_access/service/%s", appId, routeId))
                    .retrieve()
                    .bodyToMono(AuthServiceResponse.class);

            return tokenValidate
                    .flatMap(tokenValidateResponse -> {
                        boolean tokenValidateResult = (boolean) tokenValidateResponse.getData();
                        if (!tokenValidateResult)
                            return GateWayResponse.onError(exchange, ApiResponse.builder().status(ApiStatus.FAILE).message("No authentication given").build(), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
                        return permissionCheck
                                .flatMap(permissionCheckResponse -> {
                                    boolean permissionCheckResult = (boolean) permissionCheckResponse.getData();
                                    if (!permissionCheckResult)
                                        return GateWayResponse.onError(exchange, ApiResponse.builder().status(ApiStatus.FAILE).message("No authorization to access service").build(), HttpStatus.UNAUTHORIZED);
                                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().
                                            header("RequestID", UUID.randomUUID().toString()).
                                            build();

                                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                                });
                    });
        };
    }

    public static class NameConfig {
        // Put the configuration properties
    }
}
