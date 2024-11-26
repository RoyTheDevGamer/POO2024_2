package pe.edu.upeu.demoldfx.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.demoldfx.componente.*;
import pe.edu.upeu.demoldfx.dto.ModeloDataAutocomplet;
import pe.edu.upeu.demoldfx.modelo.Platos;
import pe.edu.upeu.demoldfx.modelo.VentCarrito;
import pe.edu.upeu.demoldfx.servicio.PedidosService;
import pe.edu.upeu.demoldfx.servicio.PlatosService;
import pe.edu.upeu.demoldfx.servicio.UsuarioService;
import pe.edu.upeu.demoldfx.servicio.VentCarritoService;

import java.util.*;
import java.util.function.Consumer;

@Component
public class VentaController {



    @Autowired
    VentCarritoService ventCarritoService;
    @Autowired
    PlatosService platosService;

    @Autowired
    PedidosService pedidosService;
    @Autowired
    UsuarioService usuarioService;
    @FXML
    TextField autocompCliente,dni,nombreUser;
    @FXML
    Button btnRegVenta, btnRegCarrito, btnFormCliente;

    @FXML
    TextField autocompProducto,nombreProducto,codigoPro,cantidadPro,punitPro,preTPro, txtBaseImp, txtIgv, txtImporteT;

    AutoCompleteTextField actfP;
    AutoCompleteTextField actfU;
    private final SortedSet<ModeloDataAutocomplet> entriesP = new TreeSet<>((ModeloDataAutocomplet o1, ModeloDataAutocomplet o2) -> o1.toString().compareTo(o2.toString()));
    private final SortedSet<ModeloDataAutocomplet> entriesU = new TreeSet<>((ModeloDataAutocomplet o1, ModeloDataAutocomplet o2) -> o1.toString().compareTo(o2.toString()));


    Stage stage;
    @FXML
    TableView<VentCarrito> tableView;
    @FXML
    private AnchorPane miContenedor;

    ModeloDataAutocomplet lastPedidos;
    ModeloDataAutocomplet lastUsuario;

    @FXML
    public void initialize(){
        Platform.runLater(() -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            System.out.println("El título del stage es: " + stage.getTitle());
        });

        listarPedidos();
        actfP=new AutoCompleteTextField<>(entriesP, autocompProducto);

        autocompProducto.setOnKeyReleased(e->{
            lastPedidos=(ModeloDataAutocomplet) actfP.getLastSelectedObject();
            if(lastPedidos!=null){
                System.out.println(lastPedidos.getNameDysplay());
                nombreProducto.setText(lastPedidos.getNameDysplay());
                codigoPro.setText(lastPedidos.getIdx());
                String[] dato=lastPedidos.getOtherData().split(":");
                nombreProducto.setText(dato[0]);
                punitPro.setText(dato[1]);

            }
        });


        listarUsuarios();

        actfU=new AutoCompleteTextField<>(entriesU, autocompCliente);

        autocompCliente.setOnKeyReleased(e->{
            lastUsuario=(ModeloDataAutocomplet) actfU.getLastSelectedObject();
            if(lastUsuario!=null){
                System.out.println(lastUsuario.getNameDysplay());
                String[] dato=lastUsuario.getOtherData().split(":");
                nombreUser.setText(dato[0]);
                dni.setText(dato[1]);
            }
        });

        btnRegCarrito.setDisable(true);
        personalizarTabla();
    }
    public void listarPedidos(){
        entriesP.addAll(pedidosService.listAutoCompletProducto());
    }
    public void listarUsuarios(){
        entriesU.addAll(usuarioService.listAutoCompletProducto());
    }

    public void personalizarTabla(){
        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<VentCarrito> tableViewHelper = new TableViewHelper<>();
        // Definir las columnas dinámicamente en un mapa (nombre visible -> campo del modelo)
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Prod", new ColumnInfo("idCarrito", 100.0)); // Columna visible "Columna 1" mapea al campo "campo1"
        columns.put("Nombre Producto", new ColumnInfo("nombrePlatos", 300.0)); // Columna visible "Columna 1" mapea al campo "campo1"
        columns.put("Cantidad", new ColumnInfo("cantidad", 60.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("P.Unitario", new ColumnInfo("punitario", 100.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("P.Total", new ColumnInfo("ptotal", 100.0)); // Columna visible "Columna 2" mapea al campo "campo2"

        Consumer<VentCarrito> updateAction = (VentCarrito ventCarrito) -> { System.out.println("Actualizar: " + ventCarrito); };
        Consumer<VentCarrito> deleteAction = (VentCarrito ventCarrito) -> {deleteReg(ventCarrito); };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns,updateAction, deleteAction );
        // Agregar botones de eliminar y modificar
        tableView.setTableMenuButtonVisible(true);
    }
    @FXML
    private void calcularPT(){
        if(!cantidadPro.getText().equals("")){
            double dato=Double.parseDouble(punitPro.getText())*Double.parseDouble(cantidadPro.getText());
            preTPro.setText(String.valueOf(dato));
            if(Double.parseDouble(cantidadPro.getText())>0.0){
                btnRegCarrito.setDisable(false);
            }else{
                btnRegCarrito.setDisable(true);
            }
        }else{
            btnRegCarrito.setDisable(true);
        }
    }



    public void listar(){
        tableView.getItems().clear();
        List<VentCarrito> lista=ventCarritoService.list();
        double impoTotal = 0, igv = 0;
        for (VentCarrito dato: lista){
            impoTotal += Double.parseDouble(String.valueOf(dato.getPtotal()));
        }
        txtImporteT.setText(String.valueOf(impoTotal));
        double pv = impoTotal / 1.18;
        txtBaseImp.setText(String.valueOf(Math.round(pv * 100.0) / 100.0));
        txtIgv.setText(String.valueOf(Math.round((pv * 0.18) * 100.0) / 100.0));
        tableView.getItems().addAll(lista);
    }
//    public void editVenCarrito(VentCarrito obj) {
//        System.out.println(obj.getDniruc());
//    }
    public void deleteReg(VentCarrito obj) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción");
        alert.setContentText("¿Estás seguro de que deseas eliminar este elemento?");
        // Mostrar el diálogo y esperar la respuesta del usuario
        Optional<ButtonType> result = alert.showAndWait();
        // Verificar si el usuario hizo clic en "Aceptar"
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ventCarritoService.delete(obj.getIdCarrito());
            Stage stage = StageManager.getPrimaryStage();
            double with=stage.getMaxWidth()/2;
            Toast.showToast(stage, "¡Acción completada!", 2000, with, 50);
            listar();
        } else {
            // Si el usuario cancela, no se hace nada
            System.out.println("Acción cancelada");
        }
    }

    @FXML
    private void registarPCarrito(){
        try {
            VentCarrito ss= VentCarrito.builder()
                    .platos(platosService.searchById(Long.parseLong(codigoPro.getText())))
                    .nombrePlatos(nombreProducto.getText())
                    .cantidad(Double.parseDouble(cantidadPro.getText()))
                    .punitario(Double.parseDouble(punitPro.getText()))
                    .ptotal(Double.parseDouble(preTPro.getText()))
                    .build();
            ventCarritoService.save(ss);
            listar();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void registrarVenta() {
        try {
            // Lógica de registro de la venta (si aplica)
            System.out.println("Venta registrada");

            // Llama al servicio para eliminar todos los registros de la tabla VentCarrito
            ventCarritoService.deleteAll();
            pedidosService.deleteAllP();
            listar(); // Refresca la tabla para reflejar los cambios

            // Crear una alerta de información
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro de Venta");
            alert.setHeaderText(null); // Puedes omitir el encabezado
            alert.setContentText("Tu venta fue exitosa y el carrito ha sido limpiado.");

            // Mostrar la alerta
            alert.showAndWait();

        } catch (Exception e) {
            System.out.println("Error al registrar la venta: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ocurrió un error al registrar la venta.");
            alert.showAndWait();
        }
    }



}
