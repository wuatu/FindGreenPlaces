package Clases;

public class IdUsuario {
    public static String idUsuario;
    public static String nombreUsuario;
    public static String apellidoUsuario;

    public IdUsuario(String id,String nombre,String apellido) {
        idUsuario=id;
        nombreUsuario=nombre;
        apellidoUsuario=apellido;
    }
    public IdUsuario(String id) {
        idUsuario=id;
    }



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
