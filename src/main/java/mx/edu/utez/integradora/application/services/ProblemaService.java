package mx.edu.utez.integradora.application.services;

import mx.edu.utez.integradora.domain.entities.Problema;
import mx.edu.utez.integradora.domain.entities.Proveedor;
import mx.edu.utez.integradora.domain.entities.User;
import mx.edu.utez.integradora.infrastructure.repository.ProblemaRepository;
import mx.edu.utez.integradora.infrastructure.repository.ProveedorRepository;
import mx.edu.utez.integradora.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemaService {

    @Autowired
    private ProblemaRepository problemaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Problema> getProblemasPorUsuario(Integer usuarioId) {
        return problemaRepository.findByUsuarioId(usuarioId);
    }

    public Problema crearProblema(Problema problema) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String emailUsuario = userDetails.getUsername();
        User usuario = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        problema.setUsuario(usuario);
        problema.setEstado(Problema.EstadoProblema.ABIERTO);
        return problemaRepository.save(problema);
    }

    public Problema actualizarProblema(Integer problemaId, Problema datosProblema) {
        Problema problema = problemaRepository.findById(problemaId)
                .orElseThrow(() -> new RuntimeException("Problema no encontrado"));

        problema.setTitulo(datosProblema.getTitulo());
        problema.setDescripcion(datosProblema.getDescripcion());
        problema.setFotografia(datosProblema.getFotografia());
        problema.setEstado(datosProblema.getEstado());
        return problemaRepository.save(problema);
    }

    public List<Problema> getTodosLosProblemas() {
        return problemaRepository.findAll();  // Obtener todos los problemas
    }

    public List<Problema> getProblemasPorCategoria(String categoria) {
        try {
            Problema.CategoriaProblema categoriaEnum = Problema.CategoriaProblema.valueOf(categoria.toUpperCase());
            return problemaRepository.findByCategoria(categoriaEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Categoría no válida: " + categoria);
        }
    }

    public Problema asignarProblemaComoProveedor(Integer problemaId, Authentication authentication) {
        String email = authentication.getName();
        Proveedor proveedor = proveedorRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        Problema problema = problemaRepository.findById(problemaId)
                .orElseThrow(() -> new RuntimeException("Problema no encontrado"));

        problema.setProveedor(proveedor);
        return problemaRepository.save(problema);
    }
}
