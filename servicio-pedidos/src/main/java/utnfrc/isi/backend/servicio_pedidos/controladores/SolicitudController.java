package utnfrc.isi.backend.servicio_pedidos.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfrc.isi.backend.servicio_pedidos.modelo.Solicitud;
import utnfrc.isi.backend.servicio_pedidos.servicios.ResourceNotFoundException;
import utnfrc.isi.backend.servicio_pedidos.servicios.SolicitudService;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
    @Autowired
    private SolicitudService solicitudService;

    @GetMapping("/{id}")
    public Solicitud obtenerPorId(@PathVariable Long id) {
        return solicitudService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<?> crearSolicitud(@RequestBody Solicitud solicitud) {
        try {
            Solicitud solicitudGuardada = solicitudService.crearSolicitud(solicitud);
            return new ResponseEntity<>(solicitudGuardada, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Error de negocio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/finalizar-costo")
    public ResponseEntity<?> finalizarCosto(@PathVariable Long id) {
        try {
            Solicitud solicitud = solicitudService.calcularCostoFinal(id);
            return ResponseEntity.ok(solicitud);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Error de negocio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }

    // Manejo global de excepciones para este controlador
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
