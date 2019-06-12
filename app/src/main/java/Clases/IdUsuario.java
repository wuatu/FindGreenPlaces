package Clases;

import java.io.Serializable;

public class IdUsuario implements Serializable {
    public static String idUsuario;
    public static String nombreUsuario;
    public static String apellidoUsuario;
    public static String correo;
    public static String url="";

    public IdUsuario(String id,String nombre,String apellido, String correoS, String urls) {
        idUsuario=id;
        nombreUsuario=nombre;
        apellidoUsuario=apellido;
        correo=correoS;
        url=urls;
    }

    public IdUsuario() {
    }

    public static void setIdUsuario(String idUsuario) {
        IdUsuario.idUsuario = idUsuario;
    }

    public static void setNombreUsuario(String nombreUsuario) {
        IdUsuario.nombreUsuario = nombreUsuario;
    }

    public static void setApellidoUsuario(String apellidoUsuario) {
        IdUsuario.apellidoUsuario = apellidoUsuario;
    }

    public static void setCorreo(String correo) {
        IdUsuario.correo = correo;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        IdUsuario.url = url;
    }

    public IdUsuario(String id) {
        idUsuario=id;
    }

    public static String getCorreo() { return correo; }

    public static String getIdUsuario() {
        return idUsuario;
    }

    public static String getNombreUsuario() {
        return nombreUsuario;
    }

    public static String getApellidoUsuario() {
        return apellidoUsuario;
    }
}
