package utnfrc.isi.backend.servicio_logistica.modelos;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MONTO_BASE")
    private Double montoBase;

    @Column(name = "COSTO_KM")
    private Double costoKm;

    @Column(name = "COSTO_DIA_DEPOSITO")
    private Double costoDiaDeposito;
}
