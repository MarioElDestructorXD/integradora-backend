package mx.edu.utez.integradora.login.auth;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.integradora.login.entity.Role;
import mx.edu.utez.integradora.login.entity.User;
import mx.edu.utez.integradora.login.jwt.JwtService;
import mx.edu.utez.integradora.login.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationManager authenticationManager;


    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .firstSurname(request.getFirstSurname())
                .secondSurname(request.getSecondSurname())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}
