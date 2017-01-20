package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Event_List_activity  extends AppCompatActivity  {

    private DatabaseReference mRootRef;
    private RecyclerView mRecyclerView;
    public String event, sep, hora, nom, descrip;
    public String año;
    public String dia;
    public String mes;
    public String nombreEvento[];
    public int i, pos, poss, pos2;
    public int posicion_lista;
    public static int REQUEST_NAME_LLISTA_EVENTS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_activity);
        Firebase.setAndroidContext(this);
        recogerExtras();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento").child(año).child(mes).child(dia);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nombreEvento = new String[10000];
        i = 0;
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
        intent.putExtra("hora",hora);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Inici:
                Intent intent_inici = new Intent(this, BasicActivity.class);
                startActivity(intent_inici);
                return true;
            case R.id.Qui_som:
                Intent intent_quisom = new Intent(this, QuiSomActivity.class);
                startActivity(intent_quisom);
                return true;
            case R.id.Contacte:
                Intent intent_contacte = new Intent(this, ContacteActivity.class);
                startActivity(intent_contacte);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                        String[] sep = event.split(",");
                        int posnombre = 0;
                        int poshora = 0;
                        for(int j = 0; j< sep.length; j++){
                            if (sep[j].contains("Name")){
                                posnombre = j;
                            }
                            if (sep[j].contains("hour")){
                                poshora = j;
                            }
                        }
                        pos = sep[posnombre].indexOf( "=" );
                        //poss = sep[posnombre].indexOf( "}" );
                        nom = sep[posnombre].substring( pos+1);
                        pos2 = sep[poshora].indexOf("=");
                        hora = sep[poshora].substring(pos2+1);
                        nombreEvento[i] = nom;

                        viewHolder.nombre.setText(nom);//event);
                        viewHolder.participantes.setText("3");
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
    }

    public String ConvertirObjectToString(Object model) {
        String Str="";
        if(model!=null){
            Str = model.toString();
        }
        return Str;
    }

}

