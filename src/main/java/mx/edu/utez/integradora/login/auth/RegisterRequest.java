package mx.edu.utez.integradora.login.auth;

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
