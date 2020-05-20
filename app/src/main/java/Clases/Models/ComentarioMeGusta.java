package Clases.Models;

import java.io.Serializable;

public class ComentarioMeGusta implements Serializable {
    String id;
    String idUsuario;
    String idAtractivoTuristico;
    String idComentario;
    String meGustaComentario="";

    public ComentarioMeGusta(){

    }



    public ComentarioMeGusta(String id, String idUsuario, String idAtractivoTuristico, String idComentario, String meGustaComentario) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idAtractivoTuristico = idAtractivoTuristico;
        this.idComentario = idComentario;
        this.meGustaComentario = meGustaComentario;
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

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getMeGustaComentario() {
        return meGustaComentario;
    }

    public void setMeGustaComentario(String meGustaComentario) {
        this.meGustaComentario = meGustaComentario;
    }
}
