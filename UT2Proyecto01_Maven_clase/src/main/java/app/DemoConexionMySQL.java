package app;

import conexion.ConexionBD;
import dao.DAOException;
import dao.ProductoDAO;
import dao.implementaciones.ProductoDAOImplFicheros;
import dao.implementaciones.ProductoDAOImplMySQL;
import modelo.Producto;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class DemoConexionMySQL {

    private static final int NUM_PRODUCTOS = 25;

    public static void main(String[] args) {
        Random azar = new Random();
        Path ruta = Path.of("src/main/db.properties");

        // (Connection conexion = new ConexionBD()).obtenerConexion())
        try  {
            // Nos creamos un objeto ConexionBD a partir del fichero db.properties
            ConexionBD conexionBD = new ConexionBD(ruta);
            ProductoDAO dao = new ProductoDAOImplMySQL(conexionBD);
            // 1. Rellenar con 25 productos (igual que en ACT13, pero a través del DAO).
//            for (int i = 0; i < NUM_PRODUCTOS; i++) {
//                double precio = 1 + azar.nextDouble() * 199; // Obtiene un precio entre 1 y 200
//                precio = Math.round(precio * 100) / 100.0;  // Redondea a 2 decimales
//                dao.insertar(new Producto(1001 + i, precio));
//            }

            // 2. Mostrar los productos.
            System.out.println("=== Precios iniciales ===");
            mostrar(dao.listar());

            // 3. Actualizar precios. -10% si precio > 100
            for (Producto p : dao.listar()) {
                double nuevoPrecio;
                if (p.getPrecio() > 100) {
                    nuevoPrecio = p.getPrecio() * 0.90;
                } else {
                    nuevoPrecio = p.getPrecio() * 1.10;
                }
                nuevoPrecio = (double) Math.round(nuevoPrecio * 100) / 100;
                Producto nuevo = new Producto(p.getReferencia(), nuevoPrecio);
                dao.actualizar(nuevo);
            }

            // 4. Mostrar los productos.
            System.out.println("=== Precios tras actualización de precios ===");
            mostrar(dao.listar());

            // 5. Insertar un producto para luego borrarlo
            // id=9001, precio=5.00
            dao.insertar(new Producto(9001, 5.00));

            // Mostrar lista completa
            System.out.println("=== Productos tras insertar el 9001 ===");
            mostrar(dao.listar());

            // Eliminar ese registro
            System.out.println("¿Se ha eliminado el producto 9001? " + dao.eliminar(9001));
            System.out.println("Intentando eliminar de nuevo el producto 9001? " + dao.eliminar(9001));
            //dao.eliminar(9001);

            // Volver a mostrar lista completa para comprobar borrado
            System.out.println("=== Productos tras eliminar el 9001 ===");
            mostrar(dao.listar());

            // 6. Buscar el 1001 y mostrar mensaje de existe o no
            Producto producto = dao.buscar(1001);
            // if (producto != null) {
            //     System.out.println("El producto 1001 existe.");
            // } else {
            //     System.out.println("El producto 1001 no existe.");
            // }
            System.out.println(producto != null
                    ? "El producto 1001 existe."
                    : "El producto 1001 no existe.");

            // Comprobar si existe el 9999 (que no existe)
            System.out.println("¿Existe el 9999? " + (dao.buscar(9999) != null));   //
            // Comprobar si existe el 9001 (que ya hemos eliminado)
            System.out.println("¿Existe el 9001? " + (dao.buscar(9001) != null));   //


        } catch (DAOException e) {
            System.err.println("Error al conectar " + e.getMessage());
            Throwable causa = e.getCause();
            if (causa instanceof SQLException) {
                SQLException sqle = (SQLException) causa;
                System.err.println("Motivo: " + sqle.getMessage());
                System.err.println("SQLState: " + sqle.getSQLState());
                System.err.println("Código: " + sqle.getErrorCode());
            }
        }
    } // Fin del main

    // LO SUSTITUIMOS POR LLAMADAS a toString() DE LA CLASE Producto
    private static void mostrar(List<Producto> productos) {
        for (Producto p : productos) {
            System.out.println(p);
        }
    }

    // LO SUSTITUIMOS POR LLAMADAS a toString() DE LA CLASE Producto
//    private static void mostrar0(List<Producto> productos) {
//        System.out.printf("%-10s %10s%n", "Referencia", "Precio");
//        for (Producto p : productos) {
//            System.out.printf("%-10d %10.2f%n", p.getReferencia(), p.getPrecio());
//        }
//    }
}
