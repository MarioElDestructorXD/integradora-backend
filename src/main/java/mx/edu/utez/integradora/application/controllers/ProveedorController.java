package mx.edu.utez.integradora.application.controllers;
import mx.edu.utez.integradora.domain.entities.Proveedor;
import mx.edu.utez.integradora.application.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<Proveedor>> listarProveedores() {
        return ResponseEntity.ok(proveedorService.listarProveedores());
    }

    @PostMapping
    public ResponseEntity<Proveedor> registrarProveedor(@RequestBody Proveedor proveedor) {
        return ResponseEntity.ok(proveedorService.registrarProveedor(proveedor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerProveedorPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(proveedorService.obtenerProveedorPorId(id));
    }
}
