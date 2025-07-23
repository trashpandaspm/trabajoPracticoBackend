package utnfrc.isi.backend.servicio_logistica.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CAPACIDAD_PESO")
    private Double capacidadPeso;

    @Column(name = "CAPACIDAD_VOLUMEN")
    private Double capacidadVolumen;

    @Column(name = "DISPONIBLE")
    private Boolean disponible;
}
