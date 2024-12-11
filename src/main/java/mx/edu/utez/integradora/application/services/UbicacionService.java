package mx.edu.utez.integradora.application.services;

import mx.edu.utez.integradora.application.dtos.UbicacionDTO;
import mx.edu.utez.integradora.domain.entities.Ubicacion;
import mx.edu.utez.integradora.domain.entities.User;
import mx.edu.utez.integradora.infrastructure.repository.UbicacionRepository;
import mx.edu.utez.integradora.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private UserRepository userRepository;


    public UbicacionDTO convertToDTO(Ubicacion ubicacion) {
        return UbicacionDTO.builder()
                .id(ubicacion.getId())
                .direccion(ubicacion.getDireccion())
                .latitud(ubicacion.getLatitud())
                .longitud(ubicacion.getLongitud())
                .idUsuario(ubicacion.getUsuario() != null ? ubicacion.getUsuario().getId() : null)
                .idProveedor(ubicacion.getProveedor() != null ? ubicacion.getProveedor().getId() : null)
                .build();
    }


    public Ubicacion agregarUbicacion(Ubicacion ubicacion, Integer usuarioId) {
        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (ubicacionRepository.countByUsuario(usuario) >= 1) {
            throw new RuntimeException("No se pueden agregar más de 1 ubicacion, MODIFICACION EN VIVO");
        }

        ubicacion.setUsuario(usuario);
        return ubicacionRepository.save(ubicacion);
    }

    public List<UbicacionDTO> convertToDTOList(List<Ubicacion> ubicaciones) {
        return ubicaciones.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Ubicacion> obtenerUbicacionesPorUsuario(Integer usuarioId) {
        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ubicacionRepository.findByUsuario(usuario);
    }

    // Método para eliminar una ubicación por su ID
    public void eliminarUbicacion(Integer ubicacionId) {
        Ubicacion ubicacion = ubicacionRepository.findById(ubicacionId)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
        ubicacionRepository.delete(ubicacion);
    }

}
