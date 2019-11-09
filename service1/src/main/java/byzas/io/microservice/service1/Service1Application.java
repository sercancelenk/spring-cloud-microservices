package byzas.io.microservice.service1;

import byzas.io.microservice.service1.model.Employee;
import byzas.io.microservice.service1.repository.EmployeeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class Service1Application {
    public static void main(String[] args) {
        SpringApplication.run(Service1Application.class, args);
    }

    @Bean
    EmployeeRepository repository() {
        EmployeeRepository repository = new EmployeeRepository();
        repository.add(new Employee(1L, 1L, "John Smith", 34, "Analyst"));
        repository.add(new Employee(1L, 1L, "Darren Hamilton", 37, "Manager"));
        repository.add(new Employee(1L, 1L, "Tom Scott", 26, "Developer"));
        repository.add(new Employee(1L, 2L, "Anna London", 39, "Analyst"));
        repository.add(new Employee(1L, 2L, "Patrick Dempsey", 27, "Developer"));
        repository.add(new Employee(2L, 3L, "Kevin Price", 38, "Developer"));
        repository.add(new Employee(2L, 3L, "Ian Scott", 34, "Developer"));
        repository.add(new Employee(2L, 3L, "Andrew Campton", 30, "Manager"));
        repository.add(new Employee(2L, 4L, "Steve Franklin", 25, "Developer"));
        repository.add(new Employee(2L, 4L, "Elisabeth Smith", 30, "Developer"));
        return repository;
    }
}
