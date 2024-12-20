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

    @PutMapping("/{problemaId}/aceptar")
    public ResponseEntity<Problema> aceptarProblema(@PathVariable Integer problemaId, Authentication authentication) {
        try {
            // Obtener el usuario autenticado
            String username = authentication.getName();

            // Llamar al servicio para asignar el problema al proveedor
            Problema problema = problemaService.asignarProblemaAProveedor(problemaId, username);

            return ResponseEntity.ok(problema);
        } catch (SecurityException e) {
            // Si el usuario no tiene el rol de proveedor
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (IllegalArgumentException e) {
            // Si el problema no existe o ya está asignado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Manejo de otros errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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