package pe.edu.upeu.demoldfx.control;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.demoldfx.componente.ColumnInfo;
import pe.edu.upeu.demoldfx.componente.ComboBoxAutoComplete;
import pe.edu.upeu.demoldfx.componente.TableViewHelper;
import pe.edu.upeu.demoldfx.componente.Toast;
import pe.edu.upeu.demoldfx.dto.ComboBoxOption;
import pe.edu.upeu.demoldfx.modelo.Pedidos;
import pe.edu.upeu.demoldfx.modelo.Platos;
import pe.edu.upeu.demoldfx.servicio.PedidosService;
import pe.edu.upeu.demoldfx.servicio.PlatosService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class PedidosController {

    @FXML
    private TableView<Platos> Tabla1; // Tabla1
    @FXML
    private TableView<Platos> Tabla2; // Tabla2 (La nueva tabla)
    @FXML
    private Button Pedir; // El botón que guardará los pedidos

    @Autowired
    private PedidosService pedidosService;

    @FXML
    private AnchorPane miContenedor;
    @FXML
    private TextField txtFiltroDato; // Campo para el filtro
    @Autowired
    private PlatosService platosService; // Servicio que obtiene los platos de la base de datos
    private ObservableList<Platos> listarPlatos;
    private ObservableList<Platos> platosSeleccionados; // Lista para los platos añadidos a Tabla2
    Stage stage;

    public void initialize() {
        // Código anterior...
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

        // Inicializar la lista de platos para Tabla2
        platosSeleccionados = FXCollections.observableArrayList();

        // Configuración de la Tabla1
        TableViewHelper<Platos> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idPlatos", 20.0));
        columns.put("Nombre Del Plato", new ColumnInfo("nombre", 150.0));

        columns.put("Precio", new ColumnInfo("precio", 80.0));
        columns.put("Categoria", new ColumnInfo("Categoria.nombre", 100.0));

        tableViewHelper.addColumnsInOrderWithSize(Tabla1, columns, null, null);
        Tabla1.setTableMenuButtonVisible(true);

        // Configuración de la Tabla2
        LinkedHashMap<String, ColumnInfo> columns2 = new LinkedHashMap<>();
        columns2.put("ID", new ColumnInfo("idPlatos", 20.0));
        columns2.put("Nombre Del Plato", new ColumnInfo("nombre", 150.0));

        columns2.put("Precio", new ColumnInfo("precio", 80.0));
        columns2.put("Categoria", new ColumnInfo("Categoria.nombre", 100.0));


        tableViewHelper.addColumnsInOrderWithSize(Tabla2, columns2, null, null);
        Tabla2.setTableMenuButtonVisible(true);

        // Llamada al método listar para cargar los platos de la base de datos en Tabla1
        listar();
    }

    public void listar() {
        try {
            Tabla1.getItems().clear();
            listarPlatos = FXCollections.observableArrayList(platosService.list());
            Tabla1.getItems().addAll(listarPlatos);

            // Filtro en la Tabla1
            txtFiltroDato.textProperty().addListener((observable, oldVAlue, newValue) -> {
                filtrarPlatos(newValue);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void filtrarPlatos(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            // Si el filtro está vacío, volver a mostrar la lista completa
            Tabla1.getItems().clear();
            Tabla1.getItems().addAll(listarPlatos);
        } else {
            // Aplicar el filtro
            String lowerCaseFilter = filtro.toLowerCase();
            List<Platos> platosFiltrados = listarPlatos.stream()
                    .filter(platos -> platos.getNombre().toLowerCase().contains(lowerCaseFilter))
                    .collect(Collectors.toList());

            // Actualizar los items del TableView con los platos filtrados
            Tabla1.getItems().clear();
            Tabla1.getItems().addAll(platosFiltrados);
        }
    }

    @FXML
    public void anadirPlato() {
        // Obtener el plato seleccionado de Tabla1
        Platos platoSeleccionado = Tabla1.getSelectionModel().getSelectedItem();

        if (platoSeleccionado != null) {
            // Añadir el plato a Tabla2 (platosSeleccionados)
            platosSeleccionados.add(platoSeleccionado);

            // Actualizar Tabla2 con la nueva lista de platos seleccionados
            Tabla2.getItems().clear();
            Tabla2.getItems().addAll(platosSeleccionados);
        } else {
            // Si no se ha seleccionado ningún plato, mostrar un mensaje
            System.out.println("Por favor, selecciona un plato para añadirlo.");
        }
    }


    @FXML
    public void realizarPedido() {
        if (!platosSeleccionados.isEmpty()) {
            try {
                for (Platos plato : platosSeleccionados) {
                    // Crear un objeto de tipo Pedidos
                    Pedidos pedido = new Pedidos();
                    pedido.setNombre(plato.getNombre());

                    pedido.setPrecio(plato.getPrecio());
                    pedido.setCategoria(plato.getCategoria());  // Asegúrate de que la categoría esté bien definida

                    // Guardar el pedido en la base de datos
                    Pedidos pedidoGuardado = pedidosService.save(pedido);
                    if (pedidoGuardado != null) {
                        System.out.println("Pedido guardado con éxito: " + pedidoGuardado.getNombre());
                    } else {
                        System.out.println("Error al guardar el pedido: " + plato.getNombre());
                    }
                }

                // Limpiar la lista de platos seleccionados y Tabla2
                platosSeleccionados.clear();
                Tabla2.getItems().clear();
                System.out.println("Pedidos guardados con éxito.");

            } catch (Exception e) {
                System.out.println("Error al pedir platos: " + e.getMessage());
            }
        } else {
            System.out.println("Por favor, selecciona platos para pedir.");
        }
    }

}