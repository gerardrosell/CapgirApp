package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class Edit_event_activity extends AppCompatActivity {

    private DatabaseReference mRootRef;
    private String key;
    private int hour;
    private String año;
    private String mes, mes2;
    private String dia;
    private long NAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mRootRef= FirebaseDatabase.getInstance().getReference().child("Evento");

        recogerExtras();

        key=String.format(Locale.getDefault(), "%d", (NAct+1));
        Button create_btn = (Button) findViewById(R.id.create_btn);
        final EditText nom_event = (EditText) findViewById(R.id.nom_event);
        final TextView showDate = (TextView) findViewById(R.id.showDate);
        mes2 = String.valueOf(Integer.parseInt(mes)+1);
        showDate.setText(dia+"/"+mes2+"/"+año);
        //final DatePicker selectDay = (DatePicker) findViewById(R.id.selectDay);
        final TimePicker selectHour = (TimePicker) findViewById(R.id.selectHour);
        final EditText desc = (EditText) findViewById(R.id.Descripcio);
        //final int day = selectDay.getDayOfMonth();
        //final int month = selectDay.getMonth();
        //final int year = selectDay.getYear();


        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final int day = selectDay.getDayOfMonth();
                //final int month = selectDay.getMonth();
                //final int year = selectDay.getYear();
                final String hour = "10:00";//selectHour.getText().toString();
                final String name = nom_event.getText().toString();
                final String descrip = desc.getText().toString();

                /*mRootRef.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //if(dataSnapshot.exists()){
                            long cont = dataSnapshot.getChildrenCount();
                            cont++;
                            Log.v("cont: ", String.format(Locale.getDefault(), "%d", cont));
                            key = String.format(Locale.getDefault(), "%d", cont);
                        //}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                mRootRef.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long cont = dataSnapshot.getChildrenCount();
                        cont++;
                        Log.v("cont: ", String.format(Locale.getDefault(), "%d", cont));
                        key = String.format(Locale.getDefault(), "%d", cont);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
                mRootRef.child(String.valueOf(año)).child(String.valueOf(mes)).child(String.valueOf(dia)).
                        child(key).setValue(new Event(name,hour,descrip));

                //mRootRef.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).child(key).setValue(name+' '+String.valueOf(hour)
                Intent intent = new Intent(Edit_event_activity.this, BasicActivity.class);
                startActivity(intent);


            }
        });


    }

    public void recogerExtras() {
        /*GUARDAMOS EN LA ACTIVIDAD LOS PARÁMETROS ENVIADOS DESDE OTRA ACTIVIDAD*/
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        dia = getIntent().getExtras().getString("dia");
        NAct = getIntent().getExtras().getLong("cont");
    }


}
