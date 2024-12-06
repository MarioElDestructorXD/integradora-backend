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
    public ResponseEntity<Problema> crearProblema(@RequestBody Problema problema) {
        return ResponseEntity.ok(problemaService.crearProblema(problema));
    }

    @PutMapping("/{problemaId}")
    public ResponseEntity<Problema> actualizarProblema(@PathVariable Integer problemaId,
                                                       @RequestBody Problema datosProblema) {
        return ResponseEntity.ok(problemaService.actualizarProblema(problemaId, datosProblema));
    }

    @PutMapping("/{problemaId}/aceptar")
    public ResponseEntity<Problema> aceptarProblema(@PathVariable Integer problemaId) {
        Problema problema = problemaService.asignarProblemaAProveedor(problemaId);
        return ResponseEntity.ok(problema);
    }

    @PutMapping("/{problemaId}/cancelar")
    public ResponseEntity<String> cancelarProblema(@PathVariable Integer problemaId, Authentication authentication) {
        String username = authentication.getName(); // Usuario logueado
        boolean resultado = problemaService.cancelarProblema(problemaId, username);
        if (resultado) {
            return ResponseEntity.ok("El problema ha sido reabierto y está disponible para otros proveedores.");
        } else {
            return ResponseEntity.badRequest().body("No se pudo cancelar el problema. Verifica si tienes permisos.");
        }
    }

    @PutMapping("/{problemaId}/terminar")
    public ResponseEntity<String> marcarComoTerminado(@PathVariable Integer problemaId, Authentication authentication) {
        String username = authentication.getName(); // Usuario logueado
        boolean resultado = problemaService.marcarComoTerminado(problemaId, username);
        if (resultado) {
            return ResponseEntity.ok("El problema ha sido marcado como cerrado.");
        } else {
            return ResponseEntity.badRequest().body("No se pudo marcar como cerrado. Verifica si tienes permisos.");
        }
    }
}
