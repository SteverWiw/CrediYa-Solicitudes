package co.com.powerup2025.model.loans;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Loan {

        private Long idSolicitud;

        private LocalDateTime fechaCreacion;

        private String codigoSolicitud;

        private BigDecimal monto;

        private Integer plazo;

        private String email;

        private Long idEstado;

        private Integer idTipoPrestamo;
}
