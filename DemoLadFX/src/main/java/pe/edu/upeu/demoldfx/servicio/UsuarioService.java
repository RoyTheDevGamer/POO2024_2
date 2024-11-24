package pe.edu.upeu.demoldfx.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.demoldfx.dto.ModeloDataAutocomplet;
import pe.edu.upeu.demoldfx.modelo.Pedidos;
import pe.edu.upeu.demoldfx.modelo.Platos;
import pe.edu.upeu.demoldfx.modelo.Usuario;
import pe.edu.upeu.demoldfx.repositorio.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repo;
    Logger logger= LoggerFactory.getLogger(UsuarioService.class);

    public Usuario save(Usuario to) {
        return repo.save(to);
    }

    public List<Usuario> list() {
        return repo.findAll();
    }

    public Usuario update(Usuario to, Long id) {
        try {
            Usuario toe = repo.findById(id).orElse(null);
            if (toe != null) {
                toe.setClave(to.getClave());
                return repo.save(toe);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
    public Usuario update(Usuario to){
        return repo.save(to);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Usuario searchById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Usuario loginUsuario(String user, String clave) {
        return repo.loginUsuario(user, clave);
    }

    public List<ModeloDataAutocomplet> listAutoCompletProducto() {
        List<ModeloDataAutocomplet> listarProducto = new ArrayList<>();
        try {
            for (Usuario usuario : repo.findAll()) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(String.valueOf(usuario.getIdUsuario()));
                data.setNameDysplay(usuario.getUser());
                data.setOtherData(usuario.getUser() + ":" + usuario.getDni());
                listarProducto.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al realizar la busqueda", e);
        }
        return listarProducto;
    }

}
