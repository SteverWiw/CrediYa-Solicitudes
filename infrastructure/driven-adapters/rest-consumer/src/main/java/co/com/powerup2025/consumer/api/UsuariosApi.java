package co.com.powerup2025.consumer.api;

import co.com.powerup2025.consumer.api.model.UsuarioResponseDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import lombok.extern.log4j.Log4j2;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service
public class UsuariosApi {
    private final WebClient client;
    private final ObjectMapper mapper;

    /**
    * Build call for getUser
    * @param email Email del usuario a buscar (required)
    * @return Mono<UsuarioResponseDTO> response
    */
    public Mono<UsuarioResponseDTO> getUserRequest(String email) {
        return client.method(HttpMethod.GET)
                .uri("/crediYa/api/v1/usuarios?email={email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        resp -> resp.bodyToFlux(ErrorResponse.class)
                                .collectList()
                                .flatMap(list -> Mono.error(new RuntimeException(/*list*/)))
                )
                .bodyToMono(UsuarioResponseDTO.class);
    }

}
