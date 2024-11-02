package pe.edu.upeu.demoldfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.demoldfx.modelo.Platos;
import pe.edu.upeu.demoldfx.repositorio.PlatosRepository;

import java.util.List;

@Service

public class PlatosService {
    @Autowired
    PlatosRepository repo;
    //C
    public Platos save(Platos to){
        return repo.save(to);
    }
    //R
    public List<Platos> list(){
        return repo.findAll();
    }
    //U
    public Platos update(Platos to, Long id){
        try {
            Platos toe=repo.findById(id).get();
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

    public Platos update(Platos to){
        return repo.save(to);
    }

    //D
    public void delete(Long id){
        repo.deleteById(id);
    }

    public Platos searchById(Long id){
        return repo.findById(id).orElse(null);
    }

}
