package Clases;

import java.io.Serializable;

public class MeGustaImagen implements Serializable {
    String id;
    String idAtractivoTuristico;
    String idImagen;
    String idUsuario;
    String meGusta="";

    public MeGustaImagen(){

    }

    public MeGustaImagen(String id, String idUsuario, String idAtractivoTuristico, String idImagen, String meGusta) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idAtractivoTuristico = idAtractivoTuristico;
        this.idImagen = idImagen;
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

    public String getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(String idImagen) {
        this.idImagen = idImagen;
    }

    public String getMeGusta() {
        return meGusta;
    }

    public void setMeGusta(String meGusta) {
        this.meGusta = meGusta;
    }
}
