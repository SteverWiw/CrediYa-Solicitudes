package co.com.powerup2025.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SolicitudRouterRest {
        @Bean
        /*
         * @RouterOperations({
         * 
         * @RouterOperation(path = "/crediYa/api/v1/usuarios", method =
         * RequestMethod.POST, beanClass = UsuarioHandler.class, beanMethod =
         * "createUser", operation = @Operation(operationId = "createUser", tags = {
         * "Usuarios" }, summary = "Crear un nuevo usuario", description =
         * "Crea un nuevo usuario en el sistema", requestBody = @RequestBody(required =
         * true, description = "Datos del usuario a crear", content = @Content(mediaType
         * = "application/json", schema = @Schema(implementation =
         * co.com.powerup2025.api.request_dto.UsuarioRequestDTO.class))), responses = {
         * 
         * @ApiResponse(responseCode = "201", content = @Content(mediaType =
         * "application/json", schema = @Schema(implementation =
         * co.com.powerup2025.api.response_dto.UsuarioResponseDTO.class))),
         * 
         * @ApiResponse(responseCode = "400", content = @Content(mediaType =
         * "application/json", schema = @Schema(implementation = ErrorResponse.class)))
         * }))
         * })
         */

        public RouterFunction<ServerResponse> routerFunction(SolicitudHandler handler) {
                return RouterFunctions
                                .nest(RequestPredicates.path("/crediYa"),
                                                RouterFunctions
                                                                .route()
                                                                .POST("/api/v1/usuarios", handler::listenPOSTUseCase)
                                                                .build());
        }
}
