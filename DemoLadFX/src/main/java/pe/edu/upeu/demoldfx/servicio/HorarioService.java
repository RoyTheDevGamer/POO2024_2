package pe.edu.upeu.demoldfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.demoldfx.dto.ComboBoxOption;
import pe.edu.upeu.demoldfx.modelo.Horario;
import pe.edu.upeu.demoldfx.repositorio.HorarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service

public class HorarioService {
    @Autowired
    HorarioRepository repo;
    //C
    public Horario save(Horario to){
        return repo.save(to);
    }
    //R
    public List<Horario> list(){
        return repo.findAll();
    }
    //U
    public Horario update(Horario to, Long id){
        try {
            Horario toe=repo.findById(id).get();
            if (toe!=null){
                toe.setNombre_horario(to.getNombre_horario());
            }
            return repo.save(toe);

        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return null;
    }

    public Horario update(Horario to){
        return repo.save(to);
    }

    //D
    public void delete(Long id){
        repo.deleteById(id);
    }

    public Horario searchById(Long id){
        return repo.findById(id).orElse(null);
    }

    public List<ComboBoxOption> listarComboBox(){
        List<ComboBoxOption> listar=new ArrayList<>();
        ComboBoxOption cb;
        for (Horario cate : repo.findAll()) {
            cb=new ComboBoxOption();
            cb.setKey(String.valueOf(cate.getIdHorario()));
            cb.setValue(cate.getNombre_horario());
            listar.add(cb);
        }
        return listar;
    }

}
