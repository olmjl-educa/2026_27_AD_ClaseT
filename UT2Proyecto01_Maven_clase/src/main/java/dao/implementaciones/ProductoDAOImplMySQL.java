package dao.implementaciones;

import conexion.ConexionBD;
import dao.DAOException;
import dao.ProductoDAO;
import modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ProductoDAOImplMySQL implements ProductoDAO {
    private static final String SQL_INSERTAR = """
                INSERT INTO productos (id_producto, precio) VALUES (?, ?);
            """;
    private static final String SQL_LISTAR =
            "SELECT id_producto, precio FROM productos ORDER BY id_producto";
    private static final String SQL_ACTUALIZAR =
            "UPDATE productos SET precio = 28.00 WHERE id_producto = 1001";

    private final ConexionBD conexionBD;

    public ProductoDAOImplMySQL(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Guarda un producto nuevo donde corresponda
     *
     * @param producto
     * @throws DAOException
     */
    @Override
    public void insertar(Producto producto) throws DAOException {
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, producto.getReferencia());
            ps.setDouble(2, producto.getPrecio());
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DAOException("Ya existe un producto con la referencia:" + producto.getReferencia(), e);
        } catch (SQLException e) {
            throw new DAOException("Error al insertar producto " + producto.getReferencia(), e);
        }
    }

    /**
     * Devuelve una lista de todos los productos
     *
     * @return
     * @throws DAOException
     */
    @Override
    public List<Producto> listar() throws DAOException {
        return List.of();
    }

    /**
     * Cambia el precio de un producto existente.
     *
     * @param producto
     * @return false si la referencia no existe
     * @throws DAOException
     */
    @Override
    public boolean actualizar(Producto producto) throws DAOException {
        return false;
    }
}
