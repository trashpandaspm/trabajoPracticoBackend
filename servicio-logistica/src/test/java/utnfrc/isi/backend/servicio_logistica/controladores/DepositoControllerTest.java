package utnfrc.isi.backend.servicio_logistica.controladores;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import utnfrc.isi.backend.servicio_logistica.modelos.Ciudad;
import utnfrc.isi.backend.servicio_logistica.modelos.Deposito;
import utnfrc.isi.backend.servicio_logistica.repositorios.DepositoRepository;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepositoController.class)
class DepositoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepositoRepository depositoRepository;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Test
    void testObtenerTodos() throws Exception {
        // Arrange
        Ciudad cordoba = new Ciudad(1L, "Córdoba", -31.42, -64.18, null);
        Deposito deposito1 = new Deposito(1L, cordoba, "Dirección 1", -31.42, -64.18);

        when(depositoRepository.findAll()).thenReturn(List.of(deposito1));

        // Act & Assert
        mockMvc.perform(get("/api/depositos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].direccion").value("Dirección 1"));
    }
}
