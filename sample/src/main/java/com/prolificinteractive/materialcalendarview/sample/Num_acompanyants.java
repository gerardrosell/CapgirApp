package com.prolificinteractive.materialcalendarview.sample;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Num_acompanyants extends AppCompatActivity {

    private String año, mes, mes2, dia, nombreEvento;
    private int pos;
    private DatabaseReference mRootRef;
    private TextView showDate;
    private Button guardar;
    private EditText num_acomp;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_acompanyants);
        recogerExtras();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento").child(año).child(mes2).child(dia).child(String.valueOf(pos)).child("Va En Bus Acompanyants");
        id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        showDate = (TextView) findViewById(R.id.showDate);
        guardar = (Button) findViewById(R.id.guardar);
        num_acomp = (EditText) findViewById(R.id.num_acomp);

    }

    @Override
    protected void onStart() {
        super.onStart();
        showDate.setText(dia+"/"+mes+"/"+año);
        mRootRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    num_acomp.setText(dataSnapshot.getValue().toString());
                } else{
                    num_acomp.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootRef.child(id).setValue(num_acomp.getText().toString());
                Toast toast = Toast.makeText(Num_acompanyants.this, "Guardat", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void recogerExtras() {
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        mes2 = mes;
        mes=String.valueOf(Integer.parseInt(mes)+1);
        dia = getIntent().getExtras().getString("dia");
        pos = getIntent().getExtras().getInt("pos");
        pos++;
    }
}
