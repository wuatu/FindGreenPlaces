package Clases;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cristian.findgreenplaces.R;

import java.io.Serializable;

public class Comentario implements Serializable {
    String id;
    String comentario;
    String idUsuario;
    String nombreUsuario;
    String apellidoUsuario;
    String contadorLike;
    String contadorDislike;
    public int imageViewLike=0;
    public int imageViewDislike=0;
    String contadorReportes="0";
    String visible;

    public Comentario() {
    }

    public Comentario(String id, String comentario, String idUsuario, String nombreUsuario, String apellidoUsuario, String contadorLike, String contadorDislike, String visible) {
        this.id=id;
        this.comentario = comentario;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario=apellidoUsuario;
        this.contadorLike=contadorLike;
        this.contadorDislike=contadorDislike;
        this.visible=visible;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getContadorReportes() {
        return contadorReportes;
    }

    public void setContadorReportes(String contadorReportes) {
        this.contadorReportes = contadorReportes;
    }

    public int getImageViewLike() {
        return imageViewLike;
    }

    public void setImageViewLike(int imageViewLike) {
        this.imageViewLike = imageViewLike;
    }

    public int getImageViewDislike() {
        return imageViewDislike;
    }

    public void setImageViewDislike(int imageViewDislike) {
        this.imageViewDislike = imageViewDislike;
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
