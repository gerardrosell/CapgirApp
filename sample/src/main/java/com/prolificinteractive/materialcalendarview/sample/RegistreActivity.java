package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegistreActivity extends AppCompatActivity {

    private DatabaseReference mRootRefUsu;
    private ArrayList<String> listUsu=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private EditText nouNom;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button registram = (Button) findViewById(R.id.registra);
        final EditText Nom = (EditText) findViewById(R.id.Nom);
        final EditText Email = (EditText) findViewById(R.id.email);
        final EditText telefon = (EditText) findViewById(R.id.telefon);
        list = (ListView) findViewById(R.id.UserDeviceList);
        adapter= new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1,
                listUsu);
        nouNom = (EditText) findViewById(R.id.nouNom);
        list.setAdapter(adapter);


        Intent myIntent = getIntent(); // gets the previously created intent
        final String id = myIntent.getStringExtra("id");
        final Intent i = new Intent(this, BasicActivity.class);

        registram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Nom.getText().toString().equals("") || Email.getText().toString().equals("") || telefon.getText().toString().equals("")){
                    Toast toast = Toast.makeText(RegistreActivity.this, R.string.dades_inc, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    mRootRefUsu= FirebaseDatabase.getInstance().getReference().child("Users");
                    mRootRefUsu.child(id).setValue(new Usuario(Nom.getText().toString(),
                            Email.getText().toString(), telefon.getText().toString(),listUsu));
                    startActivity(i);
                }

            }
        });
    }


    public void AddItem(View view) {
        listUsu.add(nouNom.getText().toString());
        adapter.notifyDataSetChanged();
        nouNom.setText("");

    }
}
