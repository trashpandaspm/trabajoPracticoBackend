package utnfrc.isi.backend.servicio_pedidos.controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.servicios.SolicitudService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudController.class)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SolicitudService solicitudService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPostCrearSolicitud() throws Exception {
        // Arrange
        Contenedor contenedor = new Contenedor();
        contenedor.setId(1L);
        Solicitud solicitudNueva = new Solicitud();
        solicitudNueva.setContenedor(contenedor);

        Solicitud solicitudGuardada = new Solicitud();
        solicitudGuardada.setId(1L);
        solicitudGuardada.setContenedor(contenedor);
        solicitudGuardada.setCostoEstimado(110350.0);

        when(solicitudService.crearSolicitud(any(Solicitud.class))).thenReturn(solicitudGuardada);

        // Act & Assert
        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudNueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.costoEstimado").value(110350.0));
    }
}