package mx.edu.utez.integradora.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF para APIs RESTful
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilitar CORS
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin sesiones
                .authenticationProvider(authProvider)
                .authorizeHttpRequests(authRequest -> authRequest
                        .requestMatchers("/auth/login", "/auth/register", "/auth/verify").permitAll() // Permitir acceso sin autenticación
                        // Permitir acceso libre a los endpoints de autenticación
                        .requestMatchers("/api/menu").authenticated() // Requiere autenticación
                        .requestMatchers("/api/problemas/**").authenticated()
                        .requestMatchers("/api/proveedores/**").authenticated()
                        .requestMatchers("/api/repair-post/**").authenticated()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/api/ubicaciones/**").permitAll()
                        .requestMatchers("/api/problemas/categorias").permitAll()
//                        .requestMatchers("/api/problemas/**").permitAll()
                        .anyRequest().authenticated()) // Cualquier otra solicitud requiere autenticación
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Filtro JWT antes de la autenticación básica
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://52.86.242.121:8084, http://52.86.242.121:8080")); // Dominios permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Headers permitidos
        configuration.setAllowCredentials(true); // Permitir credenciales

        // Permitir todos los orígenes
        configuration.addAllowedOriginPattern("*"); // Cambiar a un origen específico en producción

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers permitidos
        configuration.setAllowedHeaders(List.of("*")); // Permite todos los encabezados

        // Permitir credenciales (cookies, encabezados de autenticación, etc.)
        configuration.setAllowCredentials(true);

        // Aplicar configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
