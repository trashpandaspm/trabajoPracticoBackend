package utnfrc.isi.backend.servicio_pedidos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;

import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    Optional<Solicitud> findByContenedorId(Long contenedorId);
}
