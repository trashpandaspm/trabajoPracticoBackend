package utnfrc.isi.backend.servicio_logistica.repositorios;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import utnfrc.isi.backend.servicio_logistica.modelos.Camion;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CamionRepositoryTest {

    @Autowired
    private CamionRepository camionRepository;

    @Test
    void testFindCamionDisponibleConCapacidadSuficiente() {
        // Arrange: Guardamos varios camiones con distintas características
        camionRepository.save(new Camion(null, 20000.0, 80.0, true)); // Cumple
        camionRepository.save(new Camion(null, 10000.0, 50.0, true)); // No cumple por peso
        camionRepository.save(new Camion(null, 25000.0, 70.0, true)); // No cumple por volumen
        camionRepository.save(new Camion(null, 22000.0, 90.0, false)); // Cumple capacidad pero no está disponible
        camionRepository.save(new Camion(null, 30000.0, 100.0, true)); // Cumple

        // Act: Ejecutamos la consulta derivada con los requisitos
        double pesoRequerido = 15000.0;
        double volumenRequerido = 75.0;
        List<Camion> camiones = camionRepository
                .findByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(
                        pesoRequerido, volumenRequerido);

        // Assert: Verificamos que se encontraron solo los dos camiones que cumplen todas las condiciones
        assertNotNull(camiones);
        assertEquals(2, camiones.size());
    }
}