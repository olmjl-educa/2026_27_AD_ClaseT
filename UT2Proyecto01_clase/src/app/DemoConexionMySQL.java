package app;

import conexion.ConexionBD;
import dao.DAOException;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class DemoConexionMySQL {

    static void main() {

        try (Connection conexion = new ConexionBD(Path.of("src/db.properties")).obtenerConexion()) {
            System.out.println("Conexión establecida OK.");
        } catch (DAOException e) {
            System.err.println("Error al conectar " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar " + e.getMessage());
        }
    }
}
