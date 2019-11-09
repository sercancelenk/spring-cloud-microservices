package byzas.io.microservice.apigateway.model;

import lombok.*;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class ApiResponse {

    private String status;
    private String message;
}