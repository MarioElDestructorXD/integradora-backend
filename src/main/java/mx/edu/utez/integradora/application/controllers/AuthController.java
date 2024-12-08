package mx.edu.utez.integradora.application.controllers;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.integradora.application.dtos.AuthResponse;
import mx.edu.utez.integradora.application.dtos.UserProfileDto;
import mx.edu.utez.integradora.application.services.AuthService;
import mx.edu.utez.integradora.application.dtos.LoginRequest;
import mx.edu.utez.integradora.application.dtos.RegisterRequest;
import mx.edu.utez.integradora.application.services.JwtService;
import mx.edu.utez.integradora.application.services.TokenBlacklistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping(value = "register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        System.out.println("Solicitud de registro: " + request);
        return authService.register(request);
    }

    @GetMapping("verify")
    public ResponseEntity<?> verify(@RequestParam("code") String code) {
        return authService.verifyUser(code);
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        tokenBlacklistService.revokeToken(token);
        return ResponseEntity.ok("Sesión cerrada exitosamente.");
    }
}
