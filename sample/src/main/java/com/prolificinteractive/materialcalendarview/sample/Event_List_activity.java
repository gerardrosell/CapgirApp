package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class Event_List_activity  extends AppCompatActivity  {

    private DatabaseReference mRootRef, mRootRead;
    private RecyclerView mRecyclerView;
    public String event, hora, nom, Assist, Desc;
    public String año;
    public String dia;
    public String mes;
    public String nombreEvento[], participants[];
    public int i, pos, pos2, posAss, posD, q, r;
    public int posicion_lista;
    public int participen = 0;
    public long NAct;
    public boolean admin;
    public boolean busnec[];
    //public Object participants;
    //public long quantitatEvents;
    public long cont;
    private String[] descripcions, hores;
    private boolean soci;
    private boolean llistatancada[];
    private String id;
    private DatabaseReference mRootRefUsu;
    private String[] Usuaris_vinculats, Usuaris_vinculats_aux;
    private boolean multipleusuari=false;
    private String nomUsuari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);
        recogerExtras();
        participants = new String[1000];
        Usuaris_vinculats_aux = new String[100];
        mRootRefUsu= FirebaseDatabase.getInstance().getReference().child("Users");
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento").child(año).child(mes).child(dia);
        id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        mRootRefUsu.child(id).child("nombre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nomUsuari = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRootRefUsu.child(id).child("listUsu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int posAs;
                    for (posAs = 0; posAs < dataSnapshot.getChildrenCount(); posAs++) {
                        Usuaris_vinculats_aux[posAs] = dataSnapshot.child(String.valueOf(posAs)).getValue().toString();
                        multipleusuari = true;

                    }
                    Usuaris_vinculats_aux[posAs] = nomUsuari;
                }
                Usuaris_vinculats = Arrays.copyOf(Usuaris_vinculats_aux, (int) dataSnapshot.getChildrenCount()+1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //BuscarParticipants( NAct );
        nombreEvento = new String[1000];
        descripcions = new String[1000];
        busnec = new boolean[1000];
        hores = new String[1000];
        llistatancada = new boolean[1000];

        posicion_lista = 0;
    }

    public void goEvent(int pos) {
        Intent intent = new Intent(this, Event_activity.class);
        /*Envio de parámetros a otra actividad*/
        intent.putExtra("año", año);
        intent.putExtra("mes", mes);
        intent.putExtra("dia", dia);
        intent.putExtra("nombre", nombreEvento[pos]);
        intent.putExtra("pos",pos);
        intent.putExtra("hora",hores[pos]);
        intent.putExtra("desc", descripcions[pos]);
        intent.putExtra("admin", admin);
        intent.putExtra("soci", soci);
        intent.putExtra("busnecessari", busnec[pos]);
        intent.putExtra("llistatancada", llistatancada[pos]);
        intent.putExtra("Usu_vinc", Usuaris_vinculats);
        intent.putExtra("multUsu", multipleusuari);
        intent.putExtra("nomUsuari", nomUsuari);
        startActivity(intent);
    }

    @Override
    protected void onStart(){
        super.onStart();
        i=1;
        final FirebaseRecyclerAdapter<Object, MessageViewHolder> adapter =
                new FirebaseRecyclerAdapter<Object, MessageViewHolder>(Object.class,
                        R.layout.activity_event_list_item_activity,
                        MessageViewHolder.class,mRootRef) {
                    @Override
                    protected void populateViewHolder(MessageViewHolder viewHolder, Object model, int position) {

                        participen=0;
                        event = ConvertirObjectToString(model);
                        event = event.substring(0,event.length()-1);
                        event = event + ",";
                        String[] sep = event.split("=");
                        int posnombre = 0;
                        int poshora = 0;
                        int posAs;
                        int posDesc = 0;
                        int posBus = 0;
                        int posLlT = 0;

                        for(int j = 0; j< sep.length; j++){
                            if (sep[j].contains("Name")){
                                posnombre = j+1;
                            }
                            if (sep[j].contains("hour")){
                                poshora = j+1;
                            }
                            if (sep[j].contains("Description")){
                                posDesc = j+1;
                            }
                            if (sep[j].contains("Assistents")){
                                for(posAs=j+1; !sep[posAs].contains("Description") && !sep[posAs].contains("busnecessari") && !sep[posAs].contains("Name") && !sep[posAs].contains("hour") && !sep[posAs].contains("Va En Bus") && !sep[posAs].contains("No_Assist") && posAs<sep.length-1 ;posAs++){
                                    participen++;
                                }
                            }
                            if (sep[j].contains("busnecessari")){
                                posBus = j+1;
                            }
                            if (sep[j].contains("llistatancada")){
                                posLlT = j+1;
                            }
                        }
                        pos = sep[posnombre].indexOf( "," );
                        nom = sep[posnombre].substring(0,pos);
                        pos2 = sep[poshora].indexOf(",");
                        hora = sep[poshora].substring(0,pos2);
                        posD = sep[posDesc].indexOf( "," );
                        Desc = sep[posDesc].substring( 0,posD );
                        busnec[i] = sep[posBus].contains("true");
                        llistatancada[i] = sep[posLlT].contains("true");

                        nombreEvento[i] = nom;
                        descripcions[i] = Desc;
                        hores[i] = hora;


                        viewHolder.nombre.setText(nom);//event);
                        viewHolder.participantes.setText(String.valueOf(participen));
                        viewHolder.activity = Event_List_activity.this;
                        i++;

                    }


                };
        mRecyclerView.setAdapter(adapter);
        i = 0;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView participantes;
        private TextView nombre;
        public Event_List_activity activity;

        public MessageViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nomActivitat);
            participantes = (TextView) v.findViewById(R.id.participantsActivitat);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Log.i("user", String.format("Clicked %d", getAdapterPosition()));
            activity.goEvent(getAdapterPosition());
        }
    }

    public void recogerExtras() {
        /*GUARDAMOS EN LA ACTIVIDAD LOS PARÁMETROS ENVIADOS DESDE OTRA ACTIVIDAD*/
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        dia = getIntent().getExtras().getString("dia");
        NAct = getIntent().getExtras().getLong("cont");
        admin = getIntent().getExtras().getBoolean("admin");
        soci = getIntent().getExtras().getBoolean("soci");
    }

    public String ConvertirObjectToString(Object model) {
        String Str="";
        if(model!=null){
            Str = model.toString();
        }
        return Str;
    }


}

