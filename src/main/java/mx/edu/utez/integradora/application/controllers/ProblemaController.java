package mx.edu.utez.integradora.application.controllers;

import mx.edu.utez.integradora.domain.entities.Problema;
import mx.edu.utez.integradora.application.services.ProblemaService;
import mx.edu.utez.integradora.infrastructure.repository.ProblemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problemas")
public class ProblemaController {

    @Autowired
    private ProblemaService problemaService;
    @Autowired
    private ProblemaRepository problemaRepository;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Problema>> obtenerProblemasPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(problemaService.getProblemasPorUsuario(usuarioId));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Problema>> obtenerProblemasPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(problemaService.getProblemasPorCategoria(categoria));
    }

    @GetMapping
    public ResponseEntity<List<Problema>> obtenerTodosLosProblemas() {
        return ResponseEntity.ok(problemaService.getTodosLosProblemas());
    }

    @PostMapping
    public ResponseEntity<Problema> crearProblema(
            @RequestBody Problema problema,
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
        return ResponseEntity.ok(problemaService.crearProblema(problema, latitud, longitud));
    }




    @PutMapping("/{problemaId}")
    public ResponseEntity<Problema> actualizarProblema(@PathVariable Integer problemaId,
                                                       @RequestBody Problema datosProblema) {
        return ResponseEntity.ok(problemaService.actualizarProblema(problemaId, datosProblema));
    }

    @PutMapping("/{problemaId}/asignar")
    public ResponseEntity<Problema> asignarProblemaComoProveedor(@PathVariable Integer problemaId, Authentication authentication) {
        return ResponseEntity.ok(problemaService.asignarProblemaComoProveedor(problemaId, authentication));
    }
}