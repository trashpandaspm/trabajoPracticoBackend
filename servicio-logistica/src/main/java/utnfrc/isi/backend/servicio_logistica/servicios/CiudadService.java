package utnfrc.isi.backend.servicio_logistica.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfrc.isi.backend.servicio_logistica.modelos.Ciudad;
import utnfrc.isi.backend.servicio_logistica.repositorios.CiudadRepository;

import java.util.List;

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
}
