package pe.edu.upeu.demoldfx.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pe.edu.upeu.demoldfx.componente.Toast;
import pe.edu.upeu.demoldfx.dto.SessionManager;
import pe.edu.upeu.demoldfx.modelo.Usuario;
import pe.edu.upeu.demoldfx.servicio.UsuarioService;

import java.io.IOException;

@Component
public class LoginController {
    @Autowired
    private ApplicationContext context;
    @Autowired
    UsuarioService us;
    @FXML
    TextField txtUsuario;
    @FXML
    PasswordField txtClave;
    @FXML
    Button btnIngresar;
    @FXML
    Button btnRegistrarme;
    private Parent parent;
    Stage stage;

    @FXML
    public void login(ActionEvent event) throws IOException {
        System.out.println("Botón de login presionado.");
        try {
            Usuario usu=us.loginUsuario(txtUsuario.getText(),
                    new String(txtClave.getText()));
            if (usu!=null) {
                SessionManager.getInstance().setUserId(usu.getIdUsuario());
                SessionManager.getInstance().setUserName(usu.getUser());
                SessionManager.getInstance().setNombrePerfil(usu.getPerfil().getNombre());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
                fxmlLoader.setControllerFactory(context::getBean);
                parent= fxmlLoader.load();

                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.setTitle("Pantalla Principal By Roy");
                stage.setResizable(false);
                stage.show();
                stage.sizeToScene();
                stage.centerOnScreen();

            } else {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                double with=stage.getWidth()*2;
                double h=stage.getHeight()/2;
                System.out.println(with + " h:"+h);
                Toast.showToast(stage, "Credencial invalido!! intente nuevamente", 2000, with, h);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void registro() {
        System.out.println("Botón de register presionado.");
            try {


                Stage stage = (Stage) txtUsuario.getScene().getWindow();  // O usa cualquier control de la escena
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/register.fxml"));
                fxmlLoader.setControllerFactory(context::getBean);  // Si usas Spring
                parent = fxmlLoader.load();

                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.setTitle("Register Restaurant By Roy");
                stage.setResizable(false);
                stage.show();
                stage.sizeToScene();
                stage.centerOnScreen();

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

}
