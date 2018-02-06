package daw.spring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Producto {

    private long id;
    private String descripcion;
    private long precio = -1;
    private TipoProducto tipo;

    //
    @OneToMany()
    private List<Producto> listaProductos;
    //

    public Producto() {
    }

    public Producto(String descripcion, long precio, TipoProducto tipo) {
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    public enum TipoProducto{
        BOMBILLA,PERSIANA
    }
}

