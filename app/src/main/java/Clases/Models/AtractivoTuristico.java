package Clases.Models;

import java.io.Serializable;

public class AtractivoTuristico implements Serializable {
    String id;
    String nombre;
    String ciudad;
    String comuna;
    String descripcion;
    Double latitud;
    Double longitud;
    String tipsDeViaje="";
    String horarioDeAtencion="";
    String telefono="";
    String paginaWeb="";
    String redesSociales="";
    String descripcionCorta="";
    String contadorMeGusta="0";
    String contadorReportes="0";
    String contadorVisualizaciones;
    String calificacion;
    String idUsuario;
    String visible;
    String contadorOpiniones;
    String urlFoto;

    public AtractivoTuristico(String id,String idUsuario, String nombre, String ciudad, String comuna, String descripcion, Double latitud, Double longitud,String contadorVisualizaciones, String contadorReportes, String calificacion, String visible, String contadorOpiniones, String urlFoto) {
        this.id = id;
        this.idUsuario=idUsuario;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.comuna = comuna;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.contadorVisualizaciones=contadorVisualizaciones;
        this.contadorReportes=contadorReportes;
        this.calificacion=calificacion;
        this.visible=visible;
        this.contadorOpiniones=contadorOpiniones;
        this.urlFoto=urlFoto;
    }

    public AtractivoTuristico(String id, String nombre, String ciudad, String comuna, String descripcion, Double latitud, Double longitud, String tipsDeViaje, String horarioDeAtencion, String telefono, String paginaWeb, String redesSociales, String descripcionCorta, String contadorMeGusta, String contadorReportes, String contadorVisualizaciones, String calificacion, String idUsuario, String visible, String contadorOpiniones, String urlFoto) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.comuna = comuna;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tipsDeViaje = tipsDeViaje;
        this.horarioDeAtencion = horarioDeAtencion;
        this.telefono = telefono;
        this.paginaWeb = paginaWeb;
        this.redesSociales = redesSociales;
        this.descripcionCorta = descripcionCorta;
        this.contadorMeGusta = contadorMeGusta;
        this.contadorReportes = contadorReportes;
        this.contadorVisualizaciones = contadorVisualizaciones;
        this.calificacion = calificacion;
        this.idUsuario = idUsuario;
        this.visible = visible;
        this.contadorOpiniones = contadorOpiniones;
        this.urlFoto = urlFoto;
    }

    public String getContadorOpiniones() {
        return contadorOpiniones;
    }

    public void setContadorOpiniones(String contadorOpiniones) {
        this.contadorOpiniones = contadorOpiniones;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getContadorReportes() {
        return contadorReportes;
    }

    public void setContadorReportes(String contadorReportes) {
        this.contadorReportes = contadorReportes;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContadorVisualizaciones() {
        return contadorVisualizaciones;
    }

    public void setContadorVisualizaciones(String contadorVisualizaciones) {
        this.contadorVisualizaciones = contadorVisualizaciones;
    }

    public String getContadorMeGusta() {
        return contadorMeGusta;
    }

    public void setContadorMeGusta(String contadorMeGusta) {
        this.contadorMeGusta = contadorMeGusta;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public String getTipsDeViaje() {
        return tipsDeViaje;
    }

    public void setTipsDeViaje(String tipsDeViaje) {
        this.tipsDeViaje = tipsDeViaje;
    }

    public String getHorarioDeAtencion() {
        return horarioDeAtencion;
    }

    public void setHorarioDeAtencion(String horarioDeAtencion) {
        this.horarioDeAtencion = horarioDeAtencion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public String getRedesSociales() {
        return redesSociales;
    }

    public void setRedesSociales(String redesSociales) {
        this.redesSociales = redesSociales;
    }

    public AtractivoTuristico() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
