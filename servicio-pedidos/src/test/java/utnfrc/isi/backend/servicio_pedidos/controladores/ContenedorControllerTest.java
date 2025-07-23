package utnfrc.isi.backend.servicio_pedidos.controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.servicios.ContenedorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContenedorController.class)
class ContenedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContenedorService contenedorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPostCrearContenedor() throws Exception {
        // Arrange
        Cliente cliente = new Cliente(1L, "Test Client", "test@cliente.com", "pass");
        Contenedor contenedorNuevo = new Contenedor(null, 100.0, 50.0, "NUEVO", cliente);
        Contenedor contenedorGuardado = new Contenedor(1L, 100.0, 50.0, "NUEVO", cliente);

        when(contenedorService.crearContenedor(any(Contenedor.class))).thenReturn(contenedorGuardado);

        // Act & Assert
        mockMvc.perform(post("/api/contenedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contenedorNuevo)))
                .andExpect(status().isOk()) // O .andExpect(status().isCreated()) si usas ese código de estado
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("NUEVO"))
                .andExpect(jsonPath("$.cliente.id").value(1L));
    }
}