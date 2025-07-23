package utnfrc.isi.backend.servicio_pedidos.servicios;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.repositorios.ContenedorRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContenedorServiceTest {

    @Mock
    private ContenedorRepository contenedorRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ContenedorService contenedorService;

    @Test
    void testCrearContenedor_Exitoso() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Test Client", "test@cliente.com", "pass");
        Contenedor nuevoContenedor = new Contenedor(null, 100.0, 50.0, "NUEVO", cliente);

        // Simulamos que el clienteService encuentra al cliente
        when(clienteService.obtenerPorId(1L)).thenReturn(cliente);
        // Simulamos la acción de guardar del repositorio
        when(contenedorRepository.save(any(Contenedor.class))).thenReturn(nuevoContenedor);

        // Act
        Contenedor contenedorCreado = contenedorService.crearContenedor(nuevoContenedor);

        // Assert
        assertNotNull(contenedorCreado);
        assertEquals("NUEVO", contenedorCreado.getEstado());
        // Verificamos que se llamó al método para validar al cliente
        verify(clienteService, times(1)).obtenerPorId(1L);
    }
}