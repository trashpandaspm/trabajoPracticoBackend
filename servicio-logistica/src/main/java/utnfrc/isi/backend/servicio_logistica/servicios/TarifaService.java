package utnfrc.isi.backend.servicio_logistica.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfrc.isi.backend.servicio_logistica.modelos.Tarifa;
import utnfrc.isi.backend.servicio_logistica.repositorios.TarifaRepository;

@Service
public class TarifaService {
    @Autowired
    private TarifaRepository tarifaRepository;

    public Tarifa obtenerTarifaActual(){
        return tarifaRepository.findAll().stream().findFirst()
                .orElseThrow(()-> new RuntimeException("No se encontraron tarifas configuradas."));
    }
}
