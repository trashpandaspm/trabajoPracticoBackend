package utnfrc.isi.backend.servicio_pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaDTO {
    private Long id;
    private Double montoBase;
    private Double costoKm;
    private Double costoDiaDeposito;
}
