package byzas.io.microservice.service2.client;

import byzas.io.microservice.service2.config.webclient.WebClientsConfig;
import byzas.io.microservice.service2.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class EmployeeServiceClient {

    public static String targetService = "employee-service";

    @Autowired
    WebClientsConfig webClientsConfig;


    public CompletableFuture<List<Employee>> findByDepartment(Long departmentId) {
        return webClientsConfig.getWebClient(targetService)
                .get()
                .uri(String.format("/department/%s", departmentId))
                .retrieve()
                .bodyToMono(Employee[].class)
                .map(departmentArray -> Arrays.asList(departmentArray))
                .onErrorResume(RuntimeException.class, t->{
                    log.warn("No access to microservice");
                    return Mono.justOrEmpty(new ArrayList<Employee>());
                })
                .toFuture();
    }

}
