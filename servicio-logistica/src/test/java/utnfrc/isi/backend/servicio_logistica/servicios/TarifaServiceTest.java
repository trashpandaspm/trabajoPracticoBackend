package utnfrc.isi.backend.servicio_logistica.servicios;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utnfrc.isi.backend.servicio_logistica.modelos.Tarifa;
import utnfrc.isi.backend.servicio_logistica.repositorios.TarifaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TarifaServiceTest {

    @Mock
    private TarifaRepository tarifaRepository;

    @InjectMocks
    private TarifaService tarifaService;

    @Test
    void cuandoExistenTarifas_entoncesDevuelveLaPrimera() {
        // Arrange
        Tarifa tarifa = new Tarifa(1L, 5000.0, 150.5, 2500.0);
        when(tarifaRepository.findAll()).thenReturn(List.of(tarifa));

        // Act
        Tarifa tarifaActual = tarifaService.obtenerTarifaActual();

        // Assert
        assertNotNull(tarifaActual);
        assertEquals(5000.0, tarifaActual.getMontoBase());
    }

    @Test
    void cuandoNoExistenTarifas_entoncesLanzaExcepcion() {
        // Arrange
        when(tarifaRepository.findAll()).thenReturn(List.of());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            tarifaService.obtenerTarifaActual();
        });

        assertEquals("No se encontraron tarifas configuradas.", exception.getMessage());
    }
}
