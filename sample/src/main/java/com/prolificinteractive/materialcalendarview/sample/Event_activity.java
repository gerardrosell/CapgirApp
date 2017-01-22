package com.prolificinteractive.materialcalendarview.sample;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
        //TODO: arreglar la memoria de checks

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
                if(dataSnapshot.hasChild(id)){
                    Va_en_bus.setChecked(true);
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

        fecha = dia+"/"+mes+"/"+año;
        data.setText(fecha);
        nom_event.setText(nombreEvento);//utilizamos el nombre leido en event_list_activity
        hora.setText(Hora);
        descrip.setText(desc);
        }
    public void recogerExtras() {
        desc =getIntent().getExtras().getString("desc");
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        mes = String.valueOf(Integer.parseInt(mes)+1);
        dia = getIntent().getExtras().getString("dia");
        nombreEvento = getIntent().getExtras().getString("nombre");//recogemos el nombre del evento para no volverlo a leer de firebase
        pos = getIntent().getExtras().getInt("pos");
        Hora = getIntent().getExtras().getString("hora");
        admin = getIntent().getExtras().getBoolean("admin");
    }

    public void ChkSi(android.view.View view){
        No_assisteix.setChecked(false);
        Si_assisteix.setChecked(true);
        Va_en_bus.setEnabled(true);
        No_assisteix.setEnabled(true);
        No_assisteix.setChecked(false);
    }
    public void ChkNo(android.view.View view){
        No_assisteix.setChecked(true);
        Si_assisteix.setChecked(false);
        Si_assisteix.setEnabled(true);
        Va_en_bus.setEnabled(false);
        Va_en_bus.setChecked(false);
    }

    public void Bus(android.view.View view){
        No_assisteix.setChecked(false);
        Si_assisteix.setChecked(true);
        Si_assisteix.setEnabled(true);
        No_assisteix.setEnabled(true);

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
                    if (Bus.equals( "True" )){
                        mRootRef.child(posi).child("Va En Bus").child(id).setValue(nombre);
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
            intent.putExtra("mes", mes);
            intent.putExtra("dia", dia);
            intent.putExtra("nombre",nombreEvento);
            intent.putExtra("pos",pos);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /*private void Admin(final String id){
        mRootRefAdmin.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)) {
                    admin = true;
                    //textView.setText(String.valueOf(admin));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }*/
    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/
}
