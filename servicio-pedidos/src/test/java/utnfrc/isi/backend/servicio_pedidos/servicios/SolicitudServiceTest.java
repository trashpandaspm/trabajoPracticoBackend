package utnfrc.isi.backend.servicio_pedidos.servicios;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClient;
import utnfrc.isi.backend.servicio_pedidos.dto.CamionDTO;
import utnfrc.isi.backend.servicio_pedidos.dto.CiudadDTO;
import utnfrc.isi.backend.servicio_pedidos.dto.TarifaDTO;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.modelo.TramoRuta;
import utnfrc.isi.backend.servicio_pedidos.repositorios.ClienteRepository;
import utnfrc.isi.backend.servicio_pedidos.repositorios.ContenedorRepository;
import utnfrc.isi.backend.servicio_pedidos.repositorios.TramoRutaRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = { "google.maps.apikey=DUMMY_KEY_FOR_TEST" })
@AutoConfigureMockMvc
public class SolicitudServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ContenedorRepository contenedorRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private TramoRutaRepository tramoRutaRepository;

    @MockBean
    private RestClient.Builder restClientBuilder;

    private RestClient mockRestClient;

    @BeforeEach
    void setUp() {
        // ================== INICIO DE LA CORRECCIÓN ==================
        // Creamos un único mock de RestClient
        this.mockRestClient = Mockito.mock(RestClient.class);

        // Configuramos el builder para que maneje AMBOS casos
        when(restClientBuilder.build()).thenReturn(mockRestClient);
        when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder); // <-- Clave: que se devuelva a sí mismo
        // =================== FIN DE LA CORRECCIÓN ====================
    }

    @Test
    void testFlujoCompletoDeSolicitudYCalculoCostoFinal() throws Exception {
        // === ARRANGE (Preparar) ===
        Cliente clienteDePrueba = clienteRepository.save(new Cliente(1L, "Test Client", "test@test.com", "pass"));
        contenedorRepository.save(new Contenedor(1L, 18000.0, 75.0, "DISPONIBLE", clienteDePrueba));

        // Preparamos los mocks para la cadena de RestClient
        RestClient.RequestHeadersUriSpec mockUriSpec = Mockito.mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec mockHeadersSpec = Mockito.mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec mockResponseSpec = Mockito.mock(RestClient.ResponseSpec.class);

        when(mockRestClient.get()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri(anyString(), any(Object[].class))).thenReturn(mockHeadersSpec);
        when(mockUriSpec.uri(anyString())).thenReturn(mockHeadersSpec);
        when(mockHeadersSpec.retrieve()).thenReturn(mockResponseSpec);

        // Preparamos los datos de prueba
        CiudadDTO ciudadOrigen = new CiudadDTO(1L, "Origen", 10.0, 20.0);
        CiudadDTO ciudadDestino = new CiudadDTO(3L, "Destino", 11.0, 21.0);
        TarifaDTO tarifa = new TarifaDTO(1L, 500.0, 1.5, 25.0);
        CamionDTO camion = new CamionDTO(1L, 20000.0, 80.0, true);
        // Simulamos la respuesta de Google Maps (valor en metros)
        String googleMapsResponse = "{\"routes\":[{\"legs\":[{\"distance\":{\"value\":123456}}]}]}";

        // Configuramos las respuestas del mock
        when(mockResponseSpec.body(String.class)).thenReturn(googleMapsResponse);
        when(mockResponseSpec.body(CiudadDTO.class)).thenReturn(ciudadOrigen, ciudadDestino);
        when(mockResponseSpec.body(TarifaDTO.class)).thenReturn(tarifa);
        when(mockResponseSpec.body(CamionDTO.class)).thenReturn(camion);

        String solicitudJson = "{\"contenedor\": {\"id\": 1}, \"ciudadOrigenId\": 1, \"ciudadDestinoId\": 3, \"depositoId\": 1}";


        // === ACT & ASSERT (Ejecutar y Verificar) ===
        MvcResult result = mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(solicitudJson))
                .andExpect(status().isCreated())
                .andReturn();

        // El resto del test sigue igual...
        ObjectMapper objectMapper = new ObjectMapper();
        Solicitud solicitudCreada = objectMapper.readValue(result.getResponse().getContentAsString(), Solicitud.class);
        Long solicitudId = solicitudCreada.getId();

        List<TramoRuta> tramosCreados = tramoRutaRepository.findBySolicitudId(solicitudId);
        Long tramo1Id = tramosCreados.get(0).getId();
        Long tramo2Id = tramosCreados.get(1).getId();

        mockMvc.perform(patch("/api/tramos/" + tramo1Id + "/iniciar")).andExpect(status().isOk());
        mockMvc.perform(patch("/api/tramos/" + tramo1Id + "/completar")).andExpect(status().isOk());
        mockMvc.perform(patch("/api/tramos/" + tramo2Id + "/iniciar")).andExpect(status().isOk());
        mockMvc.perform(patch("/api/tramos/" + tramo2Id + "/completar")).andExpect(status().isOk());

        mockMvc.perform(post("/api/solicitudes/" + solicitudId + "/finalizar-costo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.costoFinal").isNumber());
    }
}