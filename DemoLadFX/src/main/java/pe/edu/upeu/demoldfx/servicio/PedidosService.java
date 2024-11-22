package pe.edu.upeu.demoldfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import pe.edu.upeu.demoldfx.modelo.Pedidos;
import pe.edu.upeu.demoldfx.repositorio.PedidosRepository;

import java.util.List;

@Service

public class PedidosService {
    @Autowired
    PedidosRepository repo;
    //C
    public Pedidos save(Pedidos to){
        return repo.save(to);
    }
    //R
    public List<Pedidos> list(){
        return repo.findAll();
    }
    //U
    public Pedidos update(Pedidos to, Long id){
        try {
            Pedidos toe=repo.findById(id).get();
            if (toe!=null){
                toe.setNombre(to.getNombre());
                toe.setDescripcion(to.getDescripcion());
            }
            return repo.save(toe);

        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return null;
    }

    public Pedidos update(Pedidos to){
        return repo.save(to);
    }

    //D
    public void delete(Long id){
        repo.deleteById(id);
    }

    public Pedidos searchById(Long id){
        return repo.findById(id).orElse(null);
    }

}
