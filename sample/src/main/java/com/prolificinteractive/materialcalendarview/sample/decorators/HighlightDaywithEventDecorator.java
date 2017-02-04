package com.prolificinteractive.materialcalendarview.sample.decorators;

import android.graphics.Color;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;

/**
 * Highlight Saturdays and Sundays with a background
 */
public class HighlightDaywithEventDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();
    private DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference().child("Evento");
    private boolean resultat;
    private int dia;
    private int any;
    private int mes;
    //private final Drawable highlightDrawable;
    //private static final int color = Color.parseColor("#DDDDDD");

    //public HighlightWeekendsDecorator() {highlightDrawable = new ColorDrawable(color);}

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        any=day.getYear();
        mes=day.getMonth();
        dia=day.getDay();
        mRootRef.child(String.valueOf(any)).child(String.valueOf(mes)).child(String.valueOf(dia)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    resultat=true;
                } else{
                    resultat=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*int weekDay = calendar.get(Calendar.DAY_OF_WEEK);*/
        return resultat;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, Color.RED));
    }
}
