package com.prolificinteractive.materialcalendarview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Edit_event_activity extends AppCompatActivity {

    private DatabaseReference mRootRef;
    private String key="69";
    private int hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event_activity);

        final Event event;

        mRootRef= FirebaseDatabase.getInstance().getReference().child("Evento");

        Button create_btn = (Button) findViewById(R.id.create_btn);
        final EditText nom_event = (EditText) findViewById(R.id.nom_event);
        final DatePicker selectDay = (DatePicker) findViewById(R.id.selectDay);
        final TimePicker selectHour = (TimePicker) findViewById(R.id.selectHour);

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = selectDay.getDayOfMonth();
                int month = selectDay.getMonth();
                int year = selectDay.getYear();
                int hour = 12;
                String name = nom_event.getText().toString();

                /*mRootRef.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long cont = dataSnapshot.getChildrenCount();
                        key = String.format(Locale.getDefault(), "%d", cont);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

                mRootRef.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day)).child(key).setValue(name+' '+String.valueOf(hour));
                //Map<Integer, Event> map = new HashMap<Integer, Event>();
                //map.put(key, new Event(nom_event.toString(), hour));
                //Firebase ref = new Firebase(url).child("12345");
                //mRootRef.setValue(map);
            }
        });


    }


}
