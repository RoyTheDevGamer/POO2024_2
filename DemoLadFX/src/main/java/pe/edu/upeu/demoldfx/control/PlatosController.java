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
import pe.edu.upeu.demoldfx.modelo.Platos;
import pe.edu.upeu.demoldfx.servicio.HorarioService;
import pe.edu.upeu.demoldfx.servicio.PlatosService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component

public class PlatosController {
    @FXML
    TextField txtNombrePlatos, txtPrecio,txtFiltroDato;
    @FXML
    ComboBox<ComboBoxOption> cbxHorario;
    @FXML
    private TableView<Platos> tableView;
    @FXML
    Label lbnMsg;
    @Autowired
    HorarioService hs;
    @Autowired
    PlatosService ps;
    @FXML
    private AnchorPane miContenedor;
    Stage stage;

    private Validator validator;
    ObservableList<Platos> listarPlatos;
    Platos formulario;
    Long idPlatosCE=0L;

    public void initialize(){
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

        cbxHorario.setTooltip(new Tooltip());
        cbxHorario.getItems().addAll(hs.listarComboBox());
        cbxHorario.setOnAction(Event ->{
            ComboBoxOption selectedProduct = cbxHorario.getSelectionModel().getSelectedItem();
            if (selectedProduct != null){
                String selectedld = selectedProduct.getKey();
                System.out.println("ID del producto selecionado: " + selectedld);
            }
        });
        new ComboBoxAutoComplete<>(cbxHorario);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Platos> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID",new ColumnInfo("idPlatos",20.0));
        columns.put("Nombre Del Plato",new ColumnInfo("nombre_platos",150.0));
        columns.put("Descripcion",new ColumnInfo("Horario.nombre_horario",100.0));
        columns.put("Precio",new ColumnInfo("precio_platos",80.0));
        Consumer<Platos> updateAction = (Platos platos) ->{
            System.out.println("Actualizar: " + platos);
            editForm(platos);
        };

        Consumer<Platos> deleteAction = (Platos platos) ->{
            ps.delete(platos.getIdPlatos());
            double with=stage.getWidth()/1.5;
            double h= stage.getHeight()/2;
            Toast.showToast(stage,"Se elimino correctamente!!",2000, with, h);
            listar();
        };


        tableViewHelper.addColumnsInOrderWithSize(tableView,columns,updateAction,deleteAction);

        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar(){
        try {
            tableView.getItems().clear();
            listarPlatos = FXCollections.observableArrayList(ps.list());
            tableView.getItems().addAll(listarPlatos);
            txtFiltroDato.textProperty().addListener((observable, oldVAlue, newValue) ->{
                filtrarPlatos(newValue);
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public void limpiarError(){
        txtNombrePlatos.getStyleClass().remove("text-field-error");
        txtPrecio.getStyleClass().remove("text-field-error");
        cbxHorario.getStyleClass().remove("text-field-error");
    }

    public void clearForm(){
        txtNombrePlatos.setText("");
        txtPrecio.setText("");
        cbxHorario.getSelectionModel().select(null);
        idPlatosCE=0L;
        limpiarError();
    }
    @FXML
    public void cancelarAccion(){
        clearForm();
        limpiarError();
    }

    void validarCampos(List<ConstraintViolation<Platos>> violacionesOrdenadasPorPropiedad){
        // Crear un LinkedHashMap para ordenar las violaciones
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        // Mostrar el primer mensaje de error
        for (ConstraintViolation<Platos> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();
            if(campo.equals("nombre_platos")){
                erroresOrdenados.put("nombre_platos", violacion.getMessage());
                txtNombrePlatos.getStyleClass().add("text-field-error");
            }else if (campo.equals("precio_platos")) {
                erroresOrdenados.put("precio_platos", violacion.getMessage());
                txtPrecio.getStyleClass().add("text-field-error");
            }else if (campo.equals("horario")) {
                erroresOrdenados.put("horario", violacion.getMessage());
                cbxHorario.getStyleClass().add("text-field-error");
            }
        }
        // Mostrar el primer error en el orden deseado
        Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
        lbnMsg.setText(primerError.getValue()); // Mostrar el mensaje del primer error
        lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
    }


    @FXML
    public void validarFormulario() {
        formulario = new Platos();
        formulario.setNombre_platos(txtNombrePlatos.getText());
        formulario.setPrecio_platos(Double.parseDouble(txtPrecio.getText()==""?"0":txtPrecio.getText()));
        String idxH=cbxHorario.getSelectionModel().getSelectedItem()==null?"0":cbxHorario.getSelectionModel().getSelectedItem().getKey();
        formulario.setHorario(hs.searchById(Long.parseLong(idxH)));
        Set<ConstraintViolation<Platos>> violaciones = validator.validate(formulario);
        // Si prefieres ordenarlo por el nombre de la propiedad que violó la restricción, podrías usar:
        List<ConstraintViolation<Platos>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());
        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            // Los datos son válidos
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();
            double with=stage.getWidth()/1.5;
            double h=stage.getHeight()/2;
            if(idPlatosCE!=0L && idPlatosCE>0L){
                formulario.setIdPlatos(idPlatosCE);
                ps.update(formulario);
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, with, h);
                clearForm();
            }else{
                ps.save(formulario);
                Toast.showToast(stage, "Se guardo correctamente!!", 2000, with, h);
                clearForm();
            }
            listar();
        } else {
            validarCampos(violacionesOrdenadasPorPropiedad);
        }
    }

    private void filtrarPlatos(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            // Si el filtro está vacío, volver a mostrar la lista completa
            tableView.getItems().clear();
            tableView.getItems().addAll(listarPlatos);
        } else {
            // Aplicar el filtro
            String lowerCaseFilter = filtro.toLowerCase();
            List<Platos> platosFiltrados = listarPlatos.stream()
                    .filter(platos -> {
                        // Verificar si el filtro coincide con alguno de los campos
                        if (platos.getNombre_platos().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (String.valueOf(platos.getPrecio_platos()).contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (platos.getHorario().getNombre_horario().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false; // Si no coincide con ningún campo
                    })
                    .collect(Collectors.toList());
            // Actualizar los items del TableView con los productos filtrados
            tableView.getItems().clear();
            tableView.getItems().addAll(platosFiltrados);
        }
    }

    public void editForm(Platos platos){
        txtNombrePlatos.setText(platos.getNombre_platos());
        txtPrecio.setText(platos.getPrecio_platos().toString());
        // Seleccionar el ítem en cbxMarca según el ID de Marca
        cbxHorario.getSelectionModel().select(
                cbxHorario.getItems().stream()
                        .filter(marca -> Long.parseLong(marca.getKey())==platos.getHorario().getIdHorario())
                        .findFirst()
                        .orElse(null)
        );
        idPlatosCE=platos.getIdPlatos();
        limpiarError();
    }
}
