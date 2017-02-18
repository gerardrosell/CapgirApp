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

    private String año, mes, mes2, dia, nombre;
    private int pos;
    private DatabaseReference mRootRef;
    private TextView showDate;
    private Button guardar;
    private EditText num_acomp;
    private String id;
    private String NmesA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_acompanyants);
        recogerExtras();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento").child(año).child(mes2).child(dia).child(String.valueOf(pos)).child("Va En Bus");
        id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        showDate = (TextView) findViewById(R.id.showDate);
        guardar = (Button) findViewById(R.id.guardar);
        num_acomp = (EditText) findViewById(R.id.num_acomp);

    }

    @Override
    protected void onStart() {
        super.onStart();
        showDate.setText(dia+"/"+mes+"/"+año);
        mRootRef.child(id+nombre+"Acompanyants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    NmesA=dataSnapshot.getValue().toString();
                    //NmesA=NmesA.substring(0,NmesA.length()-1);
                    String[] sep = NmesA.split(" - ");
                    num_acomp.setText(sep[1]);
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
                String valor;
                if(num_acomp.getText().toString()==null){
                    valor="0";
                } else{valor=num_acomp.getText().toString();}
                if(!valor.equals("0")){
                    mRootRef.child(id+nombre+"Acompanyants").setValue("("+nombre+") - "+valor);
                } else{
                    mRootRef.child(id+nombre+"Acompanyants").removeValue();
                }
                Toast toast = Toast.makeText(Num_acompanyants.this, "Guardat", Toast.LENGTH_LONG);
                toast.show();
                onBackPressed();
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
        nombre = getIntent().getExtras().getString("nombre");
        pos++;
    }
}
