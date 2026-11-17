package dao;

import modelo.Producto;
import java.util.List;

public interface ProductoDAO {
    /**
     * Guarda en el fichero un producto nuevo
     * @param producto
     * @throws DAOException
     */
    void insertar(Producto producto) throws DAOException;

    /**
     * Devuelve una lista de todos los productos
     * @return
     * @throws DAOException
     */
    List<Producto> listar() throws DAOException;

    /**
     * Cambia el precio de un producto existente.
     * @param producto
     * @return false si la referencia no existe
     * @throws DAOException
     */
    boolean actualizar(Producto producto) throws DAOException;
}
