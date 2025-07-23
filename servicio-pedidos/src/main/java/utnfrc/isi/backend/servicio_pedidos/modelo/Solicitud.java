package utnfrc.isi.backend.servicio_pedidos.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CONTENEDOR_ID")
    private Contenedor contenedor;

    @NotNull(message = "El ID de la ciudad de origen es obligatorio")
    @Column(name = "CIUDAD_ORIGEN_ID")
    private Long ciudadOrigenId;

    @NotNull(message = "El ID de la ciudad de destino es obligatorio")
    @Column(name = "CIUDAD_DESTINO_ID")
    private Long ciudadDestinoId;

    @NotNull(message = "El ID del deposito es obligatorio")
    @Column(name = "DEPOSITO_ID")
    private Long depositoId;

    @NotNull(message = "El ID del camion es obligatorio")
    @Column(name = "CAMION_ID")
    private Long camionId;

    @Column(name = "COSTO_ESTIMADO")
    private Double costoEstimado;

    @Column(name = "TIEMPO_ESTIMADO_HORAS")
    private Double tiempoEstimadoHoras;

    @OneToMany(mappedBy = "solicitud")
    @JsonIgnore
    private List<TramoRuta> tramos;
}
