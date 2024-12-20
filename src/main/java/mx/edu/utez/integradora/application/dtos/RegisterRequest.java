package mx.edu.utez.integradora.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.integradora.domain.entities.Role;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    String name;
    String firstSurname;
    String secondSurname;
    String phone;
    String email;
    String password;
    String verificationCode;
    boolean isVerified = false;
    Role role;
    private String photo; // Campo para la foto codificada en Base64
    private List<String> addresses; // Lista de direcciones proporcionadas por el usuario
}
