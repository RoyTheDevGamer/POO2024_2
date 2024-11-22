package pe.edu.upeu.demoldfx.control;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.demoldfx.componente.ColumnInfo;
import pe.edu.upeu.demoldfx.componente.TableViewHelper;
import pe.edu.upeu.demoldfx.componente.Toast;
import pe.edu.upeu.demoldfx.dto.ComboBoxOption;
import pe.edu.upeu.demoldfx.componente.ComboBoxAutoComplete;
import pe.edu.upeu.demoldfx.modelo.Usuario;
import pe.edu.upeu.demoldfx.servicio.PerfilService;
import pe.edu.upeu.demoldfx.servicio.UsuarioService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
@Component
public class UsuariosController {

    @FXML
    private TextField txtUsuario, txtApellido, txtTelefono, txtGmail, txtClave, txtFiltroDato;
    @FXML
    private ComboBox<ComboBoxOption> cbxCargo; // ComboBox para el perfil
    @FXML
    private TableView<Usuario> tableView;
    @FXML
    private Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PerfilService perfilService;
    private Validator validator;

    ObservableList<Usuario> listarUsuarios;
    Usuario formulario;
    Long idUsuarioCE = 0L;
    Stage stage;

    public void initialize() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event ->{
            stage = (Stage) miContenedor.getScene().getWindow();
            if(stage != null){
                System.out.println("El titulo de stage es: "+ stage.getTitle());
            }else {
                System.out.println("Stage aun no disponible");
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
        // Cargar los perfiles en el ComboBox
        cbxCargo.setTooltip(new Tooltip());
        cbxCargo.getItems().addAll(perfilService.listarComboBox());
        cbxCargo.setOnAction(Event ->{
            ComboBoxOption selectedProduct = cbxCargo.getSelectionModel().getSelectedItem();
            if (selectedProduct != null){
                String selectedld = selectedProduct.getKey();
                System.out.println("ID del plato selecionado: " + selectedld);
            }
        });

        new ComboBoxAutoComplete<>(cbxCargo);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();


        // Configurar TableView
        TableViewHelper<Usuario> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idUsuario", 20.0));
        columns.put("Usuario", new ColumnInfo("user", 100.0));
        columns.put("Apellido", new ColumnInfo("apellido", 150.0));
        columns.put("Clave", new ColumnInfo("clave", 20.0));
        columns.put("Telefono", new ColumnInfo("telefono", 100.0));
        columns.put("Email", new ColumnInfo("email", 150.0));
        columns.put("Cargo", new ColumnInfo("perfil.nombre", 100.0));

        // Acción de actualizar
        Consumer<Usuario> updateAction = (Usuario usuario) -> {
            System.out.println("Actualizar: " + usuario);
            editForm(usuario);  // Llenar el formulario con los datos del usuario
        };

        // Acción de eliminar
        Consumer<Usuario> deleteAction = (Usuario usuario) -> {
            usuarioService.delete(usuario.getIdUsuario());  // Eliminar usuario de la base de datos
            double width = stage.getWidth() / 1.5;
            double height = stage.getHeight() / 2;
            Toast.showToast(stage, "Usuario eliminado correctamente!!", 2000, width, height);
            listar();  // Actualizar la lista después de eliminar
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();// Cargar los usuarios en la tabla al inicializar

    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarUsuarios = FXCollections.observableArrayList(usuarioService.list());
            tableView.getItems().addAll(listarUsuarios);
            txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> {
                filtrarUsuarios(newValue);  // Filtrar usuarios mientras escribes
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void filtrarUsuarios(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            // Si el filtro está vacío, volver a mostrar la lista completa
            tableView.getItems().clear();
            tableView.getItems().addAll(listarUsuarios);
        } else {
            // Aplicar el filtro
            String lowerCaseFilter = filtro.toLowerCase();
            List<Usuario> usuariosFiltrados = listarUsuarios.stream()
                    .filter(usuario -> {
                        return usuario.getUser().toLowerCase().contains(lowerCaseFilter) ||
                                usuario.getApellido().toLowerCase().contains(lowerCaseFilter) ||
                                usuario.getTelefono().toLowerCase().contains(lowerCaseFilter) ||
                                usuario.getEmail().toLowerCase().contains(lowerCaseFilter) ||
                                usuario.getPerfil().getNombre().toLowerCase().contains(lowerCaseFilter);
                    })
                    .collect(Collectors.toList());
            // Actualizar los items del TableView con los usuarios filtrados
            tableView.getItems().clear();
            tableView.getItems().addAll(usuariosFiltrados);
        }
    }

    public void limpiarFormulario() {
        txtUsuario.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtGmail.clear();
        txtClave.clear();
        cbxCargo.getSelectionModel().clearSelection();
        idUsuarioCE = 0L;
    }
    public void limpiarError(){
        txtUsuario.getStyleClass().remove("text-field-error");
        txtApellido.getStyleClass().remove("text-field-error");
        txtClave.getStyleClass().remove("text-field-error");
        txtTelefono.getStyleClass().remove("text-field-error");
        txtGmail.getStyleClass().remove("text-field-error");
        cbxCargo.getStyleClass().remove("text-field-error");
    }

    void validarCampos(List<ConstraintViolation<Usuario>> violacionesOrdenadasPorPropiedad){
        // Crear un LinkedHashMap para ordenar las violaciones
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        // Mostrar el primer mensaje de error
        for (ConstraintViolation<Usuario> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();
            if(campo.equals("user")){
                erroresOrdenados.put("user", violacion.getMessage());
                txtUsuario.getStyleClass().add("text-field-error");
            }else if (campo.equals("apellido")) {
                erroresOrdenados.put("apellido", violacion.getMessage());
                txtApellido.getStyleClass().add("text-field-error");
            }
            else if (campo.equals("clave")) {
                erroresOrdenados.put("clave", violacion.getMessage());
                txtClave.getStyleClass().add("text-field-error");
            }
            else if (campo.equals("email")) {
                erroresOrdenados.put("email", violacion.getMessage());
                txtGmail.getStyleClass().add("text-field-error");
            }else if (campo.equals("perfil")) {
                erroresOrdenados.put("perfil", violacion.getMessage());
                cbxCargo.getStyleClass().add("text-field-error");
            }else if (campo.equals("telefono")) {
                erroresOrdenados.put("telefono", violacion.getMessage());
                txtTelefono.getStyleClass().add("text-field-error");
            }
        }
        // Mostrar el primer error en el orden deseado
        Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
        lbnMsg.setText(primerError.getValue()); // Mostrar el mensaje del primer error
        lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
    }

    @FXML
    public void cancelarAccion() {
        limpiarFormulario();
        clearForm();
    }

    @FXML
    public void validarFormulario() {
        formulario = new Usuario();
        formulario.setUser(txtUsuario.getText());
        formulario.setApellido(txtApellido.getText());
        formulario.setTelefono(txtTelefono.getText());
        formulario.setEmail(txtGmail.getText());
        formulario.setClave(txtClave.getText());
        String idxH=cbxCargo.getSelectionModel().getSelectedItem()==null?"0":cbxCargo.getSelectionModel().getSelectedItem().getKey();
        formulario.setPerfil(perfilService.searchById(Long.parseLong(idxH)));

        Set<ConstraintViolation<Usuario>> violaciones = validator.validate(formulario);
        // Si prefieres ordenarlo por el nombre de la propiedad que violó la restricción, podrías usar:
        List<ConstraintViolation<Usuario>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());
        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            // Los datos son válidos
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();
            double with=stage.getWidth()/1.5;
            double h=stage.getHeight()/2;
            if(idUsuarioCE!=0L && idUsuarioCE>0L){
                formulario.setIdUsuario(idUsuarioCE);
                usuarioService.update(formulario);
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, with, h);
                clearForm();
            }else{
                usuarioService.save(formulario);
                Toast.showToast(stage, "Se guardo correctamente!!", 2000, with, h);
                clearForm();
            }
            listar();
        }
        else {
            validarCampos(violacionesOrdenadasPorPropiedad);
        }
         // Volver a cargar la lista de usuarios
    }



    public void clearForm(){
        txtUsuario.setText("");
        txtGmail.setText("");
        txtTelefono.setText("");
        txtClave.setText("");
        txtApellido.setText("");
        cbxCargo.getSelectionModel().select(null);
        idUsuarioCE=0L;
        limpiarError();
    }

    public void editForm(Usuario usuario) {
        // Llenar el formulario con los datos del usuario seleccionado para editar
        txtUsuario.setText(usuario.getUser());
        txtApellido.setText(usuario.getApellido());
        txtTelefono.setText(usuario.getTelefono());
        txtGmail.setText(usuario.getEmail());
        txtClave.setText(usuario.getClave());
        cbxCargo.getSelectionModel().select(
                cbxCargo.getItems().stream()
                        .filter(marca -> Long.parseLong(marca.getKey())==usuario.getPerfil().getIdPerfil())
                        .findFirst()
                        .orElse(null)
        );
        //cbxCargo.getSelectionModel().select(usuario.getPerfil());  // Seleccionar el perfil del usuario
        idUsuarioCE = usuario.getIdUsuario();
    }
}