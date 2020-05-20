package Clases.Models;

import java.io.Serializable;

public class CalificacionPromedio implements Serializable {
    double promedioCalificacion;
    int totalPersonas;
    double sumaDeCalificaciones;

    public CalificacionPromedio(double promedioCalificacion, int totalPersonas, double sumaDeCalificaciones) {
        this.promedioCalificacion = promedioCalificacion;
        this.totalPersonas = totalPersonas;
        this.sumaDeCalificaciones = sumaDeCalificaciones;
    }



    public double getSumaDeCalificaciones() {
        return sumaDeCalificaciones;
    }

    public void setSumaDeCalificaciones(double sumaDeCalificaciones) {
        this.sumaDeCalificaciones = sumaDeCalificaciones;
    }

    public CalificacionPromedio() {
    }

    public double getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public void setPromedioCalificacion(double promedioCalificacion) {
        this.promedioCalificacion = promedioCalificacion;
    }

    public int getTotalPersonas() {
        return totalPersonas;
    }

    public void setTotalPersonas(int totalPersonas) {
        this.totalPersonas = totalPersonas;
    }
}
