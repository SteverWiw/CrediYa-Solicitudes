package co.com.powerup2025.api.response_dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SolicitudResponseDTO", description = "Respuesta de la creación de la solicitud")
public record SolicitudResponseDTO(

        @Schema(description = "Identificador único de la solicitud", example = "2025-00000001")
        String solicitudId,

        @Schema(description = "Estado actual de la solicitud", example = "Pendiente de revisión")
        String estado,

        @Schema(description = "Mensaje adicional de confirmación o error", example = "Solicitud registrada correctamente")
        String mensaje
) {}
