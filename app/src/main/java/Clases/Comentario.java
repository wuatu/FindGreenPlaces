package Clases;

import android.widget.TextView;

public class Comentario {
    String id;
    String comentario;
    String idUsuario;
    String nombreUsuario;
    String apellidoUsuario;
    String contadorLike;
    String contadorDislike;
    public Comentario() {
    }

    public Comentario(String id, String comentario, String idUsuario, String nombreUsuario, String apellidoUsuario, String contadorLike, String contadorDislike) {
        this.id=id;
        this.comentario = comentario;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario=apellidoUsuario;
        this.contadorLike=contadorLike;
        this.contadorDislike=contadorDislike;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContadorDislike() {
        return contadorDislike;
    }

    public void setContadorDislike(String contadorDislike) {
        this.contadorDislike = contadorDislike;
    }

    public String getContadorLike() {
        return contadorLike;
    }

    public void setContadorLike(String contadorLike) {
        this.contadorLike = contadorLike;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getIdUsuario() {
        return idUsuario;
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
}
