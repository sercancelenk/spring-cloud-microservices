package byzas.io.microservice.service2.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder @ToString
public class Department {
    private Long id;
    private Long organizationId;
    private String name;
    private List<Employee> employees = new ArrayList<>();

    public Department(Long organizationId, String name){
        this.organizationId = organizationId;
        this.name = name;
    }
}
