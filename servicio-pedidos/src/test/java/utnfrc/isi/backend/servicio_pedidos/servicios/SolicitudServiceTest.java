package utnfrc.isi.backend.servicio_pedidos.servicios;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;
import utnfrc.isi.backend.servicio_pedidos.dto.CamionDTO;
import utnfrc.isi.backend.servicio_pedidos.dto.CiudadDTO;
import utnfrc.isi.backend.servicio_pedidos.dto.TarifaDTO;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.repositorios.SolicitudRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock private SolicitudRepository solicitudRepository;
    @Mock private ContenedorService contenedorService;
    @Mock private TramoRutaService tramoRutaService;
    @Mock private RestClient.Builder restClientBuilder;
    @Mock private RestClient restClient;
    @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private SolicitudService solicitudService;

    @BeforeEach
    void setUp() {
        // Inyectamos una API Key de prueba antes de cada test
        ReflectionTestUtils.setField(solicitudService, "apiKey", "test-key");

        // --- CONFIGURACIÓN DE MOCK REFACTORIZADA Y ROBUSTA ---
        // Aseguramos que la cadena de llamadas siempre devuelva el siguiente mock
        lenient().when(restClientBuilder.build()).thenReturn(restClient);
        lenient().when(restClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(anyString(), any(Object[].class))).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        // Hacemos el mock de baseUrl explícito para la llamada a Google
        lenient().when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder);
    }

    @Test
    void testCrearSolicitud_FlujoCompletoExitoso() throws JsonProcessingException {
        // Arrange
        Cliente cliente = new Cliente();
        Contenedor contenedor = new Contenedor(1L, 15000.0, 60.0, "LISTO", cliente);
        Solicitud solicitudEntrante = new Solicitud();
        solicitudEntrante.setContenedor(contenedor);
        solicitudEntrante.setCiudadOrigenId(1L);
        solicitudEntrante.setCiudadDestinoId(2L);

        // Simulamos las respuestas de las APIs
        mockRespuestaServicioLogistica();
        mockRespuestaGoogleMaps(123456); // 123.456 km

        // Simulamos el comportamiento de los servicios y repositorios internos
        when(contenedorService.obtenerPorId(1L)).thenReturn(contenedor);
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudEntrante);

        // Act
        Solicitud solicitudCreada = solicitudService.crearSolicitud(solicitudEntrante);

        // Assert
        assertNotNull(solicitudCreada);
        assertEquals(1L, solicitudCreada.getCamionId());
        // Usamos un delta para la comparación de doubles
        assertEquals(1.5432, solicitudCreada.getTiempoEstimadoHoras(), 0.0001);
        assertEquals(23580.128, solicitudCreada.getCostoEstimado(), 0.001);
    }

    private void mockRespuestaServicioLogisticaParcial() {
        CiudadDTO ciudadOrigen = new CiudadDTO(1L, "Origen", -31.0, -64.0);
        CiudadDTO ciudadDestino = new CiudadDTO(2L, "Destino", -34.0, -58.0);
        TarifaDTO tarifa = new TarifaDTO(1L, 5000.0, 150.5, 2500.0);

        // Devolvemos las ciudades y la tarifa, pero no el camión
        when(responseSpec.body(CiudadDTO.class)).thenReturn(ciudadOrigen, ciudadDestino);
        when(responseSpec.body(TarifaDTO.class)).thenReturn(tarifa);
    }

    // --- Métodos auxiliares para no repetir código ---
    private void mockRespuestaServicioLogistica() {
        CiudadDTO ciudadOrigen = new CiudadDTO(1L, "Origen", -31.0, -64.0);
        CiudadDTO ciudadDestino = new CiudadDTO(2L, "Destino", -34.0, -58.0);
        TarifaDTO tarifa = new TarifaDTO(1L, 5000.0, 150.5, 2500.0);
        CamionDTO camion = new CamionDTO(1L, 20000.0, 80.0, true);

        // Configuramos los mocks para que devuelvan los DTOs correspondientes
        when(responseSpec.body(CiudadDTO.class)).thenReturn(ciudadOrigen, ciudadDestino);
        when(responseSpec.body(TarifaDTO.class)).thenReturn(tarifa);
        when(responseSpec.body(CamionDTO.class)).thenReturn(camion);
    }

    private void mockRespuestaGoogleMaps(int distanciaEnMetros) throws JsonProcessingException {
        String json = "{\"rows\":[{\"elements\":[{\"distance\":{\"value\":" + distanciaEnMetros + "}}]}]}";
        // Configuramos el mock para la respuesta específica de Google
        when(responseSpec.toEntity(String.class)).thenReturn(ResponseEntity.ok(json));
    }

    @Test
    void testCrearSolicitud_FallaSiNoHayCamionDisponible() {
        // Arrange
        Cliente cliente = new Cliente();
        Contenedor contenedor = new Contenedor(1L, 15000.0, 60.0, "LISTO", cliente);
        Solicitud solicitudEntrante = new Solicitud();
        solicitudEntrante.setContenedor(contenedor);
        solicitudEntrante.setCiudadOrigenId(1L);
        solicitudEntrante.setCiudadDestinoId(2L);

        // Simulamos las respuestas de las APIs internas
        mockRespuestaServicioLogisticaParcial(); // Usamos un helper parcial

        // La clave de esta prueba: simulamos que la API de camiones lanza una excepción
        when(responseSpec.body(CamionDTO.class)).thenThrow(new RuntimeException("No hay camiones disponibles que cumplan con la capacidad requerida."));

        when(contenedorService.obtenerPorId(1L)).thenReturn(contenedor);

        // Act & Assert: Verificamos que se lance la excepción correcta
        Exception exception = assertThrows(RuntimeException.class, () -> {
            solicitudService.crearSolicitud(solicitudEntrante);
        });

        assertTrue(exception.getMessage().contains("No hay camiones disponibles"));
        // Verificamos que, como falló, nunca intentó guardar la solicitud
        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }
    @Test
    void testCrearSolicitud_FallaSiCiudadNoExiste() {
        // Arrange
        Cliente cliente = new Cliente();
        Contenedor contenedor = new Contenedor(1L, 15000.0, 60.0, "LISTO", cliente);
        Solicitud solicitudEntrante = new Solicitud();
        solicitudEntrante.setContenedor(contenedor);
        solicitudEntrante.setCiudadOrigenId(99L); // Un ID de ciudad que no existe

        when(contenedorService.obtenerPorId(1L)).thenReturn(contenedor);

        // La clave: simulamos que la llamada a la API de ciudades falla
        when(responseSpec.body(CiudadDTO.class)).thenThrow(new RuntimeException("Ciudad no encontrada con ID: 99"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            solicitudService.crearSolicitud(solicitudEntrante);
        });
    }
}