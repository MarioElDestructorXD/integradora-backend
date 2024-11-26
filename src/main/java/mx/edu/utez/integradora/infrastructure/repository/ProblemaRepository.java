package mx.edu.utez.integradora.infrastructure.repository;

import mx.edu.utez.integradora.domain.entities.Problema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemaRepository extends JpaRepository<Problema, Integer> {
    List<Problema> findByUsuarioId(Integer usuarioId);
    List<Problema> findByProveedorId(Integer proveedorId);
}
