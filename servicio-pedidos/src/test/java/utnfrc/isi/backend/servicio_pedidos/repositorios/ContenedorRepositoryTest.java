package utnfrc.isi.backend.servicio_pedidos.repositorios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ContenedorRepositoryTest {

    @Autowired
    private ContenedorRepository contenedorRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente clienteGuardado;

    @BeforeEach
    void setUp() {
        // Creamos un cliente de prueba antes de cada test
        Cliente cliente = new Cliente(null, "Test Client", "test@cliente.com", "pass");
        clienteGuardado = clienteRepository.save(cliente);
    }

    @Test
    void testFindByClienteId() {
        // Arrange
        Contenedor contenedor = new Contenedor(null, 100.0, 50.0, "PENDIENTE", clienteGuardado);
        contenedorRepository.save(contenedor);

        // Act
        List<Contenedor> contenedores = contenedorRepository.findByClienteId(clienteGuardado.getId());

        // Assert
        assertFalse(contenedores.isEmpty());
        assertEquals(1, contenedores.size());
        assertEquals(clienteGuardado.getId(), contenedores.get(0).getCliente().getId());
    }

    @Test
    void testFindByEstado() {
        // Arrange
        Contenedor contenedor = new Contenedor(null, 120.0, 60.0, "EN_TRANSITO", clienteGuardado);
        contenedorRepository.save(contenedor);

        // Act
        List<Contenedor> contenedores = contenedorRepository.findByEstado("EN_TRANSITO");

        // Assert
        assertFalse(contenedores.isEmpty());
        assertEquals("EN_TRANSITO", contenedores.get(0).getEstado());
    }
}