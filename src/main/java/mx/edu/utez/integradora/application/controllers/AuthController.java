package mx.edu.utez.integradora.application.controllers;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.integradora.application.dtos.AuthResponse;
import mx.edu.utez.integradora.application.dtos.UserProfileDto;
import mx.edu.utez.integradora.application.services.AuthService;
import mx.edu.utez.integradora.application.dtos.LoginRequest;
import mx.edu.utez.integradora.application.dtos.RegisterRequest;
import mx.edu.utez.integradora.application.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping(value = "register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("verify")
    public ResponseEntity<?> verify(@RequestParam("code") String code) {
        return authService.verifyUser(code);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        try {
            String userEmail = jwtService.extractUsername(token.replace("Bearer ", ""));
            UserProfileDto profile = authService.getUserProfileByEmail(userEmail);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al extraer el perfil. Verifica el token proporcionado.");
        }
    }
}
