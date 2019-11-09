package byzas.io.microservice.service3;

import byzas.io.microservice.service3.model.Organization;
import byzas.io.microservice.service3.repository.OrganizationRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Service3Application {

    public static void main(String[] args) {
        SpringApplication.run(Service3Application.class, args);
    }

    @Bean
    OrganizationRepository repository() {
        OrganizationRepository repository = new OrganizationRepository();
        repository.add(new Organization("Microsoft", "Redmond, Washington, USA"));
        repository.add(new Organization("Oracle", "Redwood City, California, USA"));
        return repository;
    }

}
