package Clases;

public class Contribucion {
    String id;
    String idAtractivoTuristico;
    String idUsuario;
    String contribucionNombre;
    String contribucion;

    public Contribucion(String id, String idAtractivoTuristico, String idUsuario, String contribucionNombre, String contribucion) {
        this.id = id;
        this.idAtractivoTuristico = idAtractivoTuristico;
        this.idUsuario = idUsuario;
        this.contribucionNombre = contribucionNombre;
        this.contribucion = contribucion;
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
