package byzas.io.microservice.authservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class AuthResponse {
    private Object data;
    private boolean status;
    private String errorMessage;
}
