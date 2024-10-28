package pe.edu.upeu.demoldfx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.demoldfx.modelo.Platos;

@Repository

public interface PlatosRepository extends JpaRepository<Platos, Long> {
}
