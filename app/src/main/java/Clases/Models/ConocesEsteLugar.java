package Clases.Models;

public class ConocesEsteLugar {
    String id;
    String idAtractivoTuristico;
    String idUsuario;
    String respuesta;
    String contestado;

    public ConocesEsteLugar(String id, String idAtractivoTuristico, String idUsuario, String respuesta, String contestado) {
        this.id = id;
        this.idAtractivoTuristico = idAtractivoTuristico;
        this.idUsuario = idUsuario;
        this.respuesta = respuesta;
        this.contestado = contestado;
    }

    public ConocesEsteLugar() {
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

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String isContestado() {
        return contestado;
    }

    public void setContestado(String contestado) {
        this.contestado = contestado;
    }
}
