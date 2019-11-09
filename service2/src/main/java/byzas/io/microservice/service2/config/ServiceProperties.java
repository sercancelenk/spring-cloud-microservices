package byzas.io.microservice.service2.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ServiceProperties {
    @Value("${spring.application.name}")
    private String serviceName;
}
