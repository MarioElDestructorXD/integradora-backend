package mx.edu.utez.integradora.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO {
    private Integer id;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Integer idUsuario;  // Referencia al ID del usuario asociado
    private Integer idProveedor;  // Referencia al ID del proveedor asociado (opcional)
}
