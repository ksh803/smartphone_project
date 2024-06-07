package kr.ac.project;

import android.graphics.Color;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;
import java.util.Set;

public class EventDecorator implements DayViewDecorator {

    private final Set<CalendarDay> dates;

    public EventDecorator(Set<Event> events) {
        this.dates = new HashSet<>();
        for (Event event : events) {
            dates.add(event.getDate());
        }
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, Color.GREEN)); // 원하는 색상과 크기로 변경 가능
    }
}
