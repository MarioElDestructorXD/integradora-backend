package mx.edu.utez.integradora.application.services;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.integradora.application.dtos.AuthResponse;
import mx.edu.utez.integradora.application.dtos.LoginRequest;
import mx.edu.utez.integradora.application.dtos.RegisterRequest;
import mx.edu.utez.integradora.application.dtos.UserProfileDto;
import mx.edu.utez.integradora.domain.entities.Role;
import mx.edu.utez.integradora.domain.entities.User;
import mx.edu.utez.integradora.infrastructure.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // Método para realizar el login
    public ResponseEntity<?> login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Usuario no encontrado. Por favor verifica el correo ingresado.", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ResponseEntity<>("Contraseña incorrecta. Por favor, verifica e intenta nuevamente.", HttpStatus.UNAUTHORIZED);
        }

        if (!user.isVerified()) {
            return new ResponseEntity<>("Cuenta no verificada. Revisa tu correo electrónico para activarla.", HttpStatus.FORBIDDEN);
        }

        String token = jwtService.getToken(user);
        return new ResponseEntity<>(AuthResponse.builder().token(token).role(user.getRole()).build(), HttpStatus.OK);
    }

    // Método para registrar un nuevo usuario
    public ResponseEntity<?> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>("El correo ya está registrado. Por favor utiliza otro correo.", HttpStatus.CONFLICT);
        }

        if (request.getRole() == null || !isValidRole(request.getRole())) {
            return new ResponseEntity<>("Rol inválido. Debes elegir un rol válido: CLIENTE o PROVEEDOR.", HttpStatus.BAD_REQUEST);
        }

        String verificationCode = UUID.randomUUID().toString();

        byte[] photo = null;
        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            try {
                photo = Base64.getDecoder().decode(request.getPhoto());
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>("El formato de la foto no es válido.", HttpStatus.BAD_REQUEST);
            }
        }

        User user = User.builder()
                .name(request.getName())
                .firstSurname(request.getFirstSurname())
                .secondSurname(request.getSecondSurname())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .verificationCode(verificationCode)
                .isVerified(false)
                .role(request.getRole())
                .photo(photo)
                .build();

        userRepository.save(user);

        String verificationLink = "http://localhost:8080/auth/verify?code=" + verificationCode;

        // Envío del correo de verificación
        emailService.sendVerificationEmail(user.getEmail(), verificationLink);

        return new ResponseEntity<>("Usuario registrado correctamente. Verifica tu correo.", HttpStatus.CREATED);
    }

    // Método para verificar un usuario mediante el código de verificación
    public ResponseEntity<?> verifyUser(String code) {
        Optional<User> userOptional = userRepository.findByVerificationCode(code);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Código de verificación inválido. Por favor verifica el enlace.", HttpStatus.BAD_REQUEST);
        }

        User user = userOptional.get();
        if (user.isVerified()) {
            return new ResponseEntity<>("El usuario ya está verificado.", HttpStatus.OK);
        }

        user.setVerified(true);
        user.setVerificationCode(null); // Limpiar el código de verificación
        userRepository.save(user);

        return new ResponseEntity<>("Usuario verificado con éxito.", HttpStatus.OK);
    }

    // Método para obtener el perfil de un usuario mediante el correo
    public UserProfileDto getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return UserProfileDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .firstSurname(user.getFirstSurname())
                .secondSurname(user.getSecondSurname())
                .phone(user.getPhone())
                .build();
    }

    // Validar si el rol es válido
    private boolean isValidRole(Role role) {
        return role == Role.PROVEEDOR || role == Role.CLIENTE;
    }
}
