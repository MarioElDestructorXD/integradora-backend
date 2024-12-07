package mx.edu.utez.integradora.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ubicacion")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUbicacion;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @JsonBackReference // Esta anotación evita que la referencia hacia el usuario se serialice en cada ubicación
    private User usuario;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = true)
    private Proveedor proveedor;

    // Getters y setters
}
