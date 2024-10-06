package pe.edu.upeu.sysalmacenfx.repositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysalmacenfx.modelo.Categoria;
import java.util.List;
import java.util.Scanner;

@Component
public class MainX {
    Scanner joji = new Scanner(System.in);
    @Autowired
    CategoriaRepository repository;
    public void menu() {
        int opc = 0;
        do {
            System.out.println("------MAIN CATEGORIA-----");
            System.out.println("1.-Ver lista-------------");
            System.out.println("2.-Anadir Categoria------");
            System.out.println("3.-Eliminar Categoria----");
            System.out.println("4.-Actualizar Categoria--");
            System.out.println("5.-Editar Categoria------");
            System.out.println("6.--------Salir----------");
            opc = joji.nextInt();
            joji.nextLine();   //Roy Cruz Namani

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
                    listar();
                    break;
                case 5:
                    editar();
                    break;
                case 6:
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
        Categoria dd=repository.save(ca);
        System.out.println("Reporte: ");
        System.out.println(dd.getIdCategoria()+ " "+ dd.getNombre());
        anadir();
    }


    public void listar (){
        List<Categoria> datos= repository.findAll();
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
        Categoria dd=repository.save(ca);
        System.out.println("Reporte: " + dd.getIdCategoria() + " " + dd.getNombre());
    }
    public void eliminar() {
        System.out.print("Introduce el ID de la categoria para eliminar:");
        Long id = joji.nextLong();
        joji.nextLine();
        if (repository.existsById(id)) {
            repository.deleteById(id);
            System.out.println("La Categoria de ID " + id + " se elimino para siempre.");
        } else {
            System.out.println("No se encontro la ID de " + id);
        }
    }
    public void editar() {
        System.out.print("Introduce el ID de la categoria para editar:");
        Long id = joji.nextLong();
        joji.nextLine();
        if (repository.existsById(id)) {
            Categoria ca = repository.findById(id).orElse(null);
            if (ca != null) {
                System.out.println("Nombre actual: " + ca.getNombre());
                System.out.print("Introduce el nuevo nombre de la categoria:");
                String nuevoNombre = joji.nextLine();
                ca.setNombre(nuevoNombre);
                repository.save(ca);
                System.out.println("Categoría con nuevo nombre: " + ca.getIdCategoria() + " " + ca.getNombre());
            }
        } else {
            System.out.println("No se encontro la ID de " + id);
        }
    }
}
