package mx.edu.utez.integradora.application.controllers;

import mx.edu.utez.integradora.domain.entities.Problema;
import mx.edu.utez.integradora.application.services.ProblemaService;
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

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Problema>> obtenerProblemasPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(problemaService.getProblemasPorUsuario(usuarioId));
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
}
