package kr.ac.project;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class Event {
    private CalendarDay date;
    private String title;
    private String time;

    public Event(CalendarDay date, String title, String time) {
        this.date = date;
        this.title = title;
        this.time = time;
    }

    public CalendarDay getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return date != null ? date.equals(event.date) : event.date == null;
    }

    @Override
    public int hashCode() {
        return date != null ? date.hashCode() : 0;
    }
}