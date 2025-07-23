package utnfrc.isi.backend.servicio_logistica.repositorios;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import utnfrc.isi.backend.servicio_logistica.modelos.Tarifa;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TarifaRepositoryTest {

    @Autowired
    private TarifaRepository tarifaRepository;

    @Test
    void testGuardarYBuscarTarifa() {
        // Arrange
        Tarifa nuevaTarifa = new Tarifa(null, 5000.0, 150.5, 2500.0);

        // Act
        Tarifa tarifaGuardada = tarifaRepository.save(nuevaTarifa);

        // Assert
        assertNotNull(tarifaGuardada.getId());
        assertEquals(5000.0, tarifaGuardada.getMontoBase());
    }
}