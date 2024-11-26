package mx.edu.utez.integradora.infrastructure.repository;

import mx.edu.utez.integradora.domain.entities.Role;
import mx.edu.utez.integradora.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);
    List<User> findByRole(Role role);
    List<User> findByIsVerifiedFalse();
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.name LIKE %:query% OR u.email LIKE %:query%")
    List<User> searchByNameOrEmail(@Param("query") String query);
}
