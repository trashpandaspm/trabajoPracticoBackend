package utnfrc.isi.backend.servicio_pedidos.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utnfrc.isi.backend.servicio_pedidos.modelo.TramoRuta;
import utnfrc.isi.backend.servicio_pedidos.servicios.TramoRutaService;

import java.util.List;

@RestController
@RequestMapping("/api/tramos")
public class TramoRutaController {

    @Autowired
    private TramoRutaService tramoRutaService;

    @PatchMapping("/{id}/completar")
    public TramoRuta registrarLlegada(@PathVariable Long id) {
        return tramoRutaService.registrarLlegada(id);
    }
    @PatchMapping("/{id}/iniciar")
    public TramoRuta registrarSalida(@PathVariable Long id) {
        return tramoRutaService.registrarSalida(id);
    }
    @GetMapping
    public List<TramoRuta> obtenerTodosLosTramos() {
        return tramoRutaService.obtenerTodos();
    }

}
