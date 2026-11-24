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

    private static void mostrar(List<Producto> productos) {
        System.out.printf("%-10s %10s%n", "Referencia", "Precio");
        for (Producto p : productos) {
            System.out.printf("%-10d %10.2f%n", p.getReferencia(), p.getPrecio());
        }
    }
}
