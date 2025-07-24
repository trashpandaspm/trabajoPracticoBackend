package utnfrc.isi.backend.servicio_pedidos.servicios;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import utnfrc.isi.backend.servicio_pedidos.dto.CamionDTO;
import utnfrc.isi.backend.servicio_pedidos.dto.CiudadDTO;
import utnfrc.isi.backend.servicio_pedidos.dto.TarifaDTO;
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
    private RestClient.Builder builder;

    @Value("${google.maps.apikey}")
    private String apiKey;

    public Double obtenerDistanciaReal(String origen, String destino){
        try {
            RestClient client =
                    builder.baseUrl("https://maps.googleapis.com/maps/api").build();

            String url = "/distancematrix/json?origins=" + origen +
                    "&destinations=" + destino +
                    "&units=metric&key=" + apiKey;

            ResponseEntity<String> response =
                    client.get().uri(url).retrieve().toEntity(String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode leg = root.path("rows").get(0).path("elements").get(0);

            // Devuelve la distancia en kilómetros
            return leg.path("distance").path("value").asDouble() / 1000;
        } catch (Exception e) {
            // En un caso real, manejarías este error de forma más elegante
            System.err.println("Error al calcular la distancia: " + e.getMessage());
            // Devolvemos un valor por defecto o lanzamos una excepción de negocio
            return 700.0; // Mantenemos el valor simulado como fallback
        }
    }

    public Solicitud crearSolicitud(Solicitud solicitud) {
        // 1. Obtener el ID del contenedor que viene en el JSON.
        Long contenedorId = solicitud.getContenedor().getId();

        // 2. Usar el servicio para obtener la entidad Contenedor COMPLETA desde la BD.
        Contenedor contenedorCompleto = contenedorService.obtenerPorId(contenedorId);

        // 3. Asignar la entidad completa y gestionada a la solicitud.
        solicitud.setContenedor(contenedorCompleto);

        RestClient restClientLogistica = builder.build();

        // Obtener coordenadas de la ciudad de origen
        CiudadDTO ciudadOrigen = restClientLogistica.get()
                .uri("http://servicio-logistica:8083/api/ciudades/" + solicitud.getCiudadOrigenId())
                .retrieve()
                .body(CiudadDTO.class);

        // Obtener coordenadas de la ciudad de destino
        CiudadDTO ciudadDestino = restClientLogistica.get()
                .uri("http://servicio-logistica:8083/api/ciudades/" + solicitud.getCiudadDestinoId())
                .retrieve()
                .body(CiudadDTO.class);

        // Obtener tarifas
        TarifaDTO tarifa = restClientLogistica.get()
                .uri("http://servicio-logistica:8083/api/tarifas/actual")
                .retrieve()
                .body(TarifaDTO.class);

        // 3. Formatear coordenadas y llamar al método auxiliar de Google Maps
        String origenCoords = ciudadOrigen.getLatitud() + "," + ciudadOrigen.getLongitud();
        String destinoCoords = ciudadDestino.getLatitud() + "," + ciudadDestino.getLongitud();

        // Llamamos a nuestro nuevo método privado
        double distanciaKm = obtenerDistanciaReal(origenCoords, destinoCoords);

        // 4. Realizar los cálculos con los datos reales
        double costoEstimado = tarifa.getMontoBase() + (distanciaKm * tarifa.getCostoKm());
        double tiempoEstimadoHoras = distanciaKm / 80.0; // Asumiendo 80 km/h

        // 5. Encontrar y asignar un camión disponible
        CamionDTO camionAsignado = restClientLogistica.get()
                .uri("http://servicio-logistica:8083/api/camiones/disponible?peso={peso}&volumen={volumen}",
                        contenedorCompleto.getPeso(), contenedorCompleto.getVolumen())
                .retrieve()
                .body(CamionDTO.class);

        // --- FIN DE LA INTEGRACIÓN ---

        solicitud.setCostoEstimado(costoEstimado);
        solicitud.setTiempoEstimadoHoras(tiempoEstimadoHoras);
        solicitud.setCamionId(camionAsignado.getId());

        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);
        tramoRutaService.crearTramosParaSolicitud(solicitudGuardada);

        return solicitudGuardada;
    }

    public Solicitud obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Solicitud no encontrada con ID: " + id));
    }
}
