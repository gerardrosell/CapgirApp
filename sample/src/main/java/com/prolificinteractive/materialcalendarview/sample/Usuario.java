package com.prolificinteractive.materialcalendarview.sample;

/**
 * Created by mmontonv on 18/12/2016.
 */


public class Usuario {
    public String nombre;
    public String email;
    public boolean socio;
    //public boolean admin;
    public String fechaNacimiento;
    public String telefono;

    public Usuario(String nom, String email, String FNaix, String NumTel){
        this.nombre = nom;
        this.email = email;
        this.socio = false;
        //this.admin = true;
        this.fechaNacimiento = FNaix;
        this.telefono = NumTel;

    }

}
