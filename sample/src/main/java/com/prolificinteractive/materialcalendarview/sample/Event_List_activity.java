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
    public int i, pos;
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
        //i = 0;
        posicion_lista = 0;
    }

    public void goEvent(int pos) {
        Intent intent = new Intent(this, Event_activity.class);
        /*Envio de parámetros a otra actividad*/
        intent.putExtra("año", año);
        intent.putExtra("mes", mes);
        intent.putExtra("dia", dia);
        intent.putExtra("nombre", nombreEvento[pos]);
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
                        //String name = model.getClass().getName();
                        /*mRootRef.child(String.valueOf(i)).child("Name").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                event = dataSnapshot.getValue(String.class);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/
                        //posicion_lista=2;//falta saber la posicion de la lista para pasar el nombre. El 2 es para sacar algo de prueba

                        //TODO: estic aquí // He afegit una funcio a baix que et separa el objecte amb 3 strings.
                        //TODO: en el cas del nom, el retallo perque surti bé, però igual es una mica gitano
                        //TODO: la manera de fer-ho. Si en sabeu alguna altra, ja direu

                        event = ConvertirObjectToString(model);
                        String[] sep = event.split(",");
                        pos = sep[1].indexOf( "=" );
                        nom = sep[1].substring( pos+1 );

                        viewHolder.nombre.setText(nom);//event);
                        viewHolder.participantes.setText("3");
                        viewHolder.activity = Event_List_activity.this;
                        i++;
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
        //i = 0;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView participantes;
        private TextView nombre;
        public Event_List_activity activity;

        public MessageViewHolder(View v) {
            super(v);
            //v.setOnClickListener((View.OnClickListener) this);
            nombre = (TextView) v.findViewById(R.id.nomActivitat);
            participantes = (TextView) v.findViewById(R.id.participantsActivitat);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i("pauek", String.format("Clicked %d", getAdapterPosition()));
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

