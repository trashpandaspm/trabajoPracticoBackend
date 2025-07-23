package utnfrc.isi.backend.servicio_logistica.repositorios;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import utnfrc.isi.backend.servicio_logistica.modelos.Ciudad;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CiudadRepositoryTest {

    @Autowired
    private CiudadRepository ciudadRepository;

    @Test
    void testGuardarYBuscarCiudad() {
        // Arrange
        Ciudad nuevaCiudad = new Ciudad();
        nuevaCiudad.setNombre("Córdoba");
        nuevaCiudad.setLatitud(-31.42);
        nuevaCiudad.setLongitud(-64.18);

        // Act
        Ciudad ciudadGuardada = ciudadRepository.save(nuevaCiudad);
        Optional<Ciudad> ciudadEncontrada = ciudadRepository.findById(ciudadGuardada.getId());

        // Assert
        assertNotNull(ciudadGuardada.getId());
        assertTrue(ciudadEncontrada.isPresent());
        assertEquals("Córdoba", ciudadEncontrada.get().getNombre());
    }
}
