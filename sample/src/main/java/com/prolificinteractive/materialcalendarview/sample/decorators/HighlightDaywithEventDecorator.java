package com.prolificinteractive.materialcalendarview.sample.decorators;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Highlight Days that contains events
 */
public class HighlightDaywithEventDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();
    private final ArrayList<CalendarDay> dates;
    public HighlightDaywithEventDecorator(ArrayList<CalendarDay> datesArray) {
        dates=datesArray;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(55, Color.parseColor("#db6163")));
        //view.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#db6163")));

    }
}
