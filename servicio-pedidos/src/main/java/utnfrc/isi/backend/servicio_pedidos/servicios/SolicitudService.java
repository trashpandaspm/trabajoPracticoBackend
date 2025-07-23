package utnfrc.isi.backend.servicio_pedidos.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
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
        // 1. Obtener el ID del contenedor que viene en el JSON.
        Long contenedorId = solicitud.getContenedor().getId();

        // 2. Usar el servicio para obtener la entidad Contenedor COMPLETA desde la BD.
        Contenedor contenedorCompleto = contenedorService.obtenerPorId(contenedorId);

        // 3. Asignar la entidad completa y gestionada a la solicitud.
        solicitud.setContenedor(contenedorCompleto);

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
