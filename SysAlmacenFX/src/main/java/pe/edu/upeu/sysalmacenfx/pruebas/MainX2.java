package pe.edu.upeu.sysalmacenfx.pruebas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysalmacenfx.modelo.Categoria;
import pe.edu.upeu.sysalmacenfx.servicio.CategoriaService;

import java.util.List;
import java.util.Scanner;
@Component
public class MainX2 {
    Scanner joji = new Scanner(System.in);
    @Autowired
    CategoriaService service;
    public void menu() {
        int opc = 0;
        do {
            System.out.println("------MAIN CATEGORIA X2-----");
            System.out.println("1.-Ver lista-------------");
            System.out.println("2.-Anadir Categoria------");
            System.out.println("3.-Eliminar Categoria----");
            System.out.println("4.-Editar Categoria------");
            System.out.println("5.--------Salir----------");
            opc = joji.nextInt();
            joji.nextLine();   //Roy Cruz Mamani

            switch (opc) {
                case 1:
                    listar();
                    break;
                case 2:
                    anadir();
                    break;
                case 3:
                    eliminar();
                    break;
                case 4:
                    editar();
                    break;
                case 5:
                    System.out.println("Saliendo...:)");
                    break;
                default:
                    System.out.println("Error , intenta de nuevo.");
                    break;
            }
        } while (opc != 6);
    }

    public void registro(){
        System.out.println("MAIN CATEGORIA");
        Categoria ca=new Categoria();
        ca.setNombre("Verduras");
        Categoria dd=service.save(ca);
        System.out.println("Reporte: ");
        System.out.println(dd.getIdCategoria()+ " "+ dd.getNombre());
        anadir();
    }


    public void listar (){
        List<Categoria> datos=service.list();
        System.out.println("ID | Nombre");
        for (Categoria ca: datos ){
            System.out.println(ca.getIdCategoria()+ "  | " +ca.getNombre());
        }
    }

    public void anadir(){
        System.out.print("Introduce el nombre de la categoria para añadir:");
        String nombre = joji.nextLine();
        Categoria ca=new Categoria();
        ca.setNombre(nombre);
        Categoria dd=service.save(ca);
        System.out.println("Reporte: " + dd.getIdCategoria() + " " + dd.getNombre());
    }
    public void eliminar() {
        System.out.print("Introduce el ID de la categoria para eliminar:");
        Long id = joji.nextLong();
        joji.nextLine();
        service.delete(id);
        System.out.println("La Categoria de ID " + id + " se elimino para siempre.");

    }
    public void editar() {
        System.out.print("Introduce el ID de la categoria para editar:");
        Long id = joji.nextLong();
        joji.nextLine();
        Categoria ca = service.searchById(id);
        if (ca != null) {
            System.out.println("Nombre actual: " + ca.getNombre());
            System.out.print("Introduce el nuevo nombre de la categoria:");
            String nuevoNombre = joji.nextLine();
            ca.setNombre(nuevoNombre);
            service.update(ca);
            System.out.println("Categoría con nuevo nombre: " + ca.getIdCategoria() + " " + ca.getNombre());
        }
    }
}
