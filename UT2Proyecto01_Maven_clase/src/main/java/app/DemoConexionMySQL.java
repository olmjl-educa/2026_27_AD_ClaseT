package app;

import conexion.ConexionBD;
import dao.DAOException;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class DemoConexionMySQL {

    public static void main(String[] args) {

        try (Connection conexion = new ConexionBD(Path.of("src/main/db.properties")).obtenerConexion()) {
            System.out.println("Conexión establecida OK.");
        } catch (DAOException | SQLException e) {
            System.err.println("Error al conectar " + e.getMessage());
            Throwable causa = e.getCause();
            if (causa instanceof SQLException) {
                SQLException sqle = (SQLException) causa;
                System.err.println("Motivo: " + sqle.getMessage());
                System.err.println("SQLState: " + sqle.getSQLState());
                System.err.println("Código: " + sqle.getErrorCode());
            }
        }
    }
}
