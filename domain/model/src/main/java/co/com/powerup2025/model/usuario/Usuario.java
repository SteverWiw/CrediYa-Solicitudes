package co.com.powerup2025.model.usuario;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private Long documentoIdentidad;
    private String telefono;
    private Integer idRol;
    private java.math.BigDecimal salarioBase;
}
