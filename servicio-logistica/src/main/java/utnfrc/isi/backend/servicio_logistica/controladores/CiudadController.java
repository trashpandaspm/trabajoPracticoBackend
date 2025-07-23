package utnfrc.isi.backend.servicio_logistica.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utnfrc.isi.backend.servicio_logistica.modelos.Ciudad;
import utnfrc.isi.backend.servicio_logistica.servicios.CiudadService;

import java.util.List;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadController {
    @Autowired
    private CiudadService ciudadService;

    @GetMapping
    public List<Ciudad> obtenerTodas() {
        return ciudadService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Ciudad obtenerPorId(@PathVariable Long id) {
        return ciudadService.obtenerPorId(id);
    }
}
