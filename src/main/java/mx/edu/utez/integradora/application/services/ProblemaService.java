package mx.edu.utez.integradora.application.services;

import mx.edu.utez.integradora.domain.entities.*;
import mx.edu.utez.integradora.infrastructure.repository.ProblemaRepository;
import mx.edu.utez.integradora.infrastructure.repository.ProveedorRepository;
import mx.edu.utez.integradora.infrastructure.repository.UbicacionRepository;
import mx.edu.utez.integradora.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.api.core.ApiFuture;

import java.util.List;

@Service
public class ProblemaService {

    @Autowired
    private ProblemaRepository problemaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private UbicacionRepository ubicacionRepository;

    public List<Problema> getProblemasPorUsuario(Integer usuarioId) {
        return problemaRepository.findByUsuarioId(usuarioId);
    }

    public class UbicacionProblema {
        private Double latitude;
        private Double longitude;

        public UbicacionProblema(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        // Getters y setters
        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }


    public Problema crearProblema(Problema problema, Integer ubicacionId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String emailUsuario = userDetails.getUsername();
        User usuario = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar la ubicación seleccionada
        Ubicacion ubicacion = ubicacionRepository.findById(ubicacionId)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        // Validar que la ubicación pertenece al usuario autenticado
        if (!ubicacion.getUsuario().equals(usuario)) {
            throw new RuntimeException("La ubicación no pertenece al usuario autenticado");
        }

        problema.setUsuario(usuario);
        problema.setUbicacion(ubicacion);
        problema.setLatitud(ubicacion.getLatitud());
        problema.setLongitud(ubicacion.getLongitud());
        problema.setEstado(Problema.EstadoProblema.ABIERTO);

        return problemaRepository.save(problema);
    }

    private void almacenarUbicacionEnFirebase(Problema problema) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("problemas");

            String problemaId = String.valueOf(problema.getIdProblema());

            // Utilizando ApiFuture para manejar el resultado de la operación asíncrona
            ApiFuture<Void> future = reference.child(problemaId).setValueAsync(problema);

            // Añadiendo callback para manejar el resultado de la operación asíncrona
            future.addListener(() -> {
                try {
                    // Obtener el resultado para asegurarse de que la operación se completó correctamente
                    future.get();
                    System.out.println("Ubicación almacenada en Firebase con éxito.");
                } catch (Exception e) {
                    System.err.println("Error al almacenar la ubicación en Firebase: " + e.getMessage());
                }
            }, Runnable::run);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public Problema asignarProblemaAProveedor(Integer problemaId, String username) {
        // Obtener el usuario autenticado (proveedor)
        User usuarioAutenticado = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("El proveedor no está registrado"));

        // Verificar si el usuario es un proveedor
        if (!usuarioAutenticado.getRole().equals(Role.PROVEEDOR)) {
            throw new SecurityException("Solo los proveedores pueden aceptar problemas.");
        }

        // Buscar el problema
        Problema problema = problemaRepository.findById(problemaId)
                .orElseThrow(() -> new IllegalArgumentException("El problema con ID " + problemaId + " no existe"));

        // Verificar si ya tiene un proveedor asignado
        if (problema.getProveedor() != null) {
            throw new IllegalArgumentException("El problema ya ha sido asignado a un proveedor.");
        }

        // Asignar el proveedor al problema
        problema.setProveedor(usuarioAutenticado);
        problema.setEstado(Problema.EstadoProblema.EN_PROCESO); // Cambiar el estado a 'EN_PROCESO'

        // Guardar el problema actualizado
        return problemaRepository.save(problema);
    }


    public boolean cancelarProblema(Integer problemaId, String username) {
        Problema problema = problemaRepository.findById(problemaId)
                .orElseThrow(() -> new RuntimeException("Problema no encontrado"));

        if (problema.getProveedor() == null ||
                !problema.getProveedor().getUsername().equals(username)) {
            return false;
        }

        problema.setEstado(Problema.EstadoProblema.ABIERTO);
        problema.setProveedor(null);
        problemaRepository.save(problema);
        return true;
    }

    public boolean marcarComoTerminado(Integer problemaId, String username) {
        Problema problema = problemaRepository.findById(problemaId)
                .orElseThrow(() -> new RuntimeException("Problema no encontrado"));

        if (problema.getProveedor() == null ||
                !problema.getProveedor().getUsername().equals(username)) {
            return false; // No autorizado
        }

        problema.setEstado(Problema.EstadoProblema.CERRADO);
        problemaRepository.save(problema);
        return true;
    }


}
