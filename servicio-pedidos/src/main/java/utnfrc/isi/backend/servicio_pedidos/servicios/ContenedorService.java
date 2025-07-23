package utnfrc.isi.backend.servicio_pedidos.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.repositorios.ContenedorRepository;

import java.util.List;

@Service
public class ContenedorService {
    @Autowired
    private ContenedorRepository contenedorRepository;

    @Autowired
    private ClienteService clienteService;

    public Contenedor crearContenedor(Contenedor contenedor) {
        clienteService.obtenerPorId(contenedor.getCliente().getId());
        return contenedorRepository.save(contenedor);
    }

    public List<Contenedor> obtenerPorEstado(String estado){
        return contenedorRepository.findByEstado(estado);
    }

    public List<Contenedor> obtenerPorClienteId(Long clienteId) {
        return contenedorRepository.findByClienteId(clienteId);
    }

    public Contenedor obtenerPorId(Long id){
        return contenedorRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Contenedor no encontrado con ID: " + id));
    }
}
