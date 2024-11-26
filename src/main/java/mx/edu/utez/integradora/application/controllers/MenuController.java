package mx.edu.utez.integradora.application.controllers;

import mx.edu.utez.integradora.application.dtos.MenuOption;
import mx.edu.utez.integradora.application.services.MenuService;
import mx.edu.utez.integradora.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuOption>> getMenuOptions() {
        // Recuperar el usuario autenticado
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MenuOption> menuOptions = menuService.getMenuOptions(currentUser.getRole());

        return ResponseEntity.ok(menuOptions);
    }
}
