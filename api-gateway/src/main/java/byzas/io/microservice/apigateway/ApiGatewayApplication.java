package byzas.io.microservice.apigateway;

import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Autowired RedisServer redisServer;

	@PostConstruct
	public void start() {

		log.info("starting redis...");
		if (!redisServer.isActive()) redisServer.start();
		log.info("redis listen ports: {}", redisServer.ports().stream()
				.map(Object::toString).collect(Collectors.joining(",")));
	}

	@PreDestroy
	public void stop() {

		log.info("shutting down redis...");
		redisServer.stop();
		log.info("bye!");
	}
}
