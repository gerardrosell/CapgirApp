package com.prolificinteractive.materialcalendarview.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParticipantsListActivity extends AppCompatActivity {
    public String año, dia, mes,nombreEvento;
    private DatabaseReference mRootRef;
    public int pos;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_list);
        recogerExtras();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento").child(año).child(mes).child(dia).child(String.valueOf(pos)).child("Assistents");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onStart(){

        super.onStart();
        FirebaseRecyclerAdapter<String, MessageViewHolder> adapter =
            new FirebaseRecyclerAdapter<String, MessageViewHolder>(String.class,
            R.layout.activity_participants_item_list,
            MessageViewHolder.class,mRootRef) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, String model, int position) {
                viewHolder.participant.setText(model);
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
        mes=String.valueOf(Integer.parseInt(mes)-1);
        dia = getIntent().getExtras().getString("dia");
        nombreEvento = getIntent().getExtras().getString("nombre");
        pos = getIntent().getExtras().getInt("pos");
        pos++;
    }
}



