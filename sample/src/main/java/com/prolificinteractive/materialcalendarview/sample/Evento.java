package com.prolificinteractive.materialcalendarview.sample;

/**
 * Created by mmontonv on 18/12/2016.
 */

public class Evento {
    private String Nombre;
    private String participantes;
    private String año;
    private String mes;
    private String dia;
    private String autobus;


    public Evento(String year, String month, String day){
        Nombre = "aqui poner el string que se introduce por pantalla";
        participantes = "count del número de asistentes";
        año = year;
        mes = month;
        dia = day;
        autobus = "count de cuantos iran en autobus";// se podría inicializar en 0 e ir incrementando cada vez que alguien sube que ira;
    }
}
