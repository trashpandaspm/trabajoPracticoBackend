package utnfrc.isi.backend.servicio_pedidos.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.repositorios.ClienteRepository;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente crearCliente(Cliente cliente) {
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        return clienteRepository.save(cliente);
    }

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
    }
}
