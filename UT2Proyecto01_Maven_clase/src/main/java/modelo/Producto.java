package modelo;

public class Producto {
    private int referencia;
    private double precio;

    public Producto(int referencia, double precio) {
        this.referencia = referencia;
        this.precio = precio;
    }

    public int getReferencia() {
        return referencia;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + referencia +
                ", precio=" + precio +
                '}';
    }
}
