package co.com.powerup2025.consumer;

import co.com.powerup2025.model.logger.LoggerFactoryPort;
import co.com.powerup2025.model.logger.LoggerRepository;
import org.springframework.stereotype.Service;

import co.com.powerup2025.consumer.api.UserApi;
import co.com.powerup2025.consumer.mapper.UserMapper;
import co.com.powerup2025.model.user.User;
import co.com.powerup2025.model.user.gateways.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@Service
public class RestConsumer implements UserRepository {
    private final UserApi userApi;
    private final UserMapper mapper;
    private final LoggerRepository logger;

    public RestConsumer(UserApi userApi, UserMapper mapper, LoggerFactoryPort loggerFactoryPort) {
        this.userApi = userApi;
        this.mapper = mapper;
        this.logger = loggerFactoryPort.getLogger(RestConsumer.class);
    }


    @CircuitBreaker(name = "getUserByEmail", fallbackMethod = "getUserByEmailFallback")
    public Mono<User> getUserByEmail(String email) {
        return userApi.getUserRequest(email)
                .doOnSubscribe(s ->   logger.info("Inicia consumo de microservicio").thenReturn(s))
                .map(mapper::toModel)
                .flatMap(s1 -> logger.info("Finaliza consumo de microservicio").thenReturn(s1));
    }

    public Mono<User> getUserByEmailFallback(String email, Throwable ex) {
        logger.error(String.format("Fallback activado para getUserByEmail. Email: %s, Error: %s", email, ex.getMessage()), ex);

        return Mono.error(new Throwable("Error obteniendo user por email: " + ex.getMessage()));
    }

}
