package pe.edu.upeu.demoldfx.repositorio;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.demoldfx.modelo.Venta;

@Repository
public interface VentaRepository  extends JpaRepository<Venta, Long> {
}