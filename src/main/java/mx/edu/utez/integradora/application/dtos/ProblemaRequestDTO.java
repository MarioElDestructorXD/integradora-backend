package mx.edu.utez.integradora.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemaRequestDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String fotografia; // Base64
    private Integer ubicacionId; // ID de la ubicación seleccionada
}
