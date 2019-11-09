package byzas.io.microservice.service2.config.webclient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Profile("service-clients")
@Component
@ConfigurationProperties("webclient")
@Getter
@Setter
public class WebClientProperties {
    private WebClientCommon commons;
    private Map<String, WebClientService> services;

    @Getter @Setter
    public static class WebClientCommon {
        private int connectTimeoutMillis;
        private int socketReadTimeoutmillis;
        private int socketWriteTimeoutMillis;
        private boolean soKeepAlive;
    }

    @Getter @Setter
    public static class WebClientService {
        private boolean secure;
        private String baseUrl;
    }
}