package co.com.powerup2025.consumer.api.model;

import java.util.Objects;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* Usuario Response DTO
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Integer idUsuario = null;
    private String nombre = null;
    private String apellido = null;
    private String email = null;
    private Long documentoIdentidad = null;
    private String telefono = null;
    private Integer idRol = null;
    private BigDecimal salarioBase = null;
}