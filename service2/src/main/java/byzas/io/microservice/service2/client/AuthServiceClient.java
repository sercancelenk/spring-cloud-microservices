package byzas.io.microservice.service2.client;

import byzas.io.microservice.service2.config.ServiceProperties;
import byzas.io.microservice.service2.config.webclient.WebClientsConfig;
import byzas.io.microservice.service2.model.AuthServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class AuthServiceClient {
    public static String targetService = "auth-service";

    @Autowired
    ServiceProperties serviceProperties;

    @Autowired
    WebClientsConfig webClientsConfig;


    public CompletableFuture<AuthServiceResponse> checkServiceAccess(String destinationService) {
        return webClientsConfig.getWebClient(targetService)
                .get()
                .uri(String.format("/app/%s/type/microservice_access/service/%s", serviceProperties.getServiceName(), destinationService))
                .retrieve()
                .bodyToMono(AuthServiceResponse.class)
                .onErrorResume(e-> Mono.just(AuthServiceResponse.builder().errorMessage(e.getMessage()).build()))
                .toFuture();
    }
}
