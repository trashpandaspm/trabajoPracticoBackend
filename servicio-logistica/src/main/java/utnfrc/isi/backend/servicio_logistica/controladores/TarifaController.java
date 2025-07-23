package utnfrc.isi.backend.servicio_logistica.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utnfrc.isi.backend.servicio_logistica.modelos.Tarifa;
import utnfrc.isi.backend.servicio_logistica.servicios.TarifaService;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

    @Autowired
    private TarifaService tarifaService;

    @GetMapping("/actual")
    public Tarifa obtenerTarifaActual() {
        return tarifaService.obtenerTarifaActual();
    }

    @PostMapping
    public Tarifa crearTarifa(@RequestBody Tarifa tarifa) {
        return tarifaService.crearTarifa(tarifa);
    }

    @PutMapping("/{id}")
    public Tarifa actualizarTarifa(@PathVariable Long id, @RequestBody Tarifa tarifa) {
        return tarifaService.actualizarTarifa(id, tarifa);
    }
}
