package pe.edu.upeu.demoldfx.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Component
public class MenuController {
    @Autowired
    private ApplicationContext context;
    @FXML
    private TextField nombreField;

    @FXML
    private StackPane contenedorPrincipal;

    @FXML
    private Menu panelMenu;
    @FXML
    private Menu mesasMenu;
    @FXML
    private Menu platosMenu;
    @FXML
    private Menu pedidosMenu;
    @FXML
    private Menu datosMenu;
    @FXML
    private Menu usuarioMenu;
    @FXML
    private Menu permisosMenu;

    private String rolUsuario;



    public void setRolUsuario(String rol) {
        this.rolUsuario = rol;
        System.out.println("Rol recibido en setRolUsuario: " + rol); // Verificar si el rol llega
        configurarAccesos();
    }

    private void configurarAccesos() {
        System.out.println("Configurando accesos para el rol: " + rolUsuario);
        switch (rolUsuario) {
            case "1":
                panelMenu.setVisible(true);
                mesasMenu.setVisible(true);
                platosMenu.setVisible(false);
                pedidosMenu.setVisible(true);
                datosMenu.setVisible(false);
                usuarioMenu.setVisible(false);
                permisosMenu.setVisible(false);
                break;
            case "2":
                panelMenu.setVisible(true);
                mesasMenu.setVisible(true);
                platosMenu.setVisible(true);
                pedidosMenu.setVisible(true);
                datosMenu.setVisible(true);
                usuarioMenu.setVisible(true);
                permisosMenu.setVisible(true);
                break;
            case "3":
                panelMenu.setVisible(true);
                mesasMenu.setVisible(true);
                platosMenu.setVisible(true);
                pedidosMenu.setVisible(true);
                datosMenu.setVisible(false);
                usuarioMenu.setVisible(false);
                permisosMenu.setVisible(false);
                break;
            case "4":
                panelMenu.setVisible(true);
                mesasMenu.setVisible(true);
                platosMenu.setVisible(false);
                pedidosMenu.setVisible(true);
                datosMenu.setVisible(false);
                usuarioMenu.setVisible(false);
                permisosMenu.setVisible(false);
                break;
            default:
                panelMenu.setVisible(false);
                mesasMenu.setVisible(false);
                platosMenu.setVisible(false);
                pedidosMenu.setVisible(false);
                datosMenu.setVisible(false);
                usuarioMenu.setVisible(false);
                permisosMenu.setVisible(false);
                break;
        }
        System.out.println("Configuración de accesos completada"); // Confirmación de ejecución
    }

    // Método para cargar las diferentes vistas en el StackPane
    private void cargarVista(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/" + fxmlFile));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            contenedorPrincipal.getChildren().clear();
            contenedorPrincipal.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista: " + fxmlFile);
        }
    }

    // Mostrar una alerta en caso de error
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Cerrar sesión
    @FXML
    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio de Sesión");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Salir de la aplicación
    @FXML
    private void salir() {
        System.exit(0);
    }

    // Método para mostrar diferentes vistas según el menú seleccionado
    @FXML
    private void panel() {
        cargarVista("panel.fxml");
    }
    @FXML
    private void mesas() {
        cargarVista("mesas.fxml");
    }
    @FXML
    private void platos() {
        cargarVista("platos.fxml");
    }
    @FXML
    private void pedidos() {
        cargarVista("pedidos.fxml");
    }
    @FXML
    private void datos() {
        cargarVista("datos.fxml");
    }
    @FXML
    private void usuario() {
        cargarVista("usuarios.fxml");
    }
    @FXML
    private void venta() {
        cargarVista("venta.fxml");
    }

    @FXML
    private void notas() {
        cargarVista("platos.fxml");
    }

    // Mostrar información de "Acerca de"
    @FXML
    private void mostrarAcercaDe() {
        mostrarAlerta("Acerca de", "Sistema de Gestión de Usuarios y Notas v1.0");
    }
}