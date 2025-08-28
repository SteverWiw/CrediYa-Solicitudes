package co.com.powerup2025.consumer;

import co.com.powerup2025.consumer.api.UsuariosApi;
import co.com.powerup2025.consumer.mapper.UsuarioMapper;
import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.usuario.Usuario;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer /* implements Gateway from domain */{
    private final WebClient client;
    private final UsuariosApi usuariosApi;
    private final UsuarioMapper mapper;


    // these methods are an example that illustrates the implementation of WebClient.
    // You should use the methods that you implement from the Gateway from the domain.
    @CircuitBreaker(name = "getUserByemail", fallbackMethod = "getUserByemailFallback")
    public Mono<Usuario> getUserByemail(String email) {
        return usuariosApi.getUserRequest(email)
                .map(mapper::toModel);
    }

    public Mono<Usuario> getUserByEmailFallback(String email, Throwable ex) {
        return Mono.error(new BusinessException(ErrorCode.INT_001));
    }
}
