package utnfrc.isi.backend.servicio_pedidos.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contenedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "El peso debe ser un valor positivo")
    @Column(name = "PESO")
    private Double peso;

    @Positive(message = "El valor debe ser un valor positivo")
    @Column(name = "VOLUMEN")
    private Double volumen;

    @NotBlank(message = "El estado no puede estar vacio")
    @Size(max = 50, message = "Debe tener maximo 50 caracteres")
    @Column(name = "ESTADO")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID")
    private Cliente cliente;
}
