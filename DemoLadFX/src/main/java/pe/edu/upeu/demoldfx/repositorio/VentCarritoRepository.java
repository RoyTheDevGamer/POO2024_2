package pe.edu.upeu.demoldfx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.demoldfx.modelo.VentCarrito;

import java.util.List;

@Repository
public interface VentCarritoRepository  extends JpaRepository<VentCarrito, Long> {


}