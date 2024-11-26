package mx.edu.utez.integradora.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuOption {
    private String name;
    private String description;
    private String endpoint;
}
