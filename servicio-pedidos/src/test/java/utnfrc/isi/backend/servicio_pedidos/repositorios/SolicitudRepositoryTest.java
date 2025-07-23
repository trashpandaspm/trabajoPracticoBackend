package utnfrc.isi.backend.servicio_pedidos.repositorios;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SolicitudRepositoryTest {

    @Autowired
    private SolicitudRepository solicitudRepository;
    @Autowired
    private ContenedorRepository contenedorRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testFindByContenedorId() {
        // Arrange: Creamos las entidades necesarias
        Cliente cliente = clienteRepository.save(new Cliente(null, "Cliente Prueba", "cliente@prueba.com", "pass"));
        Contenedor contenedor = contenedorRepository.save(new Contenedor(null, 500.0, 30.0, "LISTO", cliente));
        Solicitud solicitud = new Solicitud();
        solicitud.setContenedor(contenedor);
        solicitud.setCiudadOrigenId(1L);
        solicitud.setCiudadDestinoId(2L);
        solicitud.setDepositoId(1L);
        solicitud.setCamionId(1L);
        solicitudRepository.save(solicitud);

        // Act: Ejecutamos el método del repositorio
        Optional<Solicitud> solicitudEncontrada = solicitudRepository.findByContenedorId(contenedor.getId());

        // Assert: Verificamos que se encontró la solicitud correcta
        assertTrue(solicitudEncontrada.isPresent());
        assertEquals(contenedor.getId(), solicitudEncontrada.get().getContenedor().getId());
    }
}