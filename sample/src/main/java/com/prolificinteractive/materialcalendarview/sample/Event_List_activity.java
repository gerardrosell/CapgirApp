package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Event_List_activity  extends AppCompatActivity  {

    private DatabaseReference mRootRef;
    private RecyclerView mRecyclerView;
    public String año;
    public String dia;
    public String mes;
    public String nombreEvento[];
    public int i;
    public int posicion_lista;

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
    public void goEvent(View view) {
        Intent intent = new Intent(this, Event_activity.class);
        /*Envio de parámetros a otra actividad*/
        intent.putExtra("año", año);
        intent.putExtra("mes", mes);
        intent.putExtra("dia", dia);
        intent.putExtra("nombre", nombreEvento[posicion_lista]);
        startActivity(intent);
    }
    @Override
    protected void onStart(){
        super.onStart();
        final FirebaseRecyclerAdapter<String, MessageViewHolder> adapter =
                new FirebaseRecyclerAdapter<String, MessageViewHolder>(String.class,
                        R.layout.activity_event_list_item_activity,
                        MessageViewHolder.class,mRootRef) {
                    @Override
                    protected void populateViewHolder(MessageViewHolder viewHolder, String model, int position) {
                        nombreEvento[i] = model;// guardamos los strings de todas las posiciones para despues pasar la que se ha clicado
                        i++;// posición de cada elemento
                        posicion_lista=2;//falta saber la posicion de la lista para pasar el nombre. El 2 es para sacar algo de prueba
                        viewHolder.nombre.setText(model);
                        viewHolder.participantes.setText("3");
                    }
                };
        /*PRUEBA DE ADAPTADOR QUE LEA HASHMAP (AUN NO VA)*/
        /*final FirebaseRecyclerAdapter<HashMap<String,String>, MessageViewHolder> adapter =
                new FirebaseRecyclerAdapter<HashMap<String, String>, MessageViewHolder>(HashMap.class,
                        // android.R.layout.two_line_list_item,
                        R.layout.activity_event_list_item_activity,
                        MessageViewHolder.class,mRootRef) {

                    @Override
                    protected void populateViewHolder(MessageViewHolder viewHolder, HashMap<String, String> model, int position) {
                        Iterator iterator = model.keySet().iterator();
                        int i = 0;
                        while (iterator.hasNext()){
                            String nombreEvento = (String) iterator.next();
                            String keyMap[] = new String[0];
                            keyMap[i] = nombreEvento;
                        }
                        viewHolder.nombre.setText(model.keySet());
                    }
                };*/
        mRecyclerView.setAdapter(adapter);
        i = 0;
    }

    public static class MessageViewHolder extends  RecyclerView.ViewHolder {
        private TextView participantes;
        private TextView nombre;
        public MessageViewHolder(View v) {
            super(v);
            //v.setOnClickListener((View.OnClickListener) this);

            nombre = (TextView) v.findViewById(R.id.nomActivitat);
            participantes = (TextView) v.findViewById(R.id.participantsActivitat);
        }
    }

    public void recogerExtras() {
        /*GUARDAMOS EN LA ACTIVIDAD LOS PARÁMETROS ENVIADOS DESDE OTRA ACTIVIDAD*/
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        dia = getIntent().getExtras().getString("dia");
    }
}
