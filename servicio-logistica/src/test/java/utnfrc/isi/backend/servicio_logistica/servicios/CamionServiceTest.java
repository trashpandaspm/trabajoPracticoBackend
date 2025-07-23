package utnfrc.isi.backend.servicio_logistica.servicios;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utnfrc.isi.backend.servicio_logistica.modelos.Camion;
import utnfrc.isi.backend.servicio_logistica.repositorios.CamionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CamionServiceTest {

    @Mock
    private CamionRepository camionRepository;

    @InjectMocks
    private CamionService camionService;

    @Test
    void cuandoHayCamionesDisponibles_entoncesDevuelveElPrimero() {
        // Arrange
        Camion camion1 = new Camion(1L, 20000.0, 80.0, true);
        Camion camion2 = new Camion(2L, 25000.0, 90.0, true);

        // Simulamos la respuesta del repositorio
        when(camionRepository.findByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(15000.0, 75.0))
                .thenReturn(List.of(camion1, camion2));

        // Act
        Camion camionEncontrado = camionService.encontrarCamionDisponible(15000.0, 75.0);

        // Assert
        assertNotNull(camionEncontrado);
        assertEquals(1L, camionEncontrado.getId()); // Verifica que devuelve el primero de la lista
    }

    @Test
    void cuandoNoHayCamionesDisponibles_entoncesLanzaExcepcion() {
        // Arrange
        when(camionRepository.findByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(anyDouble(), anyDouble()))
                .thenReturn(List.of()); // Simulamos una lista vacía

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            camionService.encontrarCamionDisponible(30000.0, 100.0);
        });

        assertEquals("No hay camiones disponibles que cumplan con la capacidad requerida.", exception.getMessage());
    }
}
