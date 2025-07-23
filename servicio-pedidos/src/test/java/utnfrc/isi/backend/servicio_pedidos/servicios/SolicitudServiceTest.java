package utnfrc.isi.backend.servicio_pedidos.servicios;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.repositorios.SolicitudRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;
    @Mock
    private ContenedorService contenedorService;
    @Mock
    private TramoRutaService tramoRutaService;
    @Mock
    private RestClient.Builder restClientBuilder;
    @Mock
    private RestClient restClient; // Mock adicional para la cadena de llamadas

    @InjectMocks
    private SolicitudService solicitudService;

    @Test
    void testCrearSolicitud_CalculaCostoYTiempo() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Test", "test@test.com", "pass");
        Contenedor contenedorExistente = new Contenedor(1L, 1.0, 1.0, "OK", cliente);
        Solicitud solicitud = new Solicitud();
        solicitud.setContenedor(contenedorExistente);
        solicitud.setCiudadOrigenId(1L);

        // Simulamos que el contenedor existe
        when(contenedorService.obtenerPorId(1L)).thenReturn(contenedorExistente);

        // Simulamos la respuesta del repositorio al guardar
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitud);

        // Simulamos que la creación de tramos no hace nada
        doNothing().when(tramoRutaService).crearTramosParaSolicitud(any(Solicitud.class));

        // Act
        Solicitud solicitudCreada = solicitudService.crearSolicitud(solicitud);

        // Assert
        assertNotNull(solicitudCreada);
        // Verificamos que los valores calculados (simulados) se hayan asignado
        assertEquals(110350.0, solicitudCreada.getCostoEstimado()); // 5000 + (700 * 150.5)
        assertEquals(8.75, solicitudCreada.getTiempoEstimadoHoras()); // 700 / 80
        assertEquals(1L, solicitudCreada.getCamionId());

        // Verificamos que los métodos clave fueron llamados
        verify(solicitudRepository, times(1)).save(solicitud);
        verify(tramoRutaService, times(1)).crearTramosParaSolicitud(solicitud);
    }
}