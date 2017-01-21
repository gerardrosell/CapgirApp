package com.prolificinteractive.materialcalendarview.sample;


import android.widget.CheckBox;
import android.widget.TextView;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Event_activity extends AppCompatActivity {
    public String año, dia, mes, fecha, id,Hora;
    private DatabaseReference mRootRef, mRootRefUsu;
    private TextView data, hora, nom_event;
    public String nombreEvento;
    public CheckBox Si_assisteix, No_assisteix, Va_en_bus;
    public int pos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_activity);
        Firebase.setAndroidContext(this);
        recogerExtras();
        mRootRefUsu= FirebaseDatabase.getInstance().getReference().child("Users");
        id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento").child(año).child(mes).child(dia);
        data = (TextView)findViewById(R.id.Data);
        hora = (TextView)findViewById(R.id.hora);
        nom_event = (TextView)findViewById(R.id.nom_event);
        Si_assisteix = (CheckBox)findViewById(R.id.Si);
        No_assisteix = (CheckBox)findViewById(R.id.no);
        Va_en_bus = (CheckBox) findViewById(R.id.bus_si);
        //TODO: arreglar la memoria de checks
        mRootRef.child(String.valueOf(pos+1)).child("Assistents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)){
                    Si_assisteix.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRootRef.child(String.valueOf(pos+1)).child("No_Assistents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)){
                    Si_assisteix.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRootRef.child(String.valueOf(pos+1)).child("Va En Bus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)){
                    Va_en_bus.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        hora.setText(Hora);
        }
    public void recogerExtras() {
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        dia = getIntent().getExtras().getString("dia");
        nombreEvento = getIntent().getExtras().getString("nombre");//recogemos el nombre del evento para no volverlo a leer de firebase
        pos = getIntent().getExtras().getInt("pos");
        Hora = getIntent().getExtras().getString("hora");
    }

    public void ChkSi(android.view.View view){
        No_assisteix.setChecked(false);
        Si_assisteix.setChecked(true);
        Va_en_bus.setEnabled(true);
    }
    public void ChkNo(android.view.View view){
        No_assisteix.setChecked(true);
        Si_assisteix.setChecked(false);
        Va_en_bus.setEnabled(false);
        Va_en_bus.setChecked(false);
    }

    public void ck(android.view.View view){
        mRootRefUsu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String No_ass = (No_assisteix.isChecked() ? "True" : "False");
                String Si_ass = (Si_assisteix.isChecked() ? "True" : "False");
                String Bus = (Va_en_bus.isChecked() ? "True" : "False");
                String posi = String.valueOf(pos+1);
                if(No_ass.equals( "True" )){
                    mRootRef.child(posi).child("Assistents").child(id).removeValue();
                    mRootRef.child(posi).child("Va En Bus").child(id).removeValue();
                    mRootRef.child(posi).child("No_Assistents").child(id).setValue(id);
                }else if (Si_ass.equals( "True" )){
                    mRootRef.child(posi).child("Assistents").child(id).setValue(id);
                    mRootRef.child(posi).child("No_Assistents").child(id).removeValue();
                    if (Bus.equals( "True" )){
                        mRootRef.child(posi).child("Va En Bus").child(id).setValue(id);
                    }
                    else{
                        mRootRef.child(posi).child("Va En Bus").child(id).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast toast = Toast.makeText(this, "Guardat", Toast.LENGTH_LONG);

        toast.show();
    }

    public void Cal(android.view.View view){
        Intent intent = new Intent(this, BasicActivity.class);
        startActivityForResult(intent,BasicActivity.REQUEST_NAME_CALENDARI);
    }

    /*public void LdE(android.view.View view) {
        Intent intent = new Intent( this, Event_List_activity.class );
        startActivityForResult(intent,Event_List_activity.REQUEST_NAME_LLISTA_EVENTS);
    }*/
}
