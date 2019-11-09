package byzas.io.microservice.service2.config.webclient;

import byzas.io.microservice.service2.filter.SecurityFilterFunctions;
import com.netflix.discovery.converters.Auto;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Profile("service-clients")
@Configuration
@EnableConfigurationProperties(WebClientProperties.class)
public class WebClientsConfig {

    @Autowired
    @LoadBalanced
    public WebClient.Builder defaultLoadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Autowired WebClient.Builder defaultWebClientBuilder;

    @Autowired
    WebClientProperties webClientProperties;

//    @Autowired
//    BlockingLoadBalancerClient loadBalancerClient;

    @Autowired ReactorLoadBalancerExchangeFilterFunction lbFunction;

    private Map<String, WebClient> webClients = new ConcurrentHashMap<>();

    @PostConstruct
    public void initialize() {
        webClientProperties.getServices().entrySet()
                .stream()
                .forEach(entry -> {
                    String service = entry.getKey();
                    WebClientProperties.WebClientService serviceProperties = entry.getValue();

                    HttpClient httpClient = HttpClient.create()
                            .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientProperties.getCommons().getConnectTimeoutMillis()))
                            .tcpConfiguration(client -> client.option(ChannelOption.SO_KEEPALIVE, webClientProperties.getCommons().isSoKeepAlive()))
                            .tcpConfiguration(client ->
                                    client.doOnConnected(conn -> conn
                                            .addHandlerLast(new ReadTimeoutHandler(webClientProperties.getCommons().getSocketReadTimeoutmillis() / 1000))
                                            .addHandlerLast(new WriteTimeoutHandler(webClientProperties.getCommons().getSocketWriteTimeoutMillis() / 1000))));
                    ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
                    WebClient.Builder webClientBuilder = defaultLoadBalancedWebClientBuilder().clone().filter(lbFunction);

                    if(serviceProperties.isSecure()) webClientBuilder.filter(SecurityFilterFunctions.checkAccessFunction(service));

                    WebClient webClient = webClientBuilder
                            .clientConnector(connector)
                            .baseUrl(serviceProperties.getBaseUrl())
                            .build();

                    webClients.putIfAbsent(service, webClient);
                });
    }

    public WebClient getWebClient(String serviceName) {
//        WebClient.Builder clientBuilder = webClients.get(serviceName);
//        ServiceInstance instance = loadBalancerClient.choose(serviceName);
//        clientBuilder.baseUrl(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
//        return clientBuilder.build();
        return webClients.get(serviceName);
    }
}