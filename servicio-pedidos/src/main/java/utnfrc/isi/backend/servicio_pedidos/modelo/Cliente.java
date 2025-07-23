package utnfrc.isi.backend.servicio_pedidos.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(max = 100, message = "El largo maximo es de 100 caracteres")
    @Column(name = "NOMBRE")
    private String nombre;

    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El largo maximo es de 100 caracteres")
    @Column(name = "EMAIL")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacia")
    @Size(max = 100, message = "El largo maximo es de 100 caracteres")
    @Column(name = "PASSWORD")
    private String password;
}
