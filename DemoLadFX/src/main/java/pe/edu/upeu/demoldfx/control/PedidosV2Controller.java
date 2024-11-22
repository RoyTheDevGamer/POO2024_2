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
import pe.edu.upeu.demoldfx.servicio.CategoriaService;
import pe.edu.upeu.demoldfx.servicio.PlatosService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Component
public class PedidosV2Controller {

    @FXML
    private TableView<Platos> Tabla2; // Tabla1
    @FXML
    private AnchorPane miContenedor;

    @FXML
    private TextField txtFiltroDato; // Campo para el filtro
    @Autowired
    private PlatosService platosService; // Servicio que obtiene los platos de la base de datos
    private ObservableList<Platos> listarPlatos;
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

        TableViewHelper<Platos> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID",new ColumnInfo("idPlatos",20.0));
        columns.put("Nombre Del Plato",new ColumnInfo("nombre",150.0));
        columns.put("Descripcion",new ColumnInfo("descripcion",100.0));
        columns.put("Precio",new ColumnInfo("precio",80.0));
        columns.put("Categoria",new ColumnInfo("Categoria.nombre",100.0));



        tableViewHelper.addColumnsInOrderWithSize(Tabla2,columns,null,null);

        Tabla2.setTableMenuButtonVisible(true);
        listar();
    }
    public void listar(){
        try {
            Tabla2.getItems().clear();
            listarPlatos = FXCollections.observableArrayList(platosService.list());
            Tabla2.getItems().addAll(listarPlatos);
            txtFiltroDato.textProperty().addListener((observable, oldVAlue, newValue) ->{
                filtrarPlatos(newValue);
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void filtrarPlatos(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            // Si el filtro está vacío, volver a mostrar la lista completa
            Tabla2.getItems().clear();
            Tabla2.getItems().addAll(listarPlatos);
        } else {
            // Aplicar el filtro
            String lowerCaseFilter = filtro.toLowerCase();
            List<Platos> platosFiltrados = listarPlatos.stream()
                    .filter(platos -> {
                        // Verificar si el filtro coincide con alguno de los campos
                        if (platos.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (String.valueOf(platos.getPrecio()).contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (platos.getCategoria().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false; // Si no coincide con ningún campo
                    })
                    .collect(Collectors.toList());
            // Actualizar los items del TableView con los productos filtrados
            Tabla2.getItems().clear();
            Tabla2.getItems().addAll(platosFiltrados);
        }
    }
}