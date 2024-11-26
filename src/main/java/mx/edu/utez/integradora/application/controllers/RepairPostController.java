package mx.edu.utez.integradora.application.controllers;

import mx.edu.utez.integradora.application.services.RepairPostService;
import mx.edu.utez.integradora.domain.entities.RepairPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair-post")
public class RepairPostController {

    @Autowired
    private RepairPostService repairPostService;

    // Obtener todas las publicaciones
    @GetMapping
    public ResponseEntity<List<RepairPost>> getAllRepairPost() {
        return ResponseEntity.ok(repairPostService.getAllRepairPost());
    }

    // Obtener una publicación por ID
    @GetMapping("/{id}")
    public ResponseEntity<RepairPost> getRepairPostById(@PathVariable Integer id) {
        return repairPostService.getRepairPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva publicación
    @PostMapping
    public ResponseEntity<RepairPost> createRepairPost(@RequestBody RepairPost repairPost) {
        return ResponseEntity.ok(repairPostService.saveRepairPost(repairPost));
    }

    // Actualizar una publicación
    @PutMapping("/{id}")
    public ResponseEntity<RepairPost> updateRepairPost(@PathVariable Integer id, @RequestBody RepairPost repairPost) {
        return repairPostService.getRepairPostById(id)
                .map(existingPost -> {
                    repairPost.setId(existingPost.getId());
                    return ResponseEntity.ok(repairPostService.saveRepairPost(repairPost));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una publicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepairPost(@PathVariable Integer id) {
        if (repairPostService.deleteRepairPost(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar publicaciones por título
    @GetMapping("/search")
    public ResponseEntity<List<RepairPost>> getPostsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(repairPostService.getPostByTitle(title));
    }

    // Buscar publicaciones por ID de usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RepairPost>> getPostsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(repairPostService.getPostByUserId(userId));
    }

}
