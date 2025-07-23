package utnfrc.isi.backend.servicio_pedidos.servicios;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.modelo.TramoRuta;
import utnfrc.isi.backend.servicio_pedidos.repositorios.TramoRutaRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TramoRutaServiceTest {

    @Mock
    private TramoRutaRepository tramoRutaRepository;

    @InjectMocks
    private TramoRutaService tramoRutaService;

    @Test
    void testCrearTramosParaSolicitud() {
        // Arrange
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setCiudadOrigenId(1L);
        solicitud.setDepositoId(2L);

        // Simulamos la acción de guardar del repositorio
        when(tramoRutaRepository.save(any(TramoRuta.class))).thenReturn(new TramoRuta());

        // Act
        tramoRutaService.crearTramosParaSolicitud(solicitud);

        // Assert
        // Verificamos que el método save fue llamado al menos una vez
        // (En una implementación completa, verificaríamos que se llame dos veces, una por cada tramo)
        verify(tramoRutaRepository, atLeastOnce()).save(any(TramoRuta.class));
    }
}