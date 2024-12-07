package mx.edu.utez.integradora.application.controllers;

import mx.edu.utez.integradora.application.services.UbicacionService;
import mx.edu.utez.integradora.domain.entities.Ubicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    // Endpoint para agregar una nueva ubicación para un usuario
    @PostMapping("/{userId}")
    public ResponseEntity<Ubicacion> agregarUbicacion(@RequestBody Ubicacion ubicacion, @PathVariable Integer userId) {
        Ubicacion nuevaUbicacion = ubicacionService.agregarUbicacion(ubicacion, userId);
        return ResponseEntity.ok(nuevaUbicacion);
    }

    // Endpoint para obtener todas las ubicaciones de un usuario
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Ubicacion>> obtenerUbicacionesPorUsuario(@PathVariable Integer userId) {
        List<Ubicacion> ubicaciones = ubicacionService.obtenerUbicacionesPorUsuario(userId);
        return ResponseEntity.ok(ubicaciones);
    }

    // Endpoint para eliminar una ubicación
    @DeleteMapping("/{ubicacionId}")
    public ResponseEntity<String> eliminarUbicacion(@PathVariable Integer ubicacionId) {
        ubicacionService.eliminarUbicacion(ubicacionId);
        return ResponseEntity.ok("Ubicación eliminada exitosamente");
    }
}
