package mx.edu.utez.integradora.application.controllers;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.integradora.application.dtos.UserProfileDto;
import mx.edu.utez.integradora.application.services.JwtService;
import mx.edu.utez.integradora.application.services.UserService;
import mx.edu.utez.integradora.domain.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    // Obtener perfil de usuario
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Concatenamos el nombre completo
            String fullName = user.getName() + " " + (user.getFirstSurname() != null ? user.getFirstSurname() : "")
                    + (user.getSecondSurname() != null ? " " + user.getSecondSurname() : "");

            // Creamos el DTO y asignamos el nombre completo junto con otros datos
            UserProfileDto userProfileDto = UserProfileDto.builder()
                    .name(fullName)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .firstSurname(user.getFirstSurname())
                    .secondSurname(user.getSecondSurname())
                    .phone(user.getPhone())
                    .build();

            return ResponseEntity.ok(userProfileDto);
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }


    // Actualizar perfil de usuario
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
        String email = jwtService.extractUsername(token.replace("Bearer ", ""));
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            User savedUser = userService.updateUser(existingUser, updatedUser);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }
}
