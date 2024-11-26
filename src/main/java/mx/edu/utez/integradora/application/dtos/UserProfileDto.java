package mx.edu.utez.integradora.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.integradora.domain.entities.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private String name;
    private String email;
    private Role role;
    private String firstSurname;
    private String secondSurname;
    private String phone;
}
