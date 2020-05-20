package Clases.Models;

import java.io.Serializable;

public class AtractivoTuristicoMeGusta implements Serializable {
    String id;
    String idUsuario;
    String idAtractivoTuristico;
    String meGusta;

    public AtractivoTuristicoMeGusta() {
    }



    public AtractivoTuristicoMeGusta(String id, String idUsuario, String idAtractivoTuristico, String meGusta) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idAtractivoTuristico = idAtractivoTuristico;
        this.meGusta=meGusta;
    }

    public String getMeGusta() {
        return meGusta;
    }

    public void setMeGusta(String meGusta) {
        this.meGusta = meGusta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdAtractivoTuristico() {
        return idAtractivoTuristico;
    }

    public void setIdAtractivoTuristico(String idAtractivoTuristico) {
        this.idAtractivoTuristico = idAtractivoTuristico;
    }
}
