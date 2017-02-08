package com.prolificinteractive.materialcalendarview.sample;

import java.util.ArrayList;

/**
 * Created by mmontonv on 18/12/2016.
 */


public class Usuario {
    public String nombre;
    public String email;
    public String telefono;
    public ArrayList<String> listItems;

    public Usuario(String nom, String email, String NumTel, ArrayList<String> listI){
        this.nombre = nom;
        this.email = email;
        this.telefono = NumTel;
        this.listItems=listI;

    }

}
