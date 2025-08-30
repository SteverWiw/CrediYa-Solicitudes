package co.com.powerup2025.api.response_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SolicitudResponseDTO", description = "Respuesta de la creación de la solicitud")
public class SolicitudResponseDTO {

        @Schema(description = "Identificador único de la solicitud", example = "2025-00000001")
        private String solicitudId;

        @Schema(description = "Estado actual de la solicitud", example = "Pendiente de revisión")
        private String estado;

        @Schema(description = "Mensaje adicional de confirmación o error", example = "Solicitud registrada correctamente")
        private String mensaje;
}
