package byzas.io.microservice.apigateway.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Setter
@NoArgsConstructor @AllArgsConstructor
public class AuthServiceResponse {
    private Object data;
    private boolean status;
    private String errorMessage;
}