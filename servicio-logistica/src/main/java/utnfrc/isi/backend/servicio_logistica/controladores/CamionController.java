package utnfrc.isi.backend.servicio_logistica.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utnfrc.isi.backend.servicio_logistica.modelos.Camion;
import utnfrc.isi.backend.servicio_logistica.servicios.CamionService;

import java.util.List;
import java.util.Map;

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
    @PostMapping
    public Camion crearCamion(@RequestBody Camion camion) {
        return camionService.crearCamion(camion);
    }

    @PutMapping("/{id}")
    public Camion actualizarCamion(@PathVariable Long id, @RequestBody Camion camion) {
        return camionService.actualizarCamion(id, camion);
    }

    @PatchMapping("/{id}")
    public Camion actualizarParcialmente(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return camionService.actualizarParcialmente(id, updates);
    }
}
