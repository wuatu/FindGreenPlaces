package Clases;

public class IdUsuario {
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
