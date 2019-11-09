package byzas.io.microservice.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Slf4j
@Configuration
public class EmbeddedRedisConfig {
    @Bean
    public RedisServer redisServer() {
        RedisServer.builder().reset();
        log.info("REDIS SERVER EMBEDDED");
        return RedisServer.builder().port(6379).build();
    }
}
