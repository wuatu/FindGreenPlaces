package Clases.Models;

import java.io.Serializable;

public class Categoria implements Serializable {
    String id;
    String etiqueta;


    public Categoria() {
    }
    public Categoria(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Categoria(String id, String etiqueta) {
        this.id = id;
        this.etiqueta = etiqueta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}
