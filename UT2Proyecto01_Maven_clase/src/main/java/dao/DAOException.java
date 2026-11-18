package dao;

public class DAOException extends Exception {
    public DAOException(String mensaje) {
        super(mensaje);
    }

    // Para guardar la causa original (IOException, SQLException,...)
    public DAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
