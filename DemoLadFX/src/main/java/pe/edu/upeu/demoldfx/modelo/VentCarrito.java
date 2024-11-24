package pe.edu.upeu.demoldfx.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roy_vent_carrito")
public class VentCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    public Long idCarrito;
    @ManyToOne
    @JoinColumn(name = "id_platos", nullable = false)
    public Platos platos;
    @Column(name = "nombre_platos", nullable = false, length = 120)
    public String nombrePlatos;
    @Column(name = "cantidad", nullable = false)
    public Double cantidad;
    @Column(name = "punitario", nullable = false)
    public Double punitario;
    @Column(name = "ptotal", nullable = false)
    public Double ptotal;

}
