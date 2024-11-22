package pe.edu.upeu.demoldfx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roy_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "user", nullable = false, unique = true, length = 20)
    @Size(min = 5, max = 20, message = "El nombre de usuario debe tener entre 5 y 20 caracteres")
    private String user;

    @Column(name = "apellido", nullable = false, length = 60)
    @Size(max = 60, message = "El apellido no puede tener más de 60 caracteres")
    private String apellido;

    @Column(name = "telefono", nullable = false, unique = true, length = 9)
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "clave", nullable = false, length = 100)
    private String clave;

    @ManyToOne
    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil",
            nullable = false, foreignKey = @ForeignKey(name = "FK_PERFIL_USUARIO") )
    private Perfil perfil;

}
