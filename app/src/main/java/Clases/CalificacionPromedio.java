package Clases;

public class CalificacionPromedio {
    String promedioCalificacion;
    String totalPersonas;

    public CalificacionPromedio(String promedioCalificacion, String totalPersonas) {
        this.promedioCalificacion = promedioCalificacion;
        this.totalPersonas = totalPersonas;
    }

    public CalificacionPromedio() {
    }

    public String getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public void setPromedioCalificacion(String promedioCalificacion) {
        this.promedioCalificacion = promedioCalificacion;
    }

    public String getTotalPersonas() {
        return totalPersonas;
    }

    public void setTotalPersonas(String totalPersonas) {
        this.totalPersonas = totalPersonas;
    }
}
