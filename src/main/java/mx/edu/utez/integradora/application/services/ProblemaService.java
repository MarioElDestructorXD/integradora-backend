package mx.edu.utez.integradora.application.services;

import mx.edu.utez.integradora.domain.entities.Problema;
import mx.edu.utez.integradora.domain.entities.Proveedor;
import mx.edu.utez.integradora.domain.entities.Ubicacion;
import mx.edu.utez.integradora.domain.entities.User;
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


    public Problema crearProblema(Problema problema, Double latitud, Double longitud) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String emailUsuario = userDetails.getUsername();
        User usuario = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear la ubicación con latitud y longitud
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(latitud);
        ubicacion.setLongitud(longitud);
        ubicacion.setUsuario(usuario);

        // Guardar la ubicación antes de asociarla al problema
        ubicacionRepository.save(ubicacion);

        problema.setUsuario(usuario);
        problema.setUbicacion(ubicacion);
        problema.setEstado(Problema.EstadoProblema.ABIERTO);

        // Almacenar también en Firebase
        almacenarUbicacionEnFirebase(problema);

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
