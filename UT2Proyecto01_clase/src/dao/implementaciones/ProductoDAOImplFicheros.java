package dao.implementaciones;

import dao.DAOException;
import dao.ProductoDAO;
import modelo.Producto;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImplFicheros implements ProductoDAO {

    private static final int TAM_REGISTRO = Integer.BYTES + Double.BYTES;

    private final Path ruta;

    public ProductoDAOImplFicheros(Path ruta) throws DAOException {
        this.ruta = ruta;
        try {
           if (Files.notExists(ruta)) Files.createFile(ruta);
        } catch (IOException e) {
            throw new DAOException("No se puede crear el fichero " + ruta, e);
        }
    }

    /**
     * Guarda en el fichero un producto nuevo
     *
     * @param producto
     * @throws DAOException
     */
    @Override
    public void insertar(Producto producto) throws DAOException {
        try (RandomAccessFile raf = new RandomAccessFile(ruta.toString(), "rw")) {
            // Insertamos al final del fichero
            escribirEn(raf, contar(raf) + 1, producto);
        } catch (IOException ioe) {
            throw new DAOException("Error en insertar productos", ioe);
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
        try (RandomAccessFile raf = new RandomAccessFile(ruta.toString(), "r")) {
            List<Producto> productos = new ArrayList<>();
            for (long n = 1; n <= contar(raf); n++) {
                productos.add(leerEn(raf, n));
            }
            return productos;
        } catch (IOException ioe) {
            throw new DAOException("Error en leer productos " + ruta, ioe);
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
        try (RandomAccessFile raf = new RandomAccessFile(ruta.toString(), "rw")){
            long numero = devuelveNumeroDeRegistro(raf, producto.getReferencia());
            if (numero == -1) return false;
            escribirEn(raf, numero, producto);
            return true;
        } catch (IOException ioe) {
            throw new DAOException("Error en actualizar Producto " + producto.getReferencia(), ioe);
        }
    }

    // MÉTODOS AUXILIARES
    // Contar el número de registro que contiene el fichero
    private long contar (RandomAccessFile raf) throws IOException {
        return raf.length() /  TAM_REGISTRO;
    }
    // leer el registro 'n'
    private Producto leerEn(RandomAccessFile raf, long numRegistro) throws IOException {
        raf.seek((numRegistro - 1) * TAM_REGISTRO);
        int referencia = raf.readInt();
        double precio = raf.readDouble();
        return new Producto(referencia, precio);
    }

    // escribir un modelo.modelo.Producto en un registro concreto
    private void escribirEn(RandomAccessFile raf, long numRegistro,
                            Producto producto) throws IOException {
        raf.seek((numRegistro - 1) * TAM_REGISTRO);
        raf.writeInt(producto.getReferencia());
        raf.writeDouble(producto.getPrecio());
    }

    /**
     *
     * @param raf
     * @param referencia
     * @return El número del registro con esa referencia o -1
     * si no lo encuentra
     */
    private long devuelveNumeroDeRegistro(RandomAccessFile raf, int referencia) throws IOException {
        long totalRegistros = contar(raf);
        for (long n = 1; n <= totalRegistros ; n++) {
            raf.seek((n-1) * TAM_REGISTRO);
            if (raf.readInt() == referencia) {
                return n;
            }
        }
        return -1;
    }

}
