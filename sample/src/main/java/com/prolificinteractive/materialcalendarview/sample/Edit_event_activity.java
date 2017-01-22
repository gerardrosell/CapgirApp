package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Edit_event_activity extends AppCompatActivity {

    private DatabaseReference mRootRef;
    private String key="1";
    private int hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event_activity);

        mRootRef= FirebaseDatabase.getInstance().getReference().child("Evento");

        Button create_btn = (Button) findViewById(R.id.create_btn);
        final EditText nom_event = (EditText) findViewById(R.id.nom_event);
        final DatePicker selectDay = (DatePicker) findViewById(R.id.selectDay);
        final EditText selectHour = (EditText) findViewById(R.id.selectHour);
        final EditText desc = (EditText) findViewById(R.id.Descripcio);
        final int day = selectDay.getDayOfMonth();
        final int month = selectDay.getMonth();
        final int year = selectDay.getYear();

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
        });

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int day = selectDay.getDayOfMonth();
                final int month = selectDay.getMonth();
                final int year = selectDay.getYear();
                final String hour = selectHour.getText().toString();
                final String name = nom_event.getText().toString();
                final String descrip = desc.getText().toString();


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
                });
                mRootRef.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).
                        child(key).setValue(new Event(name,hour,descrip));

                //mRootRef.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).child(key).setValue(name+' '+String.valueOf(hour)
                Intent intent = new Intent(Edit_event_activity.this, BasicActivity.class);
                startActivity(intent);


            }
        });


    }


}
