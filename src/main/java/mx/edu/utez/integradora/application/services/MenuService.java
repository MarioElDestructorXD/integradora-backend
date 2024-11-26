package mx.edu.utez.integradora.application.services;

import mx.edu.utez.integradora.application.dtos.MenuOption;
import mx.edu.utez.integradora.domain.entities.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    public List<MenuOption> getMenuOptions(Role role) {
        List<MenuOption> options = new ArrayList<>();

        // Opciones comunes para todos los roles
        options.add(new MenuOption("Perfil", "Gestiona tu perfil", "/api/user/profile"));
        options.add(new MenuOption("Cerrar Sesión", "Salir del sistema", "/api/auth/logout"));

        // Opciones específicas por rol
        if (role == Role.CLIENTE) {
            options.add(new MenuOption("Mis Problemas", "Gestiona tus problemas", "/api/problemas"));
            options.add(new MenuOption("Historial de Órdenes", "Revisa tus órdenes", "/api/ordenes/historial"));
        } else if (role == Role.PROVEEDOR) {
            options.add(new MenuOption("Mis Servicios", "Gestiona tus servicios", "/api/servicios"));
            options.add(new MenuOption("Ofertas Disponibles", "Revisa problemas publicados", "/api/problemas/ofertas"));
        }

        return options;
    }
}
