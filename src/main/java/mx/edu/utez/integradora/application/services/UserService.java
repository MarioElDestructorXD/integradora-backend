package mx.edu.utez.integradora.application.services;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.integradora.domain.entities.User;
import mx.edu.utez.integradora.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // Obtener información del usuario por su email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Actualizar la información del usuario
    public User updateUser(User existingUser, User updatedUser) {
        existingUser.setName(updatedUser.getName());
        existingUser.setFirstSurname(updatedUser.getFirstSurname());
        existingUser.setSecondSurname(updatedUser.getSecondSurname());
        existingUser.setPhone(updatedUser.getPhone());
        return userRepository.save(existingUser);
    }
}
