package utnfrc.isi.backend.servicio_pedidos.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.modelo.TramoRuta;
import utnfrc.isi.backend.servicio_pedidos.repositorios.ContenedorRepository;
import utnfrc.isi.backend.servicio_pedidos.repositorios.TramoRutaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TramoRutaService {
    @Autowired
    private TramoRutaRepository tramoRutaRepository;
    @Autowired
    private ContenedorRepository contenedorRepository;

    public void crearTramosParaSolicitud(Solicitud solicitud) {
        // Calculamos las fechas estimadas.
        // Asumimos que el primer tramo toma la mitad del tiempo total.
        LocalDateTime ahora = LocalDateTime.now();
        long horasTramo1 = (long) (solicitud.getTiempoEstimadoHoras() / 2.0);
        LocalDateTime llegadaEstimadaDeposito = ahora.plusHours(horasTramo1);

        // Tramo 1: Desde el origen hasta el depósito asignado
        TramoRuta tramoOrigenADeposito = new TramoRuta();
        tramoOrigenADeposito.setSolicitud(solicitud);
        tramoOrigenADeposito.setOrden(1);
        tramoOrigenADeposito.setTipoTramo("ORIGEN_DEPOSITO");
        tramoOrigenADeposito.setCiudadOrigenId(solicitud.getCiudadOrigenId());
        tramoOrigenADeposito.setCiudadDestinoId(solicitud.getDepositoId());
        // ================== INICIO DE LA CORRECCIÓN ==================
        tramoOrigenADeposito.setFechaEstimadaSalida(ahora); // La salida es ahora
        tramoOrigenADeposito.setFechaEstimadaLlegada(llegadaEstimadaDeposito);
        // =================== FIN DE LA CORRECCIÓN ====================
        tramoRutaRepository.save(tramoOrigenADeposito);

        // Tramo 2: Desde el depósito hasta el destino final
        long horasTramo2 = (long) (solicitud.getTiempoEstimadoHoras() - horasTramo1);
        LocalDateTime llegadaEstimadaFinal = llegadaEstimadaDeposito.plusHours(horasTramo2);

        TramoRuta tramoDepositoADestino = new TramoRuta();
        tramoDepositoADestino.setSolicitud(solicitud);
        tramoDepositoADestino.setOrden(2);
        tramoDepositoADestino.setTipoTramo("DEPOSITO_DESTINO");
        tramoDepositoADestino.setCiudadOrigenId(solicitud.getDepositoId());
        tramoDepositoADestino.setCiudadDestinoId(solicitud.getCiudadDestinoId());
        // ================== INICIO DE LA CORRECCIÓN ==================
        tramoDepositoADestino.setFechaEstimadaSalida(llegadaEstimadaDeposito);
        tramoDepositoADestino.setFechaEstimadaLlegada(llegadaEstimadaFinal);
        // =================== FIN DE LA CORRECCIÓN ====================
        tramoRutaRepository.save(tramoDepositoADestino);
    }

    public List<TramoRuta> obtenerPorSolicitud(Solicitud solicitud) {
        return tramoRutaRepository.findBySolicitudOrderByOrdenAsc(solicitud);
    }
    public TramoRuta registrarLlegada(Long tramoId) {
        // 1. Busca el tramo a actualizar.
        TramoRuta tramo = tramoRutaRepository.findById(tramoId)
                .orElseThrow(() -> new ResourceNotFoundException("Tramo de ruta no encontrado con ID: " + tramoId));

        // 2. Actualiza la fecha de llegada real.
        tramo.setFechaRealLlegada(LocalDateTime.now());
        TramoRuta tramoActualizado = tramoRutaRepository.save(tramo);

        // 3. Lógica de negocio: Verifica si este era el último tramo.
        Solicitud solicitud = tramo.getSolicitud();
        List<TramoRuta> todosLosTramos = tramoRutaRepository.findBySolicitudOrderByOrdenAsc(solicitud);

        // Si el último tramo de la lista tiene fecha de llegada, el viaje terminó.
        if (todosLosTramos.get(todosLosTramos.size() - 1).getFechaRealLlegada() != null) {
            Contenedor contenedor = solicitud.getContenedor();
            contenedor.setEstado("ENTREGADO");
            // Guardamos la solicitud para persistir el cambio en el contenedor asociado.
            contenedorRepository.save(contenedor);
        }

        return tramoActualizado;
    }
    public TramoRuta registrarSalida(Long tramoId) {
        TramoRuta tramo = tramoRutaRepository.findById(tramoId)
                .orElseThrow(() -> new ResourceNotFoundException("Tramo de ruta no encontrado con ID: " + tramoId));
        tramo.setFechaRealSalida(LocalDateTime.now());
        return tramoRutaRepository.save(tramo);
    }
    public List<TramoRuta> obtenerTodos() {
        return tramoRutaRepository.findAll();
    }
}
