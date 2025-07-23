package utnfrc.isi.backend.servicio_logistica.controladores;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import utnfrc.isi.backend.servicio_logistica.modelos.Ciudad;
import utnfrc.isi.backend.servicio_logistica.servicios.CiudadService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CiudadController.class)
class CiudadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CiudadService ciudadService;

    @Test
    void testObtenerPorId() throws Exception {
        // Arrange
        Ciudad cordoba = new Ciudad();
        cordoba.setId(1L);
        cordoba.setNombre("Córdoba");
        when(ciudadService.obtenerPorId(1L)).thenReturn(cordoba);

        // Act & Assert
        mockMvc.perform(get("/api/ciudades/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Córdoba"));
    }
}