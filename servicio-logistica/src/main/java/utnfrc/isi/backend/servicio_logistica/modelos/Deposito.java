package utnfrc.isi.backend.servicio_logistica.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "CIUDAD_ID")
    private Ciudad ciudad;

    @NotBlank(message = "La direccion no puede estar vacia")
    @Size(max = 255, message = "El largo maximo es de 255 caracteres")
    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "LATITUD")
    private Double latitud;

    @Column(name = "LONGITUD")
    private Double longitud;
}
