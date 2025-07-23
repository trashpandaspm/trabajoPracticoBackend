package utnfrc.isi.backend.servicio_logistica.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utnfrc.isi.backend.servicio_logistica.modelos.Deposito;
import utnfrc.isi.backend.servicio_logistica.repositorios.DepositoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/depositos")
public class DepositoController {
    @Autowired
    private DepositoRepository depositoRepository;

    @GetMapping
    public List<Deposito> obtenerTodos() {
        return depositoRepository.findAll();
    }

    @PostMapping
    public Deposito crearDeposito(@RequestBody Deposito deposito) {
        return depositoRepository.save(deposito);
    }

    @GetMapping("/{id}")
    public Deposito obtenerPorId(@PathVariable Long id) {
        return depositoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deposito no encontrado con ID: " + id));
    }
}
