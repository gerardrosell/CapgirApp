package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.sample.decorators.HighlightWeekendsDecorator;
import com.prolificinteractive.materialcalendarview.sample.decorators.MySelectorDecorator;
import com.prolificinteractive.materialcalendarview.sample.decorators.OneDayDecorator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BasicActivity extends AppCompatActivity implements OnDateSelectedListener {

    private DatabaseReference mRootRef;
    private DatabaseReference mRootRead;
    private Button button;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private String[] data;

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    @Bind(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Firebase.setAndroidContext(this);

        button = (Button)findViewById(R.id.button);

        mRootRef= FirebaseDatabase.getInstance().getReference().child("condition");
        ButterKnife.bind(this);
        widget.setOnDateChangedListener(this);

        widget.addDecorators(
                new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        //Setup initial text
        textView.setText(getSelectedDatesString());
    }

    public void goEvent(View view) {
        Intent intent = new Intent(this, Event_activity.class);
        startActivity(intent);
    }
    public void goListEvent(View view) {
        Intent intent = new Intent(this, Event_List_activity.class);
        if (data[0].isEmpty()||data[1].isEmpty()||data[2].isEmpty()){
            data[0] = "1999";
            data[1] = "1";
            data[2] = "1";
            // despues se ha de arreglar para que la fecha sea la del día actual.
        }
        String año = data[0];
        intent.putExtra("año",año);
        String mes = data[1];
        intent.putExtra("mes",mes);
        String dia = data[2];
        intent.putExtra("dia",dia);
        startActivity(intent);
        //if fecha esta vacía, que ponga el current date
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        /*
        //Escriu a firebase el dia seleccionat en el format data 00/00/0000
        String text = getSelectedDatesString();
        textView.setText(text);
        if(!text.isEmpty()){
            mRootRef.child(text).setValue(text);
        }*/
        String d="";
        if(date!=null) {
            d = date.toString();
        }
        d = d.substring(12,d.length()-1);
        data = d.split("-");
        //pinta el nom del primer event a la barra inferior
        mRootRead=mRootRef.child(data[0]).child(data[1]).child(data[2]).child("1");
        mRootRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String event = dataSnapshot.getValue(String.class);
                textView.setText(event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //pinta +Nº del número d'events que hi ha aquella data
        mRootRef.child(data[0]).child(data[1]).child(data[2]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long cont = dataSnapshot.getChildrenCount();
                String a = "+"+String.format(Locale.getDefault(), "%d", cont);
                button.setText(a);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "";
        }
        return FORMATTER.format(date.getDate());
    }
}
