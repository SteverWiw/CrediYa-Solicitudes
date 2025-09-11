package co.com.powerup2025.consumer.config;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.aspectj.weaver.tools.Trace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import org.springframework.security.oauth2.jwt.Jwt;


import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
public class RestConsumerConfig {

    private final String url;

    private final int timeout;

    private final Tracer tracer;

    public RestConsumerConfig(@Value("${adapter.restconsumer.url}") String url,
                              @Value("${adapter.restconsumer.timeout}") int timeout, Tracer tracer) {
        this.url = url;
        this.timeout = timeout;
        this.tracer = tracer;
    }

    @Bean
    public WebClient getWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .clientConnector(getClientHttpConnector())
                .filter((request, next) -> Mono.deferContextual(ctx ->
                        ReactiveSecurityContextHolder.getContext()
                                .map(securityContext -> {
                                    Jwt jwt = (Jwt) securityContext.getAuthentication().getCredentials();
                                    String token = jwt.getTokenValue();
                                    System.out.printf("token: %s\n", token);
                                    String traceId = tracer.currentSpan().context().traceId();
                                    String spanId = tracer.currentSpan().context().spanId();
                                    String traceparent = String.format("00-%s-%s-01", traceId, spanId);

                                    return ClientRequest.from(request)
                                            .header("traceparent", traceparent)
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                            .build();
                                })
                                .flatMap(next::exchange)
                ))
                .build();
    }


    private ClientHttpConnector getClientHttpConnector() {
        /*
        IF YO REQUIRE APPEND SSL CERTIFICATE SELF SIGNED: this should be in the default cacerts trustore
        */
        return new ReactorClientHttpConnector(HttpClient.create()
                .compress(true)
                .keepAlive(true)
                .option(CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
                }));
    }

}
