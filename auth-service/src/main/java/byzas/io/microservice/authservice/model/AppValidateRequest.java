package byzas.io.microservice.authservice.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class AppValidateRequest {
    private String appId;
    private String username;
    private String password;
    private String token;
}
