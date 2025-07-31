package utnfrc.isi.backend.servicio_logistica.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfrc.isi.backend.servicio_logistica.modelos.Tarifa;
import utnfrc.isi.backend.servicio_logistica.repositorios.TarifaRepository;

import java.util.Map;

@Service
public class TarifaService {
    @Autowired
    private TarifaRepository tarifaRepository;

    public Tarifa obtenerTarifaActual(){
        return tarifaRepository.findAll().stream().findFirst()
                .orElseThrow(()-> new RuntimeException("No se encontraron tarifas configuradas."));
    }

    public Tarifa crearTarifa(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    public Tarifa actualizarTarifa(Long id, Tarifa tarifa) {
        tarifa.setId(id);
        return tarifaRepository.save(tarifa);
    }

    public Tarifa actualizarParcialmente(Long id, Map<String, Object> updates) {
        Tarifa tarifaExistente = tarifaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa no encontrada con ID: " + id));

        if (updates.containsKey("montoBase")) {
            tarifaExistente.setMontoBase((Double) updates.get("montoBase"));
        }
        if (updates.containsKey("costoKm")) {
            tarifaExistente.setCostoKm((Double) updates.get("costoKm"));
        }
        if (updates.containsKey("costoDiaDeposito")) {
            tarifaExistente.setCostoDiaDeposito((Double) updates.get("costoDiaDeposito"));
        }

        return tarifaRepository.save(tarifaExistente);
    }
}
