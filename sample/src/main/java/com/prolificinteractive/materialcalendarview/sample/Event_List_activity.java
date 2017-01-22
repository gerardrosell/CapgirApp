package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Event_List_activity  extends AppCompatActivity  {

    private DatabaseReference mRootRef, mRootRead;
    private RecyclerView mRecyclerView;
    public String event, hora, nom, Assist, Desc;
    public String año;
    public String dia;
    public String mes;
    public String nombreEvento[], participants[];
    public int i, pos, pos2, posAss, posD, q;;
    public int posicion_lista;
    public long NAct;
    //public Object participants;
    //public long quantitatEvents;
    public long cont;
    private String[] descripcions, hores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);
        recogerExtras();
        participants = new String[1000];
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento").child(año).child(mes).child(dia);
        BuscarParticipants( NAct );
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nombreEvento = new String[1000];
        descripcions = new String[1000];
        hores = new String[1000];

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

                        event = ConvertirObjectToString(model);
                        event = event.substring(0,event.length()-1);
                        event = event + ",";
                        String[] sep = event.split("=");
                        int posnombre = 0;
                        int poshora = 0;
                        int posAs = 0;
                        int posDesc = 0;
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
                        }
                        pos = sep[posnombre].indexOf( "," );
                        nom = sep[posnombre].substring(0,pos);
                        pos2 = sep[poshora].indexOf(",");
                        hora = sep[poshora].substring(0,pos2);
                        posD = sep[posDesc].indexOf( "," );
                        Desc = sep[posDesc].substring( 0,posD );
                        nombreEvento[i] = nom;
                        descripcions[i] = Desc;
                        hores[i] = hora;

                        viewHolder.nombre.setText(nom);//event);
                        viewHolder.participantes.setText(participants[i]);
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
            Log.i("user", String.format("Clicked %d", getAdapterPosition()));
            activity.goEvent(getAdapterPosition());
        }
    }

    public void recogerExtras() {
        /*GUARDAMOS EN LA ACTIVIDAD LOS PARÁMETROS ENVIADOS DESDE OTRA ACTIVIDAD*/
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        dia = getIntent().getExtras().getString("dia");
        NAct = getIntent().getExtras().getLong("cont");
    }

    public String ConvertirObjectToString(Object model) {
        String Str="";
        if(model!=null){
            Str = model.toString();
        }
        return Str;
    }

    public void BuscarParticipants(long NumAct){
        for(q = 1; q<=NumAct; q++){
            mRootRead=mRootRef.child(String.valueOf(q)).child( "Assistents" );
            mRootRead.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    cont = dataSnapshot.getChildrenCount();
                    participants[q] = String.format( Locale.getDefault(), "%d", cont);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}

