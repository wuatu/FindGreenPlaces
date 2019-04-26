package Clases;

public class AtractivoTuristicoMeGusta {
    String id;
    String idUsuario;
    String idAtractivoTuristico;

    public AtractivoTuristicoMeGusta() {
    }

    public AtractivoTuristicoMeGusta(String id, String idUsuario, String idAtractivoTuristico) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idAtractivoTuristico = idAtractivoTuristico;
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
