package utnfrc.isi.backend.servicio_logistica.controladores;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import utnfrc.isi.backend.servicio_logistica.modelos.Tarifa;
import utnfrc.isi.backend.servicio_logistica.servicios.TarifaService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarifaController.class)
class TarifaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TarifaService tarifaService;

    @Test
    void testObtenerTarifaActual() throws Exception {
        // Arrange
        Tarifa tarifaActual = new Tarifa(1L, 5000.0, 150.5, 2500.0);
        when(tarifaService.obtenerTarifaActual()).thenReturn(tarifaActual);

        // Act & Assert
        mockMvc.perform(get("/api/tarifas/actual"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.montoBase").value(5000.0))
                .andExpect(jsonPath("$.costoKm").value(150.5));
    }
}