package mx.edu.utez.integradora.infrastructure.repository;

import mx.edu.utez.integradora.domain.entities.Problema;
import mx.edu.utez.integradora.domain.entities.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    Optional<Proveedor> findByCorreo(String correo);

}
