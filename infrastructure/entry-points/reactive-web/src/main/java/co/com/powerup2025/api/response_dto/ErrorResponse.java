package co.com.powerup2025.api.response_dto;

import co.com.powerup2025.model.exception.gateways.iErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse", description = "Estructura de la respuesta de error")
public record ErrorResponse(
        @Schema(description = "Código de error", example = "USR_002") String code,
        @Schema(description = "Mensaje de error", example = "Error de validación") String message,
        @Schema(description = "Módulo", example = "user-service") String module,
        @Schema(description = "Severidad del error", example = "HIGH") String severity,
        @Schema(description = "Identificador de la traza", example = "123e4567-e89b-12d3-a456-426614174000") String traceId) {

    public ErrorResponse(iErrorCode error, String traceId) {
        this(error.code(), error.message(), error.module().name(), error.severity().name(), traceId);
    }

}
