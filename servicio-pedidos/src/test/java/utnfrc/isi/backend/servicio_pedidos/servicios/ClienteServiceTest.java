package utnfrc.isi.backend.servicio_pedidos.servicios;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;
import utnfrc.isi.backend.servicio_pedidos.repositorios.ClienteRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock // Crea una versión "falsa" (mock) del repositorio
    private ClienteRepository clienteRepository;

    @InjectMocks // Crea una instancia real del servicio e inyecta los mocks de arriba
    private ClienteService clienteService;

    @Test
    void testCrearCliente_Exitoso() {
        // Arrange (Preparar)
        Cliente nuevoCliente = new Cliente(null, "Abril", "abril@test.com", "1234");
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.empty()); // Simula que el email no existe
        when(clienteRepository.save(any(Cliente.class))).thenReturn(nuevoCliente); // Simula la acción de guardar

        // Act (Actuar)
        Cliente clienteCreado = clienteService.crearCliente(nuevoCliente);

        // Assert (Verificar)
        assertNotNull(clienteCreado);
        assertEquals("Abril", clienteCreado.getNombre());
        verify(clienteRepository, times(1)).save(nuevoCliente); // Verifica que el método save fue llamado 1 vez
    }

    @Test
    void testCrearCliente_EmailDuplicado() {
        // Arrange (Preparar)
        Cliente clienteExistente = new Cliente(1L, "Juan", "juan@test.com", "pass");
        when(clienteRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(clienteExistente));

        // Act & Assert (Actuar y Verificar)
        // Verificamos que se lance la excepción correcta cuando el email ya existe
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente(new Cliente(null, "Otro", "juan@test.com", "pass"));
        });

        assertEquals("El email ya está registrado.", exception.getMessage());
        verify(clienteRepository, never()).save(any(Cliente.class)); // Verifica que NUNCA se llamó a save
    }
}
