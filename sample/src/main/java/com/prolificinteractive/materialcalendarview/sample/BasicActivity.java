package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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

public class BasicActivity extends AppCompatActivity
        implements OnDateSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    public static int REQUEST_NAME_CALENDARI = 1;
    private DatabaseReference mRootRef;
    private DatabaseReference mRootRead;
    private Button button;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private String[] data;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view) {
            Intent intent = new Intent(this, BasicActivity.class);
            startActivityForResult(intent,BasicActivity.REQUEST_NAME_CALENDARI);

        }else if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, ContacteActivity.class);
            startActivityForResult(intent,ContacteActivity.REQUEST_NAME_CONTACTOS);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, QuiSomActivity.class);
            startActivityForResult(intent,QuiSomActivity.REQUEST_NAME_QUIENES_SOMOS);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void goEditEvent() {
        Intent intent = new Intent(this, Edit_event_activity.class);
        startActivity(intent);
    }

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    @Bind(R.id.textView)
    TextView textView;
    private boolean DaySelected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = (Button)findViewById(R.id.button);

        mRootRef= FirebaseDatabase.getInstance().getReference().child("Evento");
        ButterKnife.bind(this);
        widget.setOnDateChangedListener(this);

        widget.addDecorators(
                new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setup initial text
        textView.setText(getSelectedDatesString());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void goEvent(View view) {
        Intent intent = new Intent(this, Event_activity.class);
        String año = data[0];
        intent.putExtra("año", año);
        String mes = data[1];
        intent.putExtra("mes", mes);
        String dia = data[2];
        intent.putExtra("dia", dia);
        startActivity(intent);
    }

    public void goListEvent(View view) {

        if (!DaySelected){
            Toast toast = Toast.makeText(this, "No hi ha cap dia seleccionat", Toast.LENGTH_LONG);

            toast.show();
        }
        else {
            Intent intent = new Intent(this, Event_List_activity.class);
            String año = data[0];
            intent.putExtra("año", año);
            String mes = data[1];
            intent.putExtra("mes", mes);
            String dia = data[2];
            intent.putExtra("dia", dia);
            startActivity(intent);
            //if fecha esta vacía, que ponga el current date
        }
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
        DaySelected=true;
        String d="";
        if(date!=null) {
            d = date.toString();
        }
        d = d.substring(12,d.length()-1);
        data = d.split("-");
        //pinta el nom del primer event a la barra inferior
        mRootRead=mRootRef.child(data[0]).child(data[1]).child(data[2]).child("1").child("Name");
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
