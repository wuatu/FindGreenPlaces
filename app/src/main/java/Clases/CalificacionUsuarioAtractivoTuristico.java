package Clases;

import java.io.Serializable;

public class CalificacionUsuarioAtractivoTuristico implements Serializable {
    double calificacion;


    public CalificacionUsuarioAtractivoTuristico(double calificacion) {
        this.calificacion = calificacion;
    }

    public CalificacionUsuarioAtractivoTuristico() {
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }
}
