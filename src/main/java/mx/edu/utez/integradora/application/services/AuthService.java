package mx.edu.utez.integradora.application.services;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.integradora.application.dtos.AuthResponse;
import mx.edu.utez.integradora.application.dtos.LoginRequest;
import mx.edu.utez.integradora.application.dtos.RegisterRequest;
import mx.edu.utez.integradora.application.dtos.UserProfileDto;
import mx.edu.utez.integradora.domain.entities.Role;
import mx.edu.utez.integradora.domain.entities.User;
import mx.edu.utez.integradora.infrastructure.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
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
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (!user.isVerified()) {
            throw new RuntimeException("Por favor verifica tu correo electrónico antes de iniciar sesión.");
        }

        String token = jwtService.getToken(user);

        return AuthResponse.builder().token(token).role(user.getRole()).build();
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        String verificationCode = UUID.randomUUID().toString();

        // Validar que el role sea uno permitido
        if (request.getRole() == null || !isValidRole(request.getRole())) {
            throw new RuntimeException("Rol inválido");
        }

        byte[] photo = null;
        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            try {
                photo = Base64.getDecoder().decode(request.getPhoto());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Formato de foto inválido.");
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
        emailService.sendVerificationEmail(user.getEmail(), "Verifica tu correo",
                "Por favor haz clic en el siguiente enlace para verificar tu cuenta: " + verificationLink);

        return AuthResponse.builder()
                .token("Usuario registrado. Verifica tu correo electrónico para activar tu cuenta.")
                .role(null)
                .build();
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

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public String updateUser(Integer id, RegisterRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setName(request.getName());
        user.setFirstSurname(request.getFirstSurname());
        user.setSecondSurname(request.getSecondSurname());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
        return "Usuario actualizado exitosamente";
    }

    public String deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
        return "Usuario eliminado exitosamente";
    }

    public List<User> getUnverifiedUsers() {
        return userRepository.findByIsVerifiedFalse();
    }

    public List<User> searchUsers(String query) {
        return userRepository.searchByNameOrEmail(query);
    }

    private boolean isValidRole(Role role) {
        return role == Role.PROVEEDOR || role == Role.CLIENTE;
    }

    public UserProfileDto getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Convertir el usuario a un DTO para la respuesta
        return UserProfileDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .firstSurname(user.getFirstSurname())
                .secondSurname(user.getSecondSurname())
                .phone(user.getPhone())
                .build();
    }
    

}
