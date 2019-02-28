package com.ceviche.sareb.salvisapp.Clases;

//public class Amigos {
public class Usuarios {


    String usuarioUid = "";
    String email = "";
    String nombre = "";
    String apellidos = "";
    String fotoPerfil = "";
    String biografia = "";
    String pais = "";
    String ciudad = "";
    String direccion = "";
    String tlfContacto = "";

    public Usuarios() { // Necesario
    }

    public Usuarios(String email, String usuarioUid) {
        this.email = email;
        this.usuarioUid = usuarioUid;
        this.nombre = "";
        this.apellidos = "";
        this.fotoPerfil = "";
        this.biografia = "";
        this.pais = "";
        this.ciudad = "";
        this.direccion = "";
    }

    // USADO EN REGISTRO
    public Usuarios(String email, String usuarioUid, String nombre, String apellidos, String pais, String ciudad, String direccion, String tlfContacto) {
        this.email = email;
        this.usuarioUid = usuarioUid;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fotoPerfil = "";
        this.biografia = "";
        this.pais = pais;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.tlfContacto = tlfContacto;
    }


    // USADO EN ACTUALIZACION
    public Usuarios(String email, String usuarioUid, String nombre, String apellidos, String fotoPerfil, String biografia, String pais, String ciudad, String direccion, String tlfContacto) {
        this.email = email;
        this.usuarioUid = usuarioUid;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fotoPerfil = fotoPerfil;
        this.biografia = biografia;
        this.pais = pais;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.tlfContacto = tlfContacto;
    }


    public String getTlfContacto() {
        return tlfContacto;
    }

    public void setTlfContacto(String tlfContacto) {
        this.tlfContacto = tlfContacto;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getPais() {
        return pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getDireccion() {
        return direccion;
    }


    public String getUsuarioUid() {
        return usuarioUid;
    }
}
//}