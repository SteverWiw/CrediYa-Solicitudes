package co.com.powerup2025.consumer;

import org.springframework.stereotype.Service;

import co.com.powerup2025.consumer.api.UsuariosApi;
import co.com.powerup2025.consumer.mapper.UsuarioMapper;
import co.com.powerup2025.model.exception.enums.ErrorModule;
import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.usuario.gateways.UsuarioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UsuarioRepository {
    private final UsuariosApi usuariosApi;
    private final UsuarioMapper mapper;
    private final IntegrationErrorHandler errorHandler;

    @CircuitBreaker(name = "getUserByEmail", fallbackMethod = "getUserByEmailFallback")
    public Mono<Usuario> getUserByEmail(String email) {
        return usuariosApi.getUserRequest(email)
                .map(mapper::toModel);
    }

    public Mono<Usuario> getUserByEmailFallback(String email, Throwable ex) {
        return errorHandler.handleIntegrationError(ex, ErrorModule.USUARIO.name());
    }

}
