package pe.edu.upeu.demoldfx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roy_perfil")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_perfil", nullable = false)
    private Long idPerfil;
    @Size(max = 40)
    @Column(length = 40)
    private String nombre;
    @Size(max = 6)
    @Column(length = 6)
    private String codigo;
}