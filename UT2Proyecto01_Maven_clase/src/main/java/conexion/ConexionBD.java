package conexion;

import dao.DAOException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {
    private final String url;
    private final String usuario;
    private final String password;

    public ConexionBD(Path ficheroPropiedades) throws DAOException {
        // Leemos el fichero de propiedades de conexión
        // con el servidor MySQL
        Properties propiedadesConexion = new Properties();
        try (BufferedReader lector = Files.newBufferedReader(ficheroPropiedades)) {
            propiedadesConexion.load(lector);
        } catch (IOException e) {
            throw new DAOException("No se puede leer el fichero properties. " + ficheroPropiedades);
        }
        this.url = propiedadesConexion.getProperty("url");
        this.usuario = propiedadesConexion.getProperty("usuario");
        this.password = propiedadesConexion.getProperty("password");

        if (url == null || usuario == null || password == null) {
            throw new DAOException("Faltan datos para la conexión.");
        }
    } // Fin del constructor

    public Connection obtenerConexion() throws DAOException {
        try {
            Connection conexion = DriverManager.getConnection(url, usuario, password);
            return conexion;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DAOException("Error en obtenerConexion" + sqle.getMessage());

        }
    }
}
