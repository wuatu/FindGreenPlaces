package Clases;

import java.io.Serializable;

public class Imagen implements Serializable {
    String id;
    String url;
    String fecha;
    String idUsuario;
    String nombreUsuario;
    String contadorLike;
    String contadorVisualizaciones;

    public Imagen() {
    }

    public Imagen(String id, String url, String fecha, String idUsuario, String nombreUsuario, String contadorLike, String contadorVisualizaciones) {
        this.id = id;
        this.url = url;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contadorLike = contadorLike;
        this.contadorVisualizaciones=contadorVisualizaciones;
    }

    public String getContadorVisualizaciones() {
        return contadorVisualizaciones;
    }

    public void setContadorVisualizaciones(String contadorVisualizaciones) {
        this.contadorVisualizaciones = contadorVisualizaciones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContadorLike() {
        return contadorLike;
    }

    public void setContadorLike(String contadorLike) {
        this.contadorLike = contadorLike;
    }

    public Imagen(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
