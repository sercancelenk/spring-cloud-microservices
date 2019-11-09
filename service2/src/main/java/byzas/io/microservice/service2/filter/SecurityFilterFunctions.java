package byzas.io.microservice.service2.filter;

import byzas.io.microservice.service2.client.AuthServiceClient;
import byzas.io.microservice.service2.config.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;


@Slf4j
public class SecurityFilterFunctions {
    public static ExchangeFilterFunction checkAccessFunction(String destinationService) {

        return (clientRequest, nextFilter) -> {
            log.info("WebClient fitler executed");
            return Mono.fromFuture(SpringContext.applicationContext.getBean(AuthServiceClient.class)
                    .checkServiceAccess(destinationService))
                    .flatMap(response -> {
                        boolean result = (boolean) response.getData();
                        if (result) return nextFilter.exchange(clientRequest);
                        throw new RuntimeException(String.format("You don't have any permission to access %s", destinationService));
                    })
                    .onErrorResume(t -> Mono.error(t));
        };
    }
}
