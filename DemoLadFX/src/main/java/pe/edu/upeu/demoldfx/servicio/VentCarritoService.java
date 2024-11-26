package pe.edu.upeu.demoldfx.servicio;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.demoldfx.modelo.VentCarrito;
import pe.edu.upeu.demoldfx.repositorio.VentCarritoRepository;

import java.util.List;

@Service
public class VentCarritoService {
    @Autowired
    VentCarritoRepository repo;

    public VentCarrito save(VentCarrito to) {
        return repo.save(to);
    }

    public List<VentCarrito> list() {
        return repo.findAll();
    }

    public VentCarrito update(VentCarrito to, Long id) {
        try {
            VentCarrito toe = repo.findById(id).orElse(null);
            if (toe != null) {
                toe.setNombrePlatos(to.getNombrePlatos());
                return repo.save(toe);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public VentCarrito searchById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public void deleteAll() {
        repo.deleteAll(); // Este método elimina todos los registros de la tabla
    }

//    public List<VentCarrito> listaCarritoCliente(String dni) {
//        return repo.listaCarritoCliente(dni);
//    }
//
//    @Transactional
//    public void deleteCarAll(String dni) {
//        this.repo.deleteByDni(dni);
//    }

}
