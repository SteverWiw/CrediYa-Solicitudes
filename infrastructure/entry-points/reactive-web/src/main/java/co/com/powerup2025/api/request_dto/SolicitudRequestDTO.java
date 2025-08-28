package co.com.powerup2025.api.request_dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SolicitudRequestDTO", description = "Solicitud Request DTO", requiredProperties = { "monto",
                "plazo", "email", "idTipoPrestamo" })
public record SolicitudRequestDTO(
                @Schema(description = "Monto del préstamo", example = "500000", minimum = "30000000", maximum = "500000000", required = true) BigDecimal monto,

                @Schema(description = "Plazo en meses para pagar el préstamo", example = "12", minimum = "6", maximum = "60", required = true) Integer plazo,

                @Schema(description = "Email del solicitante", example = "ejemplo@ejemplo.com", format = "email", required = true) String email,

                @Schema(description = "ID del tipo de préstamo", example = "1", required = true) Long idTipoPrestamo) {

}
