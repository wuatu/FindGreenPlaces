package Clases.Models;

import java.io.Serializable;

public class Imagen implements Serializable {
    String id;
    String url;
    String fecha;
    String idUsuario;
    String idAtractivo;
    String nombreUsuario;
    String contadorLike;
    String contadorVisualizaciones;
    String contadorReportes;
    String visible;

    public Imagen() {
    }

    public Imagen(String id, String url, String fecha ,String idAtractivo, String idUsuario, String nombreUsuario, String contadorLike, String contadorVisualizaciones, String contadorReportes, String visible) {
        this.id = id;
        this.url = url;
        this.fecha = fecha;
        this.idAtractivo=idAtractivo;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contadorLike = contadorLike;
        this.contadorVisualizaciones=contadorVisualizaciones;
        this.contadorReportes=contadorReportes;
        this.visible=visible;
    }



    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getIdAtractivo() {
        return idAtractivo;
    }

    public void setIdAtractivo(String idAtractivo) {
        this.idAtractivo = idAtractivo;
    }

    public String getContadorReportes() {
        return contadorReportes;
    }

    public void setContadorReportes(String contadorReportes) {
        this.contadorReportes = contadorReportes;
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
