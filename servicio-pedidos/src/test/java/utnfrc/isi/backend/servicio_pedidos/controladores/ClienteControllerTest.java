package utnfrc.isi.backend.servicio_pedidos.controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.servicios.ClienteService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc; // Permite simular peticiones HTTP

    @MockBean // Crea un mock del servicio y lo reemplaza en el contexto de Spring
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper; // Utilidad para convertir objetos a JSON

    @Test
    void testPostCrearCliente() throws Exception {
        // Arrange
        Cliente clienteNuevo = new Cliente(null, "Abril", "abril@test.com", "1234");
        Cliente clienteGuardado = new Cliente(1L, "Abril", "abril@test.com", "1234");

        when(clienteService.crearCliente(any(Cliente.class))).thenReturn(clienteGuardado);

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteNuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Abril"));
    }
}
