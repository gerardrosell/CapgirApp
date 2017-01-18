package com.prolificinteractive.materialcalendarview.sample;

/**
 * Created by mmontonv on 18/12/2016.
 */


public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private boolean socio;
    private boolean admin;
    private String fechaNacimiento;
    private String telefono;

    public Usuario(){

    }

    public void Registrat (String id, String nom, String email, String FNaix, String NumTel ){
        this.id = id;
        this.nombre = nom;
        this.email = email;
        this.socio = false;
        this.admin = false;
        this.fechaNacimiento = FNaix;
        this.telefono = NumTel;
    }
}
