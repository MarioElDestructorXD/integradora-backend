package mx.edu.utez.integradora.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "problema")
public class Problema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProblema;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(name = "firebase_location_id")
    private String firebaseLocationId;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Lob
    private byte[] fotografia;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProblema estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProblema categoria;

    // Nuevos campos para almacenar la ubicación
    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @ManyToOne
    @JoinColumn(name = "id_ubicacion", nullable = false)
    private Ubicacion ubicacion;


    public enum EstadoProblema {
        ABIERTO,
        EN_PROCESO,
        RESUELTO,
        CERRADO
    }

    public enum CategoriaProblema {
        CARPINTERIA,
        PLOMERIA,
        ELECTRICIDAD,
        ALBAÑILERIA,
    }

}
