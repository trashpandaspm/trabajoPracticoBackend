package utnfrc.isi.backend.servicio_logistica.controladores;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import utnfrc.isi.backend.servicio_logistica.modelos.Camion;
import utnfrc.isi.backend.servicio_logistica.servicios.CamionService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CamionController.class)
class CamionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CamionService camionService;

    @Test
    void testObtenerCamionDisponible() throws Exception {
        // Arrange
        Camion camionDisponible = new Camion(1L, 20000.0, 80.0, true);
        double pesoRequerido = 15000.0;
        double volumenRequerido = 75.0;

        when(camionService.encontrarCamionDisponible(pesoRequerido, volumenRequerido))
                .thenReturn(camionDisponible);

        // Act & Assert
        mockMvc.perform(get("/api/camiones/disponible")
                        .param("peso", String.valueOf(pesoRequerido))
                        .param("volumen", String.valueOf(volumenRequerido)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.disponible").value(true));
    }
}
