package utnfrc.isi.backend.servicio_pedidos.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.servicios.ContenedorService;

import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
public class ContenedorController {
    @Autowired
    private ContenedorService contenedorService;

    @GetMapping("/{id}")
    public Contenedor obtenerPorId(@PathVariable Long id) {
        return contenedorService.obtenerPorId(id);
    }

    @GetMapping
    public List<Contenedor> obtenerPorEstado(@RequestParam String estado) {
        return contenedorService.obtenerPorEstado(estado);
    }

    @PostMapping
    public Contenedor crearContenedor(@RequestBody Contenedor contenedor) {
        return contenedorService.crearContenedor(contenedor);
    }
}
