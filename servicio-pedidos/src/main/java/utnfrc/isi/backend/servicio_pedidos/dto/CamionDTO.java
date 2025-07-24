package utnfrc.isi.backend.servicio_pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CamionDTO {
    private Long id;
    private Double capacidadPeso;
    private Double capacidadVolumen;
    private Boolean disponible;
}
