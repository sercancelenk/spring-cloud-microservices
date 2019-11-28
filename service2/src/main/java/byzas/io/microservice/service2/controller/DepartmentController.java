package byzas.io.microservice.service2.controller;

import byzas.io.microservice.service2.client.EmployeeServiceClient;
import byzas.io.microservice.service2.model.Department;
import byzas.io.microservice.service2.model.Employee;
import byzas.io.microservice.service2.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;


@RestController
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    /*@Autowired
    DepartmentRepository repository;
    */
    
    private DepartmentRepository repository;
    
    @Autowired 
    public DepartmentController(DepartmentRepository repository){
        
        this.repository = repository;
    }
    


    @PostMapping("/")
    public Department add(@RequestBody Department department) {
        LOGGER.info("Department add: {}", department);
        return repository.add(department);
    }

    @GetMapping("/{id}")
    public Department findById(@PathVariable("id") Long id) {
        LOGGER.info("Department find: id={}", id);
        return repository.findById(id);
    }

    @GetMapping("/")
    public List<Department> findAll() {
        LOGGER.info("Department find");
        return repository.findAll();
    }

    @GetMapping("/organization/{organizationId}")
    public List<Department> findByOrganization(@PathVariable("organizationId") Long organizationId) {
        LOGGER.info("Department find: organizationId={}", organizationId);
        return repository.findByOrganization(organizationId);
    }

    @Autowired
    EmployeeServiceClient employeeServiceClient;

    @GetMapping("/organization/{organizationId}/with-employees")
    public CompletableFuture<List<Department>> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId) {
        LOGGER.info("Department find: organizationId={}", organizationId);

        return CompletableFuture.completedFuture(repository.findByOrganization(organizationId))
                .thenCompose(departments -> {
                    Map<Department, CompletableFuture<List<Employee>>> mapDepartments = departments.stream()
                            .collect(Collectors.toMap(Function.identity(), dp -> employeeServiceClient.findByDepartment(dp.getId())));

                    CompletableFuture<Void> merged = CompletableFuture.allOf(mapDepartments.values().toArray(new CompletableFuture[0]))
                            .exceptionally(e -> null);

                    return merged
                            .thenApply(any -> {
                                return mapDepartments.entrySet()
                                        .stream()
                                        .map((entry) -> {
                                            Department d = entry.getKey();
                                            List<Employee> employees = entry.getValue()
                                                    .exceptionally(e -> new ArrayList<>()).join();
                                            return Department.builder().id(d.getId()).name(d.getName()).organizationId(d.getOrganizationId())
                                                    .employees(employees).build();
                                        }).collect(Collectors.toList());
                            });
                });


    }

}
