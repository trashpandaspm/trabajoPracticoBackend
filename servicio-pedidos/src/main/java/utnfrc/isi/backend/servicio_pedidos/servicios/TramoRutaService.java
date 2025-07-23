package utnfrc.isi.backend.servicio_pedidos.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.modelo.TramoRuta;
import utnfrc.isi.backend.servicio_pedidos.repositorios.TramoRutaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TramoRutaService {
    @Autowired
    private TramoRutaRepository tramoRutaRepository;

    public void crearTramosParaSolicitud(Solicitud solicitud) {
        TramoRuta tramo1 = new TramoRuta();
        tramo1.setSolicitud(solicitud);
        tramo1.setOrden(1);
        tramo1.setTipoTramo("ORIGEN_DEPOSITO");
        tramo1.setCiudadOrigenId(solicitud.getCiudadOrigenId());
        tramo1.setCiudadDestinoId(solicitud.getDepositoId());
        tramo1.setFechaEstimadaSalida(LocalDateTime.now().plusDays(1));
        tramo1.setFechaEstimadaLlegada(LocalDateTime.now().plusDays(2));

        tramoRutaRepository.save(tramo1);
    }

    public List<TramoRuta> obtenerPorSolicitud(Solicitud solicitud) {
        return tramoRutaRepository.findBySolicitudOrderByOrdenAsc(solicitud);
    }
}
