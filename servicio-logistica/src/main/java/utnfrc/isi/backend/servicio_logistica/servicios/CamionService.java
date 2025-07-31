package utnfrc.isi.backend.servicio_logistica.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfrc.isi.backend.servicio_logistica.modelos.Camion;
import utnfrc.isi.backend.servicio_logistica.repositorios.CamionRepository;

import java.util.List;
import java.util.Map;

@Service
public class CamionService {
    @Autowired
    private CamionRepository camionRepository;

    public List<Camion> obtenerTodos() {
        return camionRepository.findAll();
    }

    public Camion encontrarCamionDisponible(Double pesoRequerido, Double volumenRequerido) {
        List<Camion> camionesDisponibles = camionRepository
                .findByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(
                        pesoRequerido, volumenRequerido);
        if (camionesDisponibles.isEmpty()){
            throw new RuntimeException("No hay camiones disponibles que cumplan con la capacidad requerida.");
        }
        return camionesDisponibles.get(0);
    }
    public Camion crearCamion(Camion camion) {
        return camionRepository.save(camion);
    }

    public Camion actualizarCamion(Long id, Camion camion) {
        camion.setId(id);
        return camionRepository.save(camion);
    }

    public Camion actualizarParcialmente(Long id, Map<String, Object> updates) {
        Camion camionExistente = camionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado con ID: " + id));


        if (updates.containsKey("capacidadPeso")) {
            camionExistente.setCapacidadPeso((Double) updates.get("capacidadPeso"));
        }

        if (updates.containsKey("capacidadVolumen")) {
            camionExistente.setCapacidadVolumen((Double) updates.get("capacidadVolumen"));
        }

        if (updates.containsKey("disponible")) {
            camionExistente.setDisponible((Boolean) updates.get("disponible"));
        }

        return camionRepository.save(camionExistente);
    }
}
