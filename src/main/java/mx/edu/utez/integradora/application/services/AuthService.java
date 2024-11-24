package mx.edu.utez.integradora.application.services;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.integradora.application.dtos.AuthResponse;
import mx.edu.utez.integradora.application.dtos.LoginRequest;
import mx.edu.utez.integradora.application.dtos.RegisterRequest;
import mx.edu.utez.integradora.domain.entities.Role;
import mx.edu.utez.integradora.domain.entities.User;
import mx.edu.utez.integradora.infrastructure.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    public AuthResponse login(LoginRequest request) {
        // Valida el inicio de sesión y verifica si el usuario está confirmado
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.isVerified()) {
            throw new RuntimeException("Por favor verifica tu correo electrónico antes de iniciar sesión.");
        }

        // Continuar con el flujo normal de generación de token
        String token = jwtService.getToken(user);

        return AuthResponse.builder().token(token).role(user.getRole()).build();
    }

    public AuthResponse register(RegisterRequest request) {
        String verificationCode = UUID.randomUUID().toString(); // Generar código único

        User user = User.builder().name(request.getName()).firstSurname(request.getFirstSurname()).secondSurname(request.getSecondSurname()).phone(request.getPhone()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).verificationCode(verificationCode).isVerified(false).role(request.getRole()).build();

        userRepository.save(user);

        // Enviar correo de verificación
        String verificationLink = "http://localhost:8080/auth/verify?code=" + verificationCode;
        emailService.sendVerificationEmail(user.getEmail(), "Verifica tu correo", "Por favor haz clic en el siguiente enlace para verificar tu cuenta: " + verificationLink);

        return AuthResponse.builder().token("Usuario registrado. Verifica tu correo electrónico para activar tu cuenta.").role(null).build();
    }

    public String verifyUser(String code) {
        Optional<User> userOptional = userRepository.findByVerificationCode(code);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isVerified()) {
                return "El usuario ya está verificado.";
            }
            user.setVerified(true);
            user.setVerificationCode(null); // Limpiar el código de verificación
            userRepository.save(user);
            return "Usuario verificado con éxito.";
        }
        return "Código de verificación inválido.";
    }

}
