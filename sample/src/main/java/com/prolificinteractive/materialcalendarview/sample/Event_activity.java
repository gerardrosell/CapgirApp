package com.prolificinteractive.materialcalendarview.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.core.view.View;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Event_activity extends AppCompatActivity {
    public String año, dia, mes, fecha;
    private DatabaseReference mRootRef, mRootRefUsu;
    private TextView data, hora, nom_event;
    public String nombreEvento;
    public CheckBox Si_assisteix, No_assisteix, Va_en_bus;

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
        Si_assisteix = (CheckBox)findViewById(R.id.Si);
        No_assisteix = (CheckBox)findViewById(R.id.no);
        Va_en_bus = (CheckBox) findViewById(R.id.bus_si);
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

    public void ActualitzaSiAssiteixAlEvent(final String id){
        mRootRefUsu= FirebaseDatabase.getInstance().getReference().child("Users");
        mRootRefUsu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(No_assisteix.isChecked()){
                    mRootRefUsu.child(id).child("Assist").setValue(false);
                }else{
                    mRootRefUsu.child(id).child("Assist").setValue(true);
                    if(Va_en_bus.isChecked()) mRootRefUsu.child(id).child("Va En Bus").setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
