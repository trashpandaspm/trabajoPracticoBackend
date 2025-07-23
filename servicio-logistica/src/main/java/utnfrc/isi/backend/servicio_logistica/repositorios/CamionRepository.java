package utnfrc.isi.backend.servicio_logistica.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfrc.isi.backend.servicio_logistica.modelos.Camion;

import java.util.List;

public interface CamionRepository extends JpaRepository<Camion, Long> {
    List<Camion> findByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(
            Double pesoRequerido, Double volumenRequerido);
}
