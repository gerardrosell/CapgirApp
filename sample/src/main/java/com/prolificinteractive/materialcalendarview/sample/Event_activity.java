package com.prolificinteractive.materialcalendarview.sample;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
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
    private TextView data, hora, nom_event, descrip;
    public String nombreEvento, nombre;
    public CheckBox Si_assisteix, No_assisteix, Va_en_bus;
    public int pos;
    private String desc;
    //private DatabaseReference mRootRefAdmin;
    private boolean admin ;//= false;
    public String No_ass, Si_ass, Bus, posi;
    private String mes2;
    private boolean busnecessari, apuntatBus;
    private Button btn_acompanyants;
    private boolean soci;
    private boolean llistatancada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);
        recogerExtras();
        mRootRefUsu= FirebaseDatabase.getInstance().getReference().child("Users");
        id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        mRootRefUsu.child(id).child("nombre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombre=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento").child(año).child(mes).child(dia);
        //mRootRefAdmin= FirebaseDatabase.getInstance().getReference().child("Admin");
        data = (TextView)findViewById(R.id.Data);
        hora = (TextView)findViewById(R.id.hora);
        nom_event = (TextView)findViewById(R.id.nom_event);
        descrip = (TextView)findViewById(R.id.Descripcio);
        Si_assisteix = (CheckBox)findViewById(R.id.Si);
        No_assisteix = (CheckBox)findViewById(R.id.no);
        Va_en_bus = (CheckBox) findViewById(R.id.bus_si);
        btn_acompanyants = (Button) findViewById(R.id.btn_acompanyants);
        btn_acompanyants.setVisibility(View.INVISIBLE);
        if(!busnecessari){
            TextView bus_text = (TextView) findViewById(R.id.bus_text);
            bus_text.setVisibility(View.INVISIBLE);
            Va_en_bus.setVisibility(View.INVISIBLE);
        }
        if(!soci || llistatancada){
            TextView Assist_text = (TextView) findViewById(R.id.Assist_text);
            Assist_text.setVisibility(View.INVISIBLE);
            Si_assisteix.setVisibility(View.INVISIBLE);
            No_assisteix.setVisibility(View.INVISIBLE);
            TextView bus_text = (TextView) findViewById(R.id.bus_text);
            bus_text.setVisibility(View.INVISIBLE);
            Va_en_bus.setVisibility(View.INVISIBLE);
        }

        mRootRef.child(String.valueOf(pos+1)).child("Assistents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)){
                    Si_assisteix.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRootRef.child(String.valueOf(pos+1)).child("No_Assist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)){
                    No_assisteix.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRootRef.child(String.valueOf(pos+1)).child("Va En Bus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id) && !llistatancada){
                    Va_en_bus.setChecked(true);
                    btn_acompanyants.setVisibility(View.VISIBLE);

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

        fecha = dia+"/"+mes2+"/"+año;
        data.setText(fecha);
        nom_event.setText(nombreEvento);//utilizamos el nombre leido en event_list_activity
        hora.setText(Hora);
        descrip.setText(desc);
        apuntatBus=false;
        mRootRef.child(String.valueOf(pos+1)).child("Va En Bus").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                apuntatBus=dataSnapshot.exists();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }
    public void recogerExtras() {
        desc =getIntent().getExtras().getString("desc");
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        mes2 = String.valueOf(Integer.parseInt(mes)+1);
        dia = getIntent().getExtras().getString("dia");
        nombreEvento = getIntent().getExtras().getString("nombre");//recogemos el nombre del evento para no volverlo a leer de firebase
        pos = getIntent().getExtras().getInt("pos");
        Hora = getIntent().getExtras().getString("hora");
        admin = getIntent().getExtras().getBoolean("admin");
        soci = getIntent().getExtras().getBoolean("soci");
        busnecessari = getIntent().getExtras().getBoolean("busnecessari");
        llistatancada = getIntent().getExtras().getBoolean("llistatancada");
    }

    public void num_acompanyants(android.view.View view){
        ck(view);
        Intent intent = new Intent(this, Num_acompanyants.class);
        /*Envio de parámetros a otra actividad*/
        intent.putExtra("año", año);
        intent.putExtra("mes", mes);
        intent.putExtra("dia", dia);
        intent.putExtra("pos",pos);
        intent.putExtra("nombre", nombre);
        startActivity(intent);
    }

    public void ChkSi(android.view.View view){
        No_assisteix.setChecked(false);
        Si_assisteix.setChecked(true);
        Va_en_bus.setEnabled(true);
        No_assisteix.setEnabled(true);
        No_assisteix.setChecked(false);
        if(Va_en_bus.isChecked()){
            btn_acompanyants.setVisibility(View.VISIBLE);
        } else {
            btn_acompanyants.setVisibility(View.INVISIBLE);
        }
    }
    public void ChkNo(android.view.View view){
        No_assisteix.setChecked(true);
        Si_assisteix.setChecked(false);
        Si_assisteix.setEnabled(true);
        Va_en_bus.setEnabled(false);
        Va_en_bus.setChecked(false);
        if(Va_en_bus.isChecked()){
            btn_acompanyants.setVisibility(View.VISIBLE);
        } else {
            btn_acompanyants.setVisibility(View.INVISIBLE);
        }
    }

    public void Bus(android.view.View view){
        No_assisteix.setChecked(false);
        Si_assisteix.setChecked(true);
        Si_assisteix.setEnabled(true);
        No_assisteix.setEnabled(true);
        if(Va_en_bus.isChecked()){
            btn_acompanyants.setVisibility(View.VISIBLE);
        } else {
            btn_acompanyants.setVisibility(View.INVISIBLE);
        }

    }

    public void ck(android.view.View view){
        mRootRefUsu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                No_ass = (No_assisteix.isChecked() ? "True" : "False");
                Si_ass = (Si_assisteix.isChecked() ? "True" : "False");
                Bus = (Va_en_bus.isChecked() ? "True" : "False");
                posi = String.valueOf(pos+1);

                if(No_ass.equals( "True" )){
                    mRootRef.child(posi).child("Assistents").child(id).removeValue();
                    mRootRef.child(posi).child("Va En Bus").child(id).removeValue();
                    mRootRef.child(posi).child("No_Assist").child(id).setValue(nombre);
                }else if (Si_ass.equals( "True" )){
                    mRootRef.child(posi).child("Assistents").child(id).setValue(nombre);
                    mRootRef.child(posi).child("No_Assist").child(id).removeValue();
                    if (Bus.equals( "True" ) && !apuntatBus){
                        mRootRef.child(posi).child("Va En Bus").child(id).setValue(nombre+" - 0");
                    }
                    else if(Bus.equals( "False" )){
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


    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem register = menu.findItem( R.id.goParticipants_btn );
        //Admin(id);
        if(!admin){
            register.setVisible( false );
        }else{
            register.setVisible( true );
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_participants, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.goParticipants_btn) {
            Intent intent = new Intent(this, ParticipantsListActivity.class);
            intent.putExtra("año", año);
            intent.putExtra("mes", String.valueOf(Integer.parseInt(mes)+1));
            intent.putExtra("dia", dia);
            intent.putExtra("nombre",nombreEvento);
            intent.putExtra("pos",pos);
            startActivity(intent);
            return true;
        }
        if (id == R.id.goNoParticipants_btn) {
            Intent intent = new Intent(this, NoParticipantsListActivity.class);
            intent.putExtra("año", año);
            intent.putExtra("mes", String.valueOf(Integer.parseInt(mes)+1));
            intent.putExtra("dia", dia);
            intent.putExtra("nombre",nombreEvento);
            intent.putExtra("pos",pos);
            startActivity(intent);
            return true;
        }

        if (id == R.id.goBus_btn) {
            if(busnecessari){Intent intent = new Intent(this, BusParticipantsListActivity.class);
            intent.putExtra("año", año);
            intent.putExtra("mes", String.valueOf(Integer.parseInt(mes)+1));
            intent.putExtra("dia", dia);
            intent.putExtra("nombre",nombreEvento);
            intent.putExtra("pos",pos);
            startActivity(intent);}
            else{
                Toast toast = Toast.makeText(this, "Aquesta activitat no té llista de BUS", Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        }

        if (id == R.id.tancallista_btn) {
            mRootRef.child(String.valueOf(pos+1)).child("llistatancada").setValue("true");
            return true;
        }

        if (id == R.id.obrellista_btn) {
            mRootRef.child(String.valueOf(pos+1)).child("llistatancada").setValue("false");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
