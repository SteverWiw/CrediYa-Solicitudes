package co.com.powerup2025.r2dbc.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tipo_prestamo",schema = "loan")
public class LoanTypeEntity {

    @Id
    @Column("id_tipo_prestamo")
    private Integer idTipoPrestamo;

    @Column("nombre")
    private String nombre;

    @Column("monto_minimo")
    private BigDecimal montoMinimo;

    @Column("monto_maximo")
    private BigDecimal montoMaximo;

    @Column("tasa_interes")
    private BigDecimal tasaInteres;

    @Column("validacion_automatica")
    private Boolean validacionAutomatica;

    @Column("plazo_maximo")
    private Integer plazoMaximo;

    @Column("plazo_minimo")
    private Integer plazoMinimo;
}
