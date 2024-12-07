package mx.edu.utez.integradora.infrastructure.repository;

import mx.edu.utez.integradora.domain.entities.Ubicacion;
import mx.edu.utez.integradora.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {

    // Método para obtener todas las ubicaciones de un usuario específico
    List<Ubicacion> findByUsuario(User usuario);

    // Método para contar cuántas ubicaciones tiene un usuario
    long countByUsuario(User usuario);
}
