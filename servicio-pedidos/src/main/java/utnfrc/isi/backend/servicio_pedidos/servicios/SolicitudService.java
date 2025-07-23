package utnfrc.isi.backend.servicio_pedidos.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.repositorios.SolicitudRepository;

@Service
public class SolicitudService {
    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private ContenedorService contenedorService;

    @Autowired
    private TramoRutaService tramoRutaService;

    @Autowired
    private RestClient.Builder restClientBuilder;

    public Solicitud crearSolicitud(Solicitud solicitud) {
        contenedorService.obtenerPorId(solicitud.getContenedor().getId());

        double distanciaKm = 700.0;
        double tarifaBase = 5000.0;
        double tarifaKm = 150.5;

        double costoEstimado = tarifaBase + (distanciaKm * tarifaKm);
        double tiempoEstimadoHoras = distanciaKm / 80.0;

        solicitud.setCostoEstimado(costoEstimado);
        solicitud.setTiempoEstimadoHoras(tiempoEstimadoHoras);
        solicitud.setCamionId(1L);

        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);
        tramoRutaService.crearTramosParaSolicitud(solicitudGuardada);

        return solicitudGuardada;
    }

    public Solicitud obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Solicitud no encontrada con ID: " + id));
    }
}
