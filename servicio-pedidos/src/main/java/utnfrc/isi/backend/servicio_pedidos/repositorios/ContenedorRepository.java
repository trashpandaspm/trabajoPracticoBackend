package utnfrc.isi.backend.servicio_pedidos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfrc.isi.backend.servicio_pedidos.modelo.Contenedor;

import java.util.List;

public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {
    List<Contenedor> findByClienteId(Long clienteId);

    List<Contenedor> findByEstado(String estado);
}
