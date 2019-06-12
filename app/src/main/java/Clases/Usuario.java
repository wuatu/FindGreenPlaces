package Clases;

import java.io.Serializable;

public class Usuario implements Serializable {
    String id;
    String nombre;
    String apellido;
    String email;
    int dia;
    int mes;
    int año;
    String password;
    String urlFotoPerfil="@drawable/com_facebook_profile_picture_blank_square";
    String nivel;
    String puntos;
    String contribuciones;
    String nombreNivel;

    public Usuario(String id,String nombre, String apellido, String email, int dia, int mes, int año, String password,String nivel, String puntos, String contribuciones, String nombreNivel) {
        this.id=id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.dia = dia;
        this.mes = mes;
        this.año = año;
        this.password = password;
        this.nivel=nivel;
        this.puntos=puntos;
        this.contribuciones=contribuciones;
        this.nombreNivel=nombreNivel;
    }

    public Usuario(String id, String nombre, String apellido, String email, int dia, int mes, int año, String password, String urlFotoPerfil, String nivel, String puntos, String contribuciones, String nombreNivel) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.dia = dia;
        this.mes = mes;
        this.año = año;
        this.password = password;
        this.urlFotoPerfil = urlFotoPerfil;
        this.nivel = nivel;
        this.puntos = puntos;
        this.contribuciones = contribuciones;
        this.nombreNivel = nombreNivel;
    }


    public Usuario() {
    }

    public String getNombreNivel() {
        return nombreNivel;
    }

    public void setNombreNivel(String nombreNivel) {
        this.nombreNivel = nombreNivel;
    }

    public String getContribuciones() {
        return contribuciones;
    }

    public void setContribuciones(String contribuciones) {
        this.contribuciones = contribuciones;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getUrlFotoPerfil() {
        return urlFotoPerfil;
    }

    public void setUrlFotoPerfil(String urlFotoPerfil) {
        this.urlFotoPerfil = urlFotoPerfil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
