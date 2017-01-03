package com.prolificinteractive.materialcalendarview.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Event_activity extends AppCompatActivity {
    public String año;
    public String dia;
    public String mes;
    public String fecha;
    private DatabaseReference mRootRef;
    private TextView data;
    private TextView hora;
    private TextView nom_event;
    public String nombreEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_activity);
        Firebase.setAndroidContext(this);
        recogerExtras();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento").child(año).child(mes).child(dia);
        data = (TextView)findViewById(R.id.Data);
        hora = (TextView)findViewById(R.id.hora);
        nom_event = (TextView)findViewById(R.id.nom_event);
    }
    @Override
    protected void onStart() {
        super.onStart();
        /* corrección del valor del mes*/
        int mes_a_corregir = Integer.parseInt(mes);
        int mes_corregido = mes_a_corregir+1;
        mes = String.valueOf(mes_corregido);
        fecha = dia+"/"+mes+"/"+año;
        data.setText(fecha);
       nom_event.setText(nombreEvento);//utilizamos el nombre leido en event_list_activity
    }
    public void recogerExtras() {
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        dia = getIntent().getExtras().getString("dia");
        nombreEvento = getIntent().getExtras().getString("nombre");//recogemos el nombre del evento para no volverlo a leer de firebase
    }
}
