package co.com.powerup2025.r2dbc.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="solicitud",schema = "loan")
public class LoanEntity {

    @Id
    @Column("id_solicitud")
    private Long idSolicitud;

    @Column("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column("codigo_solicitud")
    private String codigoSolicitud;

    @Column("monto")
    private BigDecimal monto;

    @Column("plazo")
    private Integer plazo;

    @Column("email")
    private String email;

    @Column("id_estado")
    private Long idEstado;

    @Column("id_tipo_prestamo")
    private Integer idTipoPrestamo;
}
