package mx.edu.utez.integradora.application.controllers;

import mx.edu.utez.integradora.application.dtos.ApiResponse;
import mx.edu.utez.integradora.application.dtos.ProblemaRequestDTO;
import mx.edu.utez.integradora.domain.entities.Problema;
import mx.edu.utez.integradora.application.services.ProblemaService;
import mx.edu.utez.integradora.domain.entities.Ubicacion;
import mx.edu.utez.integradora.infrastructure.repository.ProblemaRepository;
import mx.edu.utez.integradora.infrastructure.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private UbicacionRepository ubicacionRepository;

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

    @PostMapping("/post")
    public ResponseEntity<ApiResponse> crearProblema(@RequestBody ProblemaRequestDTO problemaRequest) {
        try {
            Problema problema = new Problema();
            problema.setTitulo(problemaRequest.getTitulo());
            problema.setDescripcion(problemaRequest.getDescripcion());
            problema.setCategoria(Problema.CategoriaProblema.valueOf(problemaRequest.getCategoria().toUpperCase()));
            problema.setFotografia(problemaRequest.getFotografia().getBytes());

            // Agregar ubicación al problema
            Problema nuevoProblema = problemaService.crearProblema(problema, problemaRequest.getUbicacionId());

            return ResponseEntity.ok(new ApiResponse("Problema creado exitosamente", "success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Error al crear el problema: " + e.getMessage(), "error"));
        }
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


    @GetMapping("/categorias")
    public ResponseEntity<List<String>> obtenerCategorias() {
        // Obtener los nombres de las categorías del ENUM
        List<String> categorias = List.of(Problema.CategoriaProblema.values())
                .stream()
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(categorias);
    }


}