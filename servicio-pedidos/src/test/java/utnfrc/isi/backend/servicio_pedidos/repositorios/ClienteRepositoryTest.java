package utnfrc.isi.backend.servicio_pedidos.repositorios;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Anotación clave para pruebas de repositorios
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void cuandoBuscaPorEmail_entoncesEncuentraCliente() {
        // Arrange: Guardamos un cliente en la base de datos de prueba
        Cliente nuevoCliente = new Cliente(null, "Abril", "abril@test.com", "1234");
        clienteRepository.save(nuevoCliente);

        // Act: Ejecutamos el método que queremos probar
        Optional<Cliente> clienteEncontrado = clienteRepository.findByEmail("abril@test.com");

        // Assert: Verificamos que el resultado es el esperado
        assertTrue(clienteEncontrado.isPresent());
        assertEquals("Abril", clienteEncontrado.get().getNombre());
    }
}
