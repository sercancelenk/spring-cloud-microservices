package byzas.io.microservice.apigateway.util;

import byzas.io.microservice.apigateway.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class GateWayResponse {
    public static Mono<Void> onError(ServerWebExchange exchange, ApiResponse apiResponse, HttpStatus httpStatus) {

        ObjectMapper objectMapper = new ObjectMapper();
        String responseJsonString = "";
        try {
            responseJsonString = objectMapper.writeValueAsString(apiResponse);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        ServerHttpResponse response = (ServerHttpResponse) exchange.getResponse();
        response.setStatusCode(httpStatus);
        byte[] bytes = responseJsonString.getBytes(StandardCharsets.UTF_8);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
