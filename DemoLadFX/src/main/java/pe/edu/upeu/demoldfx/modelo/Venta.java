package pe.edu.upeu.demoldfx.modelo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roy_venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;
    @Column(name = "preciobase", nullable = false)
    private Double precioBase;
    @Column(name = "igv", nullable = false)
    private Double igv;
    @Column(name = "preciototal", nullable = false)
    private Double precioTotal;
    @ManyToOne
    @JoinColumn(name = "dni", referencedColumnName = "dni",
            nullable = false, foreignKey = @ForeignKey(name = "FK_USUARIO_VENTA"))
    private Usuario cliente;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario",
            nullable = false, foreignKey = @ForeignKey(name = "FK_USUARIO_VENTA"))
    private Usuario usuario;
    @Column(name = "num_doc", nullable = false, length = 20)
    private String numDoc;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_gener", nullable = false)
    private LocalDateTime fechaGener;

//    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<VentaDetalle> ventaDetalles;
}

