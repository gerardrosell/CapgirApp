package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

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
        final NumberPicker day = (NumberPicker) findViewById(R.id.day);
        final NumberPicker month = (NumberPicker) findViewById(R.id.month);
        final NumberPicker year = (NumberPicker) findViewById(R.id.year);
        Intent myIntent = getIntent(); // gets the previously created intent
        final String id = myIntent.getStringExtra("id");
        final Intent i = new Intent(this, BasicActivity.class);

        registram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootRefUsu= FirebaseDatabase.getInstance().getReference().child("Users");
                mRootRefUsu.child(id).setValue(new Usuario(Nom.getText().toString(), Email.getText().toString(), String.valueOf(day.getValue())+"/"+String.valueOf(month.getValue())+"/"+String.valueOf(year.getValue()), telefon.getText().toString()));
                startActivity(i);
            }
        });


    }
}
