package com.prolificinteractive.materialcalendarview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.sample.decorators.HighlightWeekendsDecorator;
import com.prolificinteractive.materialcalendarview.sample.decorators.MySelectorDecorator;
import com.prolificinteractive.materialcalendarview.sample.decorators.OneDayDecorator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BasicActivity extends AppCompatActivity implements OnDateSelectedListener {

    private DatabaseReference mRootRef;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    @Bind(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Firebase.setAndroidContext(this);
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

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        String text = getSelectedDatesString();
        textView.setText(text);
        if(!text.isEmpty()){
            mRootRef.child(text).setValue(text);
        }
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
}
