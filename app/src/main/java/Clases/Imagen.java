package Clases;

import java.io.Serializable;

public class Imagen implements Serializable {
    String url;

    public Imagen() {
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
