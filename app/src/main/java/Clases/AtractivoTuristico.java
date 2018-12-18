package Clases;

import java.io.Serializable;

public class AtractivoTuristico implements Serializable {
    String id;
    String nombre;
    String ciudad;
    String comuna;
    String descripcion;
    Double latitud;
    Double longitud;


    public AtractivoTuristico(String id, String nombre, String ciudad, String comuna, String descripcion, Double latitud, Double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.comuna = comuna;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public AtractivoTuristico() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
