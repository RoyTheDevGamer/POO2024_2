package pe.edu.upeu.sysalmacenfx.control;

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
import pe.edu.upeu.sysalmacenfx.componente.ColumnInfo;
import pe.edu.upeu.sysalmacenfx.componente.ComboBoxAutoComplete;
import pe.edu.upeu.sysalmacenfx.componente.TableViewHelper;
import pe.edu.upeu.sysalmacenfx.componente.Toast;
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.Producto;
import pe.edu.upeu.sysalmacenfx.servicio.CategoriaService;
import pe.edu.upeu.sysalmacenfx.servicio.MarcaService;
import pe.edu.upeu.sysalmacenfx.servicio.ProductoService;
import pe.edu.upeu.sysalmacenfx.servicio.UnidadMedidaService;

import javax.xml.validation.Validator;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

@Component
public class ProductoController {
    @FXML
    TextField txtNombreProducto, txtPUnit, txtPUnitOld, txtUtilidad, txtStock, txtStockOld,txtFiltroDato;
    @FXML
    ComboBox<ComboBoxOption> cbxMarca;
    @FXML
    ComboBox<ComboBoxOption> cbxCategoria;
    @FXML
    ComboBox<ComboBoxOption> cbxUnidadMedida;
    @FXML
    private TableView<Producto> tableView;
    @FXML
    Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;
    Stage stage;

    @Autowired
    MarcaService ms;
    @Autowired
    CategoriaService cs;
    @Autowired
    ProductoService ps;
    @Autowired
    UnidadMedidaService ums;

    private Validator validator;
    ObservableList<Producto> listarProducto;
    Producto formulario;
    Long idProductoCE=0L;

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

        cbxMarca.setTooltip(new Tooltip());
        cbxMarca.getItems().addAll(ms.listarComboBox());
        cbxMarca.setOnAction(Event ->{
            ComboBoxOption selectedProduct = cbxMarca.getSelectionModel().getSelectedItem();
            if (selectedProduct != null){
                String selectedld = selectedProduct.getKey();
                System.out.println("ID del producto selecionado: " + selectedld);
            }
        });
        new ComboBoxAutoComplete<>(cbxMarca);

        cbxCategoria.setTooltip(new Tooltip());
        cbxCategoria.getItems().addAll(cs.listarComboBox());
        cbxCategoria.setOnAction(Event ->{
            ComboBoxOption selectedProduct = cbxCategoria.getSelectionModel().getSelectedItem();
            if (selectedProduct != null){
                String selectedld = selectedProduct.getKey();
                System.out.println("ID del producto selecionado: " + selectedld);
            }
        });
        new ComboBoxAutoComplete<>(cbxCategoria);

        cbxUnidadMedida.setTooltip(new Tooltip());
        cbxUnidadMedida.getItems().addAll(ums.listarComboBox());
        cbxUnidadMedida.setOnAction(Event ->{
            ComboBoxOption selectedProduct = cbxUnidadMedida.getSelectionModel().getSelectedItem();
            if (selectedProduct != null){
                String selectedld = selectedProduct.getKey();
                System.out.println("ID del producto selecionado: " + selectedld);
            }
        });
        new ComboBoxAutoComplete<>(cbxUnidadMedida);

        TableViewHelper<Producto> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Pro",new ColumnInfo("idProducto",60.0));
        columns.put("Nombre Producto",new ColumnInfo("nombre",200.0));
        columns.put("P. Unitario",new ColumnInfo("pu",150.0));
        columns.put("Utilidad",new ColumnInfo("utilidad",100.0));
        columns.put("Marca",new ColumnInfo("marca.nombre",200.0));
        columns.put("Categoria",new ColumnInfo("categoria.nombre",200.0));
        columns.put("Unid. Medida",new ColumnInfo("unidadMedida.nombreMedida",200.0));

        Consumer<Producto> updateAction = (Producto producto) ->{
            System.out.println("Actualizar: " + producto);
            //editForm(producto);
        };

        Consumer<Producto> deleteAction = (Producto producto) ->{
          ps.delete(producto.getIdProducto());
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
            listarProducto = FXCollections.observableArrayList(ps.list());
            tableView.getItems().addAll(listarProducto);
            txtFiltroDato.textProperty().addListener((observable, oldVAlue, newValue) ->{
                //filtrarProductos(newValue);
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



}
