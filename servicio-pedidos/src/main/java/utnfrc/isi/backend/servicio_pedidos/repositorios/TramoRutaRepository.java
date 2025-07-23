package utnfrc.isi.backend.servicio_pedidos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.modelo.TramoRuta;

import java.util.List;

public interface TramoRutaRepository extends JpaRepository<TramoRuta, Long> {
    List<TramoRuta> findBySolicitudOrderByOrdenAsc(Solicitud solicitud);
}
