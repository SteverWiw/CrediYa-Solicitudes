package co.com.powerup2025.model.solicitudes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitudes {

        private Long idSolicitud;

        private LocalDateTime fechaCreacion;

        private String codigoSolicitud;

        private BigDecimal monto;

        private Integer plazo;

        private String email;

        private Long idEstado;

        private Integer idTipoPrestamo;
}
