package utnfrc.isi.backend.servicio_logistica.servicios;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utnfrc.isi.backend.servicio_logistica.modelos.Ciudad;
import utnfrc.isi.backend.servicio_logistica.repositorios.CiudadRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CiudadServiceTest {

    @Mock
    private CiudadRepository ciudadRepository;

    @InjectMocks
    private CiudadService ciudadService;

    @Test
    void cuandoSeBuscaPorIdExistente_entoncesDevuelveCiudad() {
        // Arrange
        Ciudad cordoba = new Ciudad();
        cordoba.setId(1L);
        cordoba.setNombre("Córdoba");
        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(cordoba));

        // Act
        Ciudad ciudadEncontrada = ciudadService.obtenerPorId(1L);

        // Assert
        assertNotNull(ciudadEncontrada);
        assertEquals("Córdoba", ciudadEncontrada.getNombre());
    }

    @Test
    void cuandoSeBuscaPorIdInexistente_entoncesLanzaExcepcion() {
        // Arrange
        when(ciudadRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ciudadService.obtenerPorId(99L);
        });
    }
}
