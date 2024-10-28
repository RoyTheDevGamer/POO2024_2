package pe.edu.upeu.demoldfx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
@Table(name = "roy_platos")
public class Platos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_platos")
    private Long idPlatos;
    @NotNull(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    @Column(name = "nombre_platos", nullable = false, length = 30)
    private String nombre_platos;
    @PositiveOrZero(message = "El Precio Anterior debe ser positivo o cero")
    @Column(name = "precio_platos", nullable = false, length = 10)
    private Double precio_platos;
    @NotNull(message = "El horario no puede estar vacío")
    @ManyToOne
    @JoinColumn(name = "id_horario", referencedColumnName = "id_horario",
            nullable = false, foreignKey = @ForeignKey(name = "FK_HORARIO_PLATOS") )
    private Horario horario;

}