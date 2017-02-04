package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BusParticipantsListActivity extends AppCompatActivity {
    public String año, dia, mes,nombreEvento;
    private DatabaseReference mRootRef;
    public int pos;
    private RecyclerView mRecyclerView;
    private String mes2;
    private String llista="Apuntats al BUS:\n";
    private TextView titol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        recogerExtras();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento").child(año).child(mes).child(dia).child(String.valueOf(pos)).child("Va En Bus");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        titol=(TextView) findViewById(R.id.titol);
    }
    @Override
    protected void onStart(){
        titol.setText(R.string.Bus_Participants);

        super.onStart();
        FirebaseRecyclerAdapter<String, MessageViewHolder> adapter =
            new FirebaseRecyclerAdapter<String, MessageViewHolder>(String.class,
            R.layout.activity_participants_item_list,
            MessageViewHolder.class,mRootRef) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, String model, int position) {
                viewHolder.participant.setText(model);
                llista+=model+"\n";
            }
        };
     mRecyclerView.setAdapter(adapter);
    }

    public static class MessageViewHolder extends  RecyclerView.ViewHolder {
        private TextView participant;
        public MessageViewHolder (android.view.View view){
            super(view);
            participant = (TextView) view.findViewById(R.id.nomParticipant);
        }
    }
    public void recogerExtras() {
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        mes2=mes;
        mes=String.valueOf(Integer.parseInt(mes)-1);
        dia = getIntent().getExtras().getString("dia");
        nombreEvento = getIntent().getExtras().getString("nombre");
        pos = getIntent().getExtras().getInt("pos");
        pos++;
    }

    public void send_list(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + "capgiratapp@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Llista d'apuntats al BUS - "+nombreEvento+" - "+dia+"/"+mes2+"/"+año);
        emailIntent.putExtra(Intent.EXTRA_TEXT, llista);
        startActivity(emailIntent);
    }
}



