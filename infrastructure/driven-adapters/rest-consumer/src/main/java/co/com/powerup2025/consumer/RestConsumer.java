package co.com.powerup2025.consumer;

import co.com.powerup2025.model.exception.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.exception.gateways.LoggerPort;
import org.springframework.stereotype.Service;

import co.com.powerup2025.consumer.api.UsuariosApi;
import co.com.powerup2025.consumer.mapper.UsuarioMapper;
import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
public class RestConsumer implements UsuarioRepository {
    private final UsuariosApi usuariosApi;
    private final UsuarioMapper mapper;
    private final LoggerPort logger;

    public RestConsumer(UsuariosApi usuariosApi, UsuarioMapper mapper, LoggerFactoryPort loggerFactoryPort) {
        this.usuariosApi = usuariosApi;
        this.mapper = mapper;
        this.logger = loggerFactoryPort.getLogger(RestConsumer.class);
    }


    @CircuitBreaker(name = "getUserByEmail", fallbackMethod = "getUserByEmailFallback")
    public Mono<Usuario> getUserByEmail(String email) {
        return usuariosApi.getUserRequest(email)
                .doOnSubscribe(s ->   logger.info("Inicia consumo de microservicio").thenReturn(s))
                .map(mapper::toModel)
                .flatMap(s1 -> logger.info("Finaliza consumo de microservicio").thenReturn(s1));
    }

    public Mono<Usuario> getUserByEmailFallback(String email, Throwable ex) {
        return Mono.error(new Throwable("Error obteniendo user por email: " + ex.getMessage()));
    }

}
