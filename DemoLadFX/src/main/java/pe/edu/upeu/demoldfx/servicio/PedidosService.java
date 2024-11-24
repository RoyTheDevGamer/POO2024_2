package pe.edu.upeu.demoldfx.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import pe.edu.upeu.demoldfx.dto.ModeloDataAutocomplet;
import pe.edu.upeu.demoldfx.modelo.Pedidos;
import pe.edu.upeu.demoldfx.modelo.Platos;
import pe.edu.upeu.demoldfx.repositorio.PedidosRepository;

import java.util.ArrayList;
import java.util.List;

@Service

public class PedidosService {
    @Autowired
    PedidosRepository repo;
    Logger logger= LoggerFactory.getLogger(PedidosService.class);
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
    public List<ModeloDataAutocomplet> listAutoCompletProducto() {
        List<ModeloDataAutocomplet> listarProducto = new ArrayList<>();
        try {
            for (Pedidos pedidos : repo.findAll()) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(String.valueOf(pedidos.getIdPedidos()));
                data.setNameDysplay(pedidos.getNombre());
                data.setOtherData(pedidos.getNombre() + ":" + pedidos.getPrecio());
                listarProducto.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al realizar la busqueda", e);
        }
        return listarProducto;
    }

}
