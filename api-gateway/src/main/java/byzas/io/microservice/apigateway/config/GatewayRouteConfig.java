package byzas.io.microservice.apigateway.config;

import byzas.io.microservice.apigateway.filter.AuthorizationCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;

@Configuration
@RequiredArgsConstructor
public class GatewayRouteConfig {
    private final AuthorizationCheckFilter authorizationCheckFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder
                .routes()
                .route(
                        route -> route.path("/employee/**")
                                .filters(f ->
                                        f.rewritePath("/employee/(?<path>.*)", "/${path}")
                                                .filter(authorizationCheckFilter.apply("employee-service", new AuthorizationCheckFilter.NameConfig()), -1)

                                )
                                .uri("lb://employee-service/")
                                .id("employee-service")
                )
                .route(
                        route -> route.path("/department/**")
                                .filters(f ->
                                        f.rewritePath("/department/(?<path>.*)", "/${path}")
                                                .filter(authorizationCheckFilter.apply("department-service", new AuthorizationCheckFilter.NameConfig()), -1)
                                )
                                .uri("lb://department-service/")
                                .id("department-service")
                )
                .route(
                        route -> route.path("/organization/**")
                                .filters(f ->
                                        f.rewritePath("/organization/(?<path>.*)", "/${path}")
                                                .filter(authorizationCheckFilter.apply("organization-service", new AuthorizationCheckFilter.NameConfig()), -1)
                                )
                                .uri("lb://organization-service/")
                                .id("organization-service")
                )

                .build();
    }
}
