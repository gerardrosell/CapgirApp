package com.prolificinteractive.materialcalendarview.sample;

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

public class Event_List_activity  extends AppCompatActivity  {

    private DatabaseReference mRootRef;
    private RecyclerView mRecyclerView;
    public String año;
    public String dia;
    public String mes;

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
    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerAdapter<String, MessageViewHolder> adapter =
                new FirebaseRecyclerAdapter<String, MessageViewHolder>(String.class,
                        android.R.layout.two_line_list_item,
                        MessageViewHolder.class,mRootRef) {
                    @Override
                    protected void populateViewHolder(MessageViewHolder viewHolder, String model, int position) {
                        viewHolder.mText.setText(model);
                    }
                };
        mRecyclerView.setAdapter(adapter);
    }

    public static class MessageViewHolder extends  RecyclerView.ViewHolder {
        TextView mText;
        public MessageViewHolder(View v) {
            super(v);
            mText = (TextView) v.findViewById(android.R.id.text1);
        }
    }
    public void recogerExtras() {
        año = getIntent().getExtras().getString("año");
        mes = getIntent().getExtras().getString("mes");
        dia = getIntent().getExtras().getString("dia");
    }
}
