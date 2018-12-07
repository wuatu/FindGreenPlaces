package Clases;

public class AtractivoTuristico {
    String descripcion;
    Double latitud;
    Double longitud;
    String nombre;
    String ciudad;
    String comuna;

    public AtractivoTuristico() {
    }

    public AtractivoTuristico(String nombre, String ciudad, String comuna, String descripcion, Double latitud, Double longitud) {
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.comuna = comuna;
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
}
