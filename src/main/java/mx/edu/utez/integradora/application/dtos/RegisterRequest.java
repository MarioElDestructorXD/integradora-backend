package mx.edu.utez.integradora.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
