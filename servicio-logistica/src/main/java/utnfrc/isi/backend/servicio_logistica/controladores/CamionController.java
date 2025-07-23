package utnfrc.isi.backend.servicio_logistica.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utnfrc.isi.backend.servicio_logistica.modelos.Camion;
import utnfrc.isi.backend.servicio_logistica.servicios.CamionService;

import java.util.List;

@RestController
@RequestMapping("/api/camiones")
public class CamionController {
    @Autowired
    private CamionService camionService;

    @GetMapping
    public List<Camion> obtenerTodos() {
        return camionService.obtenerTodos();
    }

    @GetMapping("/disponible")
    public Camion obtenerCamionDisponible(@RequestParam Double peso, @RequestParam Double volumen) {
        return camionService.encontrarCamionDisponible(peso, volumen);
    }
}
