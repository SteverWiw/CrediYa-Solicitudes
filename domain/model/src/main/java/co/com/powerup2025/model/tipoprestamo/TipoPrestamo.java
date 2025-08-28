package co.com.powerup2025.model.tipoprestamo;
import lombok.Builder;
import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TipoPrestamo {

    private Integer idTipoPrestamo;
    private String nombre;
    private BigDecimal montoMinimo;
    private BigDecimal montoMaximo;
    private BigDecimal tasaInteres;
    private Boolean validacionAutomatica;
    private Integer plazoMaximo;
    private Integer plazoMinimo;

}
