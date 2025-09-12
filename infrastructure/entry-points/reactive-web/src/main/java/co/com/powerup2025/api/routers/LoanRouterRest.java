package co.com.powerup2025.api.routers;

import co.com.powerup2025.api.dtos.request.LoanRequest;
import co.com.powerup2025.api.dtos.response.LoanResponse;
import co.com.powerup2025.api.handler.LoanHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class LoanRouterRest {
    @Bean

    @RouterOperations(value = {
            @RouterOperation(path = "/crediYa/api/v1/solicitud/crear", method = RequestMethod.POST, beanClass = LoanHandler.class, beanMethod = "crearteLoan", operation = @Operation(operationId = "crearteLoan", tags = {
                    "Solicitud"}, summary = "Crear una nueva solicitud", description = "Crea una nueva solicitud en el sistema", requestBody = @RequestBody(required = true, description = "Datos de la solicitud a crear", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanRequest.class))), responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanResponse.class))),
                    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }))
    })


    public RouterFunction<ServerResponse> routerFunction(LoanHandler handler) {
        return RouterFunctions
                .nest(RequestPredicates.path("/crediYa/api/v1/solicitud"),
                        RouterFunctions
                                .route()
                                .POST("/crear", handler::crearteLoan)
                                .GET("/listByState", handler::listarPorEstado)
                                .build());
    }
}
