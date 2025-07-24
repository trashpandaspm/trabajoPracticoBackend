package utnfrc.isi.backend.servicio_logistica.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfrc.isi.backend.servicio_logistica.modelos.Ciudad;
import utnfrc.isi.backend.servicio_logistica.repositorios.CiudadRepository;

import java.util.List;
import java.util.Map;

@Service
public class CiudadService {
    @Autowired
    private CiudadRepository ciudadRepository;

    public List<Ciudad> obtenerTodas(){
        return ciudadRepository.findAll();
    }

    public Ciudad obtenerPorId(Long id){
        return ciudadRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Ciudad no encontrada con ID:" + id));
    }

    public Ciudad crearCiudad(Ciudad ciudad) {
        return ciudadRepository.save(ciudad);
    }

    public Ciudad actualizarCiudad(Long id, Ciudad ciudad) {
        ciudad.setId(id);
        return ciudadRepository.save(ciudad);
    }

    public Ciudad actualizarParcialmente(Long id, Map<String, Object> updates) {
        Ciudad ciudadExistente = ciudadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada con ID: " + id));

        if (updates.containsKey("nombre")) {
            ciudadExistente.setNombre((String) updates.get("nombre"));
        }
        if (updates.containsKey("latitud")) {
            ciudadExistente.setLatitud((Double) updates.get("latitud"));
        }
        if (updates.containsKey("longitud")) {
            ciudadExistente.setLongitud((Double) updates.get("longitud"));
        }

        return ciudadRepository.save(ciudadExistente);
    }
}
