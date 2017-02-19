package com.prolificinteractive.materialcalendarview.sample;


import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RemoteViews;
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
    private boolean busnecessari;
    private Button btn_acompanyants;
    private boolean soci;
    private boolean llistatancada;
    private String[] Usuaris_vinculats;
    private boolean multipleusuari;
    private TextView Assist_text;
    private String nomUsuariSeleccionat;
    private String id_write;

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);
        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        recogerExtras();
        remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.notif_icon,R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.notif_title,nombreEvento+"  "+dia+"/"+mes2+"/"+año+"  "+Hora);
        mRootRefUsu= FirebaseDatabase.getInstance().getReference().child("Users");
        id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        /*mRootRefUsu.child(id).child("nombre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombre=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
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
        Assist_text = (TextView) findViewById(R.id.Assist_text);
        btn_acompanyants.setVisibility(View.INVISIBLE);
        if(!busnecessari){
            TextView bus_text = (TextView) findViewById(R.id.bus_text);
            bus_text.setVisibility(View.INVISIBLE);
            Va_en_bus.setVisibility(View.INVISIBLE);
        }
        if(!soci || llistatancada){
            Button Apuntat = (Button) findViewById(R.id.apuntat);
            Apuntat.setEnabled(false);
            TextView Assist_text = (TextView) findViewById(R.id.Assist_text);
            Assist_text.setVisibility(View.INVISIBLE);
            Si_assisteix.setVisibility(View.INVISIBLE);
            No_assisteix.setVisibility(View.INVISIBLE);
            TextView bus_text = (TextView) findViewById(R.id.bus_text);
            bus_text.setVisibility(View.INVISIBLE);
            Va_en_bus.setVisibility(View.INVISIBLE);
        }

        id_write = id;
        if(multipleusuari){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.escull_usuari);
            builder.setItems(Usuaris_vinculats, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on colors[which]
                    if(Usuaris_vinculats[which].length()<=6){
                        Assist_text.setText(Usuaris_vinculats[which]);
                    } else{
                        Assist_text.setText(Usuaris_vinculats[which].substring(0,5)+".");
                    }
                    nomUsuariSeleccionat = Usuaris_vinculats[which];
                    id_write+=nomUsuariSeleccionat;
                    comprovació_checks();

                }
            });
            builder.setCancelable(false);
            builder.show();
        } else{
            nomUsuariSeleccionat = nombre;
            id_write+=nomUsuariSeleccionat;
            comprovació_checks();
        }

    }

    private void comprovació_checks() {
        //comprovació existència
        mRootRef.child(String.valueOf(pos+1)).child("Assistents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id_write)){
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
                if(dataSnapshot.hasChild(id_write)){
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
                if(dataSnapshot.hasChild(id_write) && !llistatancada){
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
        Usuaris_vinculats = getIntent().getExtras().getStringArray("Usu_vinc");
        multipleusuari = getIntent().getExtras().getBoolean("multUsu");
        nombre = getIntent().getExtras().getString("nomUsuari");
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
                    mRootRef.child(posi).child("Assistents").child(id_write).removeValue();
                    mRootRef.child(posi).child("Va En Bus").child(id_write).removeValue();
                    mRootRef.child(posi).child("No_Assist").child(id_write).setValue(nomUsuariSeleccionat);
                    if(nomUsuariSeleccionat.equals(nombre)){
                        mRootRef.child(posi).child("Va En Bus").child(id_write+"Acompanyants").removeValue();
                    }
                }else if (Si_ass.equals( "True" )){
                    mRootRef.child(posi).child("Assistents").child(id_write).setValue(nomUsuariSeleccionat);
                    mRootRef.child(posi).child("No_Assist").child(id_write).removeValue();
                    //comprova_apuntat();
                    if (Bus.equals( "True" )){
                        mRootRef.child(posi).child("Va En Bus").child(id_write).setValue(nomUsuariSeleccionat);
                    }
                    else if(Bus.equals( "False" )){
                        mRootRef.child(posi).child("Va En Bus").child(id_write).removeValue();
                        if(nomUsuariSeleccionat.equals(nombre)){
                            mRootRef.child(posi).child("Va En Bus").child(id_write+"Acompanyants").removeValue();
                        }
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

        if (id == R.id.envia_notificacio) {
            notification_id = (int) System.currentTimeMillis();

            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentTitle("CAPGIRAPP")
                    .setCustomBigContentView(remoteViews);

            notificationManager.notify(notification_id,builder.build());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
