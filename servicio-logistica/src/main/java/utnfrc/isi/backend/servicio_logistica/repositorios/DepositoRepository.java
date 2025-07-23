package utnfrc.isi.backend.servicio_logistica.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfrc.isi.backend.servicio_logistica.modelos.Deposito;

public interface DepositoRepository extends JpaRepository<Deposito, Long> {
}
