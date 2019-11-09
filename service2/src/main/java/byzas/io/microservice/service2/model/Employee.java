package byzas.io.microservice.service2.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class Employee {

    private Long id;
    private String name;
    private int age;
    private String position;
}