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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .role(((User) user).getRole()) // Incluye el rol en la respuesta
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        Role role = request.getRole() != null ? request.getRole() : Role.USER; // Default a USER si no se especifica un rol
        User user = User.builder()
                .name(request.getName())
                .firstSurname(request.getFirstSurname())
                .secondSurname(request.getSecondSurname())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}
