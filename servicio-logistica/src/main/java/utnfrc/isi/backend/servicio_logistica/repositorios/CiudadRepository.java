package utnfrc.isi.backend.servicio_logistica.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfrc.isi.backend.servicio_logistica.modelos.Ciudad;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
}
