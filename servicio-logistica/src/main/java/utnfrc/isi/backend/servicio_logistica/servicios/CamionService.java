package utnfrc.isi.backend.servicio_logistica.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfrc.isi.backend.servicio_logistica.modelos.Camion;
import utnfrc.isi.backend.servicio_logistica.repositorios.CamionRepository;

import java.util.List;

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
}
