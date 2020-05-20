package Clases.Models;

import java.io.Serializable;

public class Contribucion implements Serializable {
    String id;
    String idAtractivoTuristico;
    String idUsuario;
    String contribucionNombre;
    String contribucion;
    String visible;

    public Contribucion(String id, String idAtractivoTuristico, String idUsuario, String contribucionNombre, String contribucion, String visible) {
        this.id = id;
        this.idAtractivoTuristico = idAtractivoTuristico;
        this.idUsuario = idUsuario;
        this.contribucionNombre = contribucionNombre;
        this.contribucion = contribucion;
        this.visible=visible;
    }


    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAtractivoTuristico() {
        return idAtractivoTuristico;
    }

    public void setIdAtractivoTuristico(String idAtractivoTuristico) {
        this.idAtractivoTuristico = idAtractivoTuristico;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContribucionNombre() {
        return contribucionNombre;
    }

    public void setContribucionNombre(String contribucionNombre) {
        this.contribucionNombre = contribucionNombre;
    }

    public String getContribucion() {
        return contribucion;
    }

    public void setContribucion(String contribucion) {
        this.contribucion = contribucion;
    }
}
