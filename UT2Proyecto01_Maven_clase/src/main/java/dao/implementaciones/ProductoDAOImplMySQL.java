package dao.implementaciones;

import conexion.ConexionBD;
import dao.DAOException;
import dao.ProductoDAO;
import modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImplMySQL implements ProductoDAO {
    private static final String SQL_INSERTAR = """
                INSERT INTO productos (id_producto, precio) VALUES (?, ?);
            """;
    private static final String SQL_LISTAR =
            "SELECT id_producto, precio FROM productos ORDER BY id_producto";
    private static final String SQL_ACTUALIZAR =
            //"UPDATE productos SET precio = 28.00 WHERE id_producto = 1001";
            "UPDATE productos SET precio = ? WHERE id_producto = ?";
    private static final String SQL_BORRAR =
            "DELETE FROM productos WHERE id_producto = ?";

    private static final String SQL_BUSCAR =
            "SELECT id_producto, precio FROM productos WHERE id_producto = ?";

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
        List<Producto> productos = new ArrayList<>();
        int id;
        double precio;
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_LISTAR)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id =  rs.getInt("id_producto");
                precio = rs.getDouble("precio");
                //productos.add(new Producto(rs.getInt("id_producto"), rs.getDouble("precio")));
                productos.add(new Producto(id, precio));
            }
            return productos;

        } catch (SQLException e) {
            throw new DAOException("Error al listar productos ", e);
        }
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
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setDouble(1, producto.getPrecio());
            ps.setInt(2, producto.getReferencia());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar producto " + producto.getReferencia(), e);
        }

    }

    @Override
    public boolean eliminar(int referencia) throws DAOException {
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_BORRAR)) {
            ps.setInt(1, referencia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al borrar producto " + referencia, e);
        }
    }

    @Override
    public Producto buscar(int referencia) throws DAOException {
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_BUSCAR)) {
            ps.setInt(1, referencia);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {  // Hemos encontrado el producto con ese ID
                //return new Producto(rs.getInt("id_producto"), rs.getDouble("precio"));
                return new Producto(rs.getInt(1), rs.getDouble(2));
            }
            return null;

        } catch (SQLException e) {
            throw new DAOException("Error al buscar producto " + referencia, e);
        }
    }
}
