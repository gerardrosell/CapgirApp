package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.sample.decorators.HighlightDaywithEventDecorator;
import com.prolificinteractive.materialcalendarview.sample.decorators.HighlightWeekendsDecorator;
import com.prolificinteractive.materialcalendarview.sample.decorators.MySelectorDecorator;
import com.prolificinteractive.materialcalendarview.sample.decorators.OneDayDecorator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BasicActivity extends AppCompatActivity
        implements OnDateSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    public static int REQUEST_NAME_CALENDARI = 1;
    private DatabaseReference mRootRef, mRootRefUsu;
    private DatabaseReference mRootRead;
    private DatabaseReference mRootRefAdmin, mRootRefSoci;
    private Button button;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private String[] data;
    public long cont;
    public Object participants;
    //private Usuario Usuari;
    private boolean admin = false;
    private String id;

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    @Bind(R.id.textView)
    TextView textView;
    private boolean DaySelected=false;
    private ArrayList<CalendarDay> dates;
    private DatabaseReference mRootIt;
    private String event;
    private boolean soci=false;
    private boolean registrat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Evento");
        mRootRefUsu = FirebaseDatabase.getInstance().getReference().child("Users");
        mRootRefAdmin = FirebaseDatabase.getInstance().getReference().child("Admin");
        mRootRefSoci = FirebaseDatabase.getInstance().getReference().child("Soci");
        id = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
        Registre(id);
        Admin(id);
        Soci(id);

        //findViewById(R.id.goEditEvent_btn).setEnabled( false );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = (Button) findViewById(R.id.button);
        //Usuari = new Usuario();


        ButterKnife.bind(this);
        widget.setOnDateChangedListener(this);


        //final int mesAct = CalendarDay.today().getMonth();
        final int anyAct = CalendarDay.today().getYear();


        //int a = mRootIt.hashCode();
        for (int mesAct = 0; mesAct < 12; mesAct++){
            mRootIt = mRootRef.child(String.valueOf(anyAct)).child(String.valueOf(mesAct));
            final int finalMesAct = mesAct;
            mRootIt.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dates = new ArrayList<CalendarDay>();
                    for (int k = 1; k < 31; k++) {
                        if (dataSnapshot.hasChild(String.valueOf(k))) {
                            dates.add(CalendarDay.from(anyAct, finalMesAct, k));
                        }
                    }
                    widget.addDecorator(new HighlightDaywithEventDecorator(dates));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }



        widget.addDecorators(
                new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(),
                //new EventDecorator(Color.RED, vector),
                oneDayDecorator
        );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setup initial text
        textView.setText(getSelectedDatesString());
        invalidateOptionsMenu();
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        Registre(id);
        Admin(id);
        Soci(id);
    }*/

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem register = menu.findItem( R.id.goEditEvent_btn );
        Admin(id);
        if(!admin){
            register.setVisible( false );
        }else{
            register.setVisible( true );
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.goEditEvent_btn) {
            if (!DaySelected){
                Toast toast = Toast.makeText(this, "No hi ha cap dia seleccionat", Toast.LENGTH_LONG);

                toast.show();
            } else {
                Intent intent = new Intent(this, Edit_event_activity.class);
                String año = data[0];
                intent.putExtra("año", año);
                String mes = data[1];
                intent.putExtra("mes", mes);
                String dia = data[2];
                intent.putExtra("dia", dia);
                intent.putExtra( "cont", cont );
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendari) {
            Intent intent = new Intent(this, BasicActivity.class);
            startActivityForResult(intent,BasicActivity.REQUEST_NAME_CALENDARI);

        }else if (id == R.id.nav_contactos) {
            Intent intent = new Intent(this, ContacteActivity.class);
            startActivityForResult(intent,ContacteActivity.REQUEST_NAME_CONTACTOS);

        } else if (id == R.id.nav_Qui_Som) {
            Intent intent = new Intent(this, QuiSomActivity.class);
            startActivityForResult(intent,QuiSomActivity.REQUEST_NAME_QUIENES_SOMOS);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        System.exit(0);
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
            intent.putExtra( "cont", cont );
            intent.putExtra("admin",admin);
            intent.putExtra("soci", soci);
            startActivity(intent);
            //if fecha esta vacía, que ponga el current date
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {

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
                cont = dataSnapshot.getChildrenCount();
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

    private void Registre(final String id){
        mRootRefUsu= FirebaseDatabase.getInstance().getReference().child("Users");
        final Intent intentReg = new Intent(this, RegistreActivity.class);
        mRootRefUsu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(id)){
                    intentReg.putExtra("id", id);
                    startActivity(intentReg);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Admin(final String id){
        mRootRefAdmin.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)) {
                    admin = true;
                    //textView.setText(String.valueOf(admin));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    private void Soci(final String id) {
        mRootRefSoci.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)) {
                    soci = true;
                    //textView.setText(String.valueOf(admin));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

    }


}
