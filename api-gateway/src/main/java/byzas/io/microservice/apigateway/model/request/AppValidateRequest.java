package byzas.io.microservice.apigateway.model.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AppValidateRequest {
    private String appId;
    private String username;
    private String password;
    private String token;
}
