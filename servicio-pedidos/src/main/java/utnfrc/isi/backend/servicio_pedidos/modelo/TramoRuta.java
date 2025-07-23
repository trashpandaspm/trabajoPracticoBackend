package utnfrc.isi.backend.servicio_pedidos.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TramoRuta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SOLICITUD_ID")
    private Solicitud solicitud;

    @Positive(message = "El numero debe ser positivo")
    @Column(name = "ORDEN")
    private Integer orden;

    @NotBlank(message = "No puede estar en blanco")
    @Size(max = 30, message = "El maximo son 30 caracteres")
    @Column(name = "TIPO_TRAMO")
    private String tipoTramo;

    @Column(name = "CIUDAD_ORIGEN_ID")
    private Long ciudadOrigenId;

    @Column(name = "CIUDAD_DESTINO_ID")
    private Long ciudadDestinoId;

    @NotNull(message = "La fecha estimada de salida no puede ser nula")
    @FutureOrPresent(message = "La fecha estimada de salida no puede ser pasada")
    @Column(name = "FECHA_ESTIMADA_SALIDA")
    private LocalDateTime fechaEstimadaSalida;

    @NotNull(message = "La fecha estimada de llegada no puede ser nula")
    @Future(message = "La fecha estimada de llegada debe ser futura")
    @Column(name = "FECHA_ESTIMADA_LLEGADA")
    private LocalDateTime fechaEstimadaLlegada;

    @PastOrPresent
    @Column(name = "FECHA_REAL_SALIDA")
    private LocalDateTime fechaRealSalida;

    @PastOrPresent
    @Column(name = "FECHA_REAL_LLEGADA")
    private LocalDateTime fechaRealLlegada;
}
