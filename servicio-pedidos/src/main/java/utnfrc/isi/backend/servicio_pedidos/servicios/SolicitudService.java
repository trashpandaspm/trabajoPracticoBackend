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
import utnfrc.isi.backend.servicio_pedidos.modelo.TramoRuta;
import utnfrc.isi.backend.servicio_pedidos.repositorios.SolicitudRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        try {
            System.out.println("=== INICIO CREACIÓN SOLICITUD ===");

            // 1. Obtener y asignar el contenedor completo
            Contenedor contenedorCompleto = contenedorService.obtenerPorId(solicitud.getContenedor().getId());
            solicitud.setContenedor(contenedorCompleto);
            System.out.println("Contenedor obtenido: peso=" + contenedorCompleto.getPeso() + ", volumen=" + contenedorCompleto.getVolumen());

            // 2. Obtener datos externos (ciudades, tarifa, depósito)
            RestClient restClientLogistica = builder.build();

            System.out.println("Consultando ciudad origen ID: " + solicitud.getCiudadOrigenId());
            CiudadDTO ciudadOrigen = restClientLogistica.get()
                    .uri("http://servicio-logistica:8083/api/ciudades/" + solicitud.getCiudadOrigenId())
                    .retrieve()
                    .body(CiudadDTO.class);

            System.out.println("Consultando ciudad destino ID: " + solicitud.getCiudadDestinoId());
            CiudadDTO ciudadDestino = restClientLogistica.get()
                    .uri("http://servicio-logistica:8083/api/ciudades/" + solicitud.getCiudadDestinoId())
                    .retrieve()
                    .body(CiudadDTO.class);

            System.out.println("Consultando depósito ID: " + solicitud.getDepositoId());
            CiudadDTO ciudadDeposito = restClientLogistica.get()
                    .uri("http://servicio-logistica:8083/api/ciudades/" + solicitud.getDepositoId())
                    .retrieve()
                    .body(CiudadDTO.class);

            System.out.println("Consultando tarifa actual...");
            TarifaDTO tarifa = restClientLogistica.get()
                    .uri("http://servicio-logistica:8083/api/tarifas/actual")
                    .retrieve()
                    .body(TarifaDTO.class);

            // Validar que se obtuvieron los datos necesarios
            if (ciudadOrigen == null || ciudadDestino == null || ciudadDeposito == null || tarifa == null) {
                throw new IllegalStateException("No se pudieron obtener todos los datos necesarios del servicio de logística");
            }

            System.out.println("Tarifa obtenida: base=" + tarifa.getMontoBase() + ", porKm=" + tarifa.getCostoKm());

            // 3. Cálculo de distancias REALES según el enunciado:
            // origen → depósito + depósito → destino
            double distanciaOrigenADeposito = 0.0;
            double distanciaDepositoADestino = 0.0;

            try {
                String origenCoords = ciudadOrigen.getLatitud() + "," + ciudadOrigen.getLongitud();
                String depositoCoords = ciudadDeposito.getLatitud() + "," + ciudadDeposito.getLongitud();
                String destinoCoords = ciudadDestino.getLatitud() + "," + ciudadDestino.getLongitud();

                System.out.println("Calculando distancia origen->depósito...");
                distanciaOrigenADeposito = obtenerDistanciaReal(origenCoords, depositoCoords);

                System.out.println("Calculando distancia depósito->destino...");
                distanciaDepositoADestino = obtenerDistanciaReal(depositoCoords, destinoCoords);

                System.out.println("Distancia origen->depósito: " + distanciaOrigenADeposito + " km");
                System.out.println("Distancia depósito->destino: " + distanciaDepositoADestino + " km");

            } catch (Exception e) {
                System.err.println("ALERTA: Falla en API de distancia. Usando distancias por defecto. Error: " + e.getMessage());
                distanciaOrigenADeposito = 250.0; // Distancia por defecto
                distanciaDepositoADestino = 250.0;
            }

            // Validar distancias
            if (Double.isNaN(distanciaOrigenADeposito) || distanciaOrigenADeposito < 0) {
                distanciaOrigenADeposito = 250.0;
            }
            if (Double.isNaN(distanciaDepositoADestino) || distanciaDepositoADestino < 0) {
                distanciaDepositoADestino = 250.0;
            }

            double distanciaTotal = distanciaOrigenADeposito + distanciaDepositoADestino;
            System.out.println("Distancia total del recorrido: " + distanciaTotal + " km");

            // 4. Encontrar camión disponible
            System.out.println("Buscando camión disponible...");
            CamionDTO camionAsignado = restClientLogistica.get()
                    .uri("http://servicio-logistica:8083/api/camiones/disponible?peso={peso}&volumen={volumen}",
                            contenedorCompleto.getPeso(), contenedorCompleto.getVolumen())
                    .retrieve()
                    .body(CamionDTO.class);

            if (camionAsignado == null) {
                throw new IllegalStateException("No se encontraron camiones disponibles para el peso y volumen requeridos.");
            }
            System.out.println("Camión asignado ID: " + camionAsignado.getId());

            // 5. CÁLCULO DEL COSTO SEGÚN EL ENUNCIADO:
            // Base fija + costo por kilómetro + ajuste por peso/volumen
            double costoBase = tarifa.getMontoBase();
            double costoDistancia = distanciaTotal * tarifa.getCostoKm();

            // Factor adicional por peso/volumen del contenedor (puedes ajustar esta lógica)
            double factorPeso = contenedorCompleto.getPeso() / 1000.0; // Por cada tonelada
            double factorVolumen = contenedorCompleto.getVolumen() / 10.0; // Por cada 10 m³
            double costoContenedor = (factorPeso + factorVolumen) * 50.0; // $50 por factor

            double costoEstimado = costoBase + costoDistancia + costoContenedor;

            // 6. CÁLCULO DEL TIEMPO ESTIMADO
            // Basado en velocidad promedio y tiempo de carga/descarga
            double velocidadPromedio = 80.0; // km/h
            double tiempoViaje = distanciaTotal / velocidadPromedio;
            double tiempoCarga = 2.0; // 2 horas para carga/descarga en depósito
            double tiempoEstimadoHoras = tiempoViaje + tiempoCarga;

            System.out.println("=== CÁLCULOS FINALES ===");
            System.out.println("Costo base: $" + costoBase);
            System.out.println("Costo por distancia: $" + costoDistancia);
            System.out.println("Costo por contenedor: $" + costoContenedor);
            System.out.println("Costo estimado total: $" + costoEstimado);
            System.out.println("Tiempo estimado: " + tiempoEstimadoHoras + " horas");

            // 7. Asignar valores calculados
            solicitud.setCostoEstimado(costoEstimado);
            solicitud.setTiempoEstimadoHoras(tiempoEstimadoHoras);
            solicitud.setCamionId(camionAsignado.getId());

            // 8. Guardar y crear tramos
            Solicitud solicitudGuardada = solicitudRepository.save(solicitud);
            tramoRutaService.crearTramosParaSolicitud(solicitudGuardada);

            System.out.println("=== SOLICITUD CREADA EXITOSAMENTE ===");
            return solicitudGuardada;

        } catch (Exception e) {
            System.err.println("=== ERROR AL CREAR SOLICITUD ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Solicitud obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Solicitud no encontrada con ID: " + id));
    }

    public Solicitud calcularCostoFinal(Long solicitudId) {
        try {
            // 1. Busca la solicitud y sus tramos.
            Solicitud solicitud = obtenerPorId(solicitudId);
            List<TramoRuta> tramos = tramoRutaService.obtenerPorSolicitud(solicitud);

            // Aseguramos que el viaje haya finalizado.
            if (!"ENTREGADO".equals(solicitud.getContenedor().getEstado())) {
                throw new IllegalStateException("La solicitud aún no ha sido completada.");
            }

            // 2. Lógica para calcular los días de estadía en el depósito.
            LocalDateTime llegadaDeposito = null;
            LocalDateTime salidaDeposito = null;

            for (TramoRuta tramo : tramos) {
                if (tramo.getOrden() == 1 && tramo.getFechaRealLlegada() != null) {
                    llegadaDeposito = tramo.getFechaRealLlegada();
                }
                if (tramo.getOrden() == 2 && tramo.getFechaRealSalida() != null) {
                    salidaDeposito = tramo.getFechaRealSalida();
                }
            }

            if (llegadaDeposito == null || salidaDeposito == null) {
                throw new IllegalStateException("Faltan registrar fechas reales de llegada o salida del depósito.");
            }

            // Calculamos la diferencia de días (redondeando hacia arriba).
            long horasDeEstadia = ChronoUnit.HOURS.between(llegadaDeposito, salidaDeposito);
            long diasDeEstadia = Math.max(1, (long) Math.ceil(horasDeEstadia / 24.0)); // Mínimo 1 día

            // 3. Obtener la tarifa desde el servicio-logistica.
            RestClient restClientLogistica = builder.build();
            TarifaDTO tarifa = restClientLogistica.get()
                    .uri("http://servicio-logistica:8083/api/tarifas/actual")
                    .retrieve()
                    .body(TarifaDTO.class);

            if (tarifa == null) {
                throw new IllegalStateException("No se pudo obtener la tarifa actual");
            }

            // 4. Calcular el costo final y actualizar la solicitud.
            double costoEstadia = diasDeEstadia * tarifa.getCostoDiaDeposito();
            double costoBase = solicitud.getCostoEstimado() != null ? solicitud.getCostoEstimado() : 0.0;
            double costoFinal = costoBase + costoEstadia;

            solicitud.setCostoFinal(costoFinal);

            return solicitudRepository.save(solicitud);

        } catch (Exception e) {
            System.err.println("Error al calcular costo final: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
