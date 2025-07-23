package utnfrc.isi.backend.servicio_pedidos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfrc.isi.backend.servicio_pedidos.modelo.Cliente;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}
