package utnfrc.isi.backend.servicio_pedidos.repositorios;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.modelo.TramoRuta;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TramoRutaRepositoryTest {

    @Autowired
    private TramoRutaRepository tramoRutaRepository;
    @Autowired
    private SolicitudRepository solicitudRepository;
    @Autowired
    private ContenedorRepository contenedorRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testFindBySolicitudOrderByOrdenAsc() {
        // Arrange: Creamos toda la jerarquía de objetos
        Cliente cliente = clienteRepository.save(new Cliente(null, "Test", "test@test.com", "pass"));
        Contenedor contenedor = contenedorRepository.save(new Contenedor(null, 1.0, 1.0, "OK", cliente));

        Solicitud solicitud = new Solicitud();
        solicitud.setContenedor(contenedor);
        solicitud.setCiudadOrigenId(1L);
        solicitud.setCiudadDestinoId(2L);
        solicitud.setDepositoId(1L);
        solicitud.setCamionId(1L);
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

        // Creamos dos tramos en desorden
        LocalDateTime salidaEstimada = LocalDateTime.now().plusDays(1); // Mañana
        LocalDateTime llegadaEstimada = LocalDateTime.now().plusDays(2); // Pasado mañana

        // Creamos dos tramos en desorden usando las fechas válidas
        TramoRuta tramo2 = new TramoRuta(null, solicitudGuardada, 2, "T2", 2L, 3L, salidaEstimada, llegadaEstimada, null, null);
        TramoRuta tramo1 = new TramoRuta(null, solicitudGuardada, 1, "T1", 1L, 2L, salidaEstimada, llegadaEstimada, null, null);
        tramoRutaRepository.save(tramo2);
        tramoRutaRepository.save(tramo1);

        // Act: Ejecutamos el método del repositorio
        List<TramoRuta> tramos = tramoRutaRepository.findBySolicitudOrderByOrdenAsc(solicitudGuardada);

        // Assert: Verificamos que la lista no esté vacía y que los tramos estén en el orden correcto
        assertFalse(tramos.isEmpty());
        assertEquals(2, tramos.size());
        assertEquals(1, tramos.get(0).getOrden()); // El primer elemento debe ser el tramo 1
        assertEquals(2, tramos.get(1).getOrden()); // El segundo elemento debe ser el tramo 2
    }
}