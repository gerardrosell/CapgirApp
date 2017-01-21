package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistreActivity extends AppCompatActivity {

    private DatabaseReference mRootRefUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);
        Button registram = (Button) findViewById(R.id.registra);
        final EditText Nom = (EditText) findViewById(R.id.Nom);
        final EditText Email = (EditText) findViewById(R.id.email);
        final EditText telefon = (EditText) findViewById(R.id.telefon);
        final DatePicker selectDayNaix = (DatePicker) findViewById(R.id.selectDayNaix);


        Intent myIntent = getIntent(); // gets the previously created intent
        final String id = myIntent.getStringExtra("id");
        final Intent i = new Intent(this, BasicActivity.class);

        registram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int day = selectDayNaix.getDayOfMonth();
                final int month = selectDayNaix.getMonth();
                final int year = selectDayNaix.getYear();
                mRootRefUsu= FirebaseDatabase.getInstance().getReference().child("Users");
                mRootRefUsu.child(id).setValue(new Usuario(Nom.getText().toString(),
                        Email.getText().toString(), String.valueOf( day )
                        +"/"+String.valueOf( month+1 )+ "/" +String.valueOf( year ), telefon.getText().toString()));
                startActivity(i);
            }
        });


    }
}
