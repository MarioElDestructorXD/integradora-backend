package mx.edu.utez.integradora.application.services;

import mx.edu.utez.integradora.domain.entities.Proveedor;
import mx.edu.utez.integradora.infrastructure.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    public Proveedor registrarProveedor(Proveedor proveedor) {
        if (proveedorRepository.findByCorreo(proveedor.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }
        // Encriptar contraseña antes de guardar
        proveedor.setContraseña(passwordEncoder.encode(proveedor.getContraseña()));
        return proveedorRepository.save(proveedor);
    }

    public Proveedor obtenerProveedorPorId(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
    }
}
