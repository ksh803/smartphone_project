package kr.ac.project.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kr.ac.project.Database.DatabaseHelper;
import kr.ac.project.Event;
import kr.ac.project.EventAdapter;
import kr.ac.project.EventDecorator;
import kr.ac.project.R;

public class CalendarFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private CalendarDay selectedDate;
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventsForSelectedDate = new ArrayList<>();
    private TextView noEventsTextView;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_calendar, container, false);

        dbHelper = DatabaseHelper.getInstance(getContext());

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = date;
                updateEventsForSelectedDate();
            }
        });

        noEventsTextView = view.findViewById(R.id.noEventsTextView);

        eventRecyclerView = view.findViewById(R.id.recyclerView);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventAdapter = new EventAdapter(eventsForSelectedDate);
        eventRecyclerView.setAdapter(eventAdapter);

        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            openAddEventDialog();
        });

        // Initialize decorators with existing events
        Set<Event> allEvents = dbHelper.getAllEvents();
        if (!allEvents.isEmpty()) {
            calendarView.addDecorator(new EventDecorator(allEvents));
        }

        return view;
    }

    private void openAddEventDialog() {
        AddEventDialog dialog = new AddEventDialog();
        Bundle args = new Bundle();
        if (selectedDate != null) {
            args.putInt("year", selectedDate.getYear());
            args.putInt("month", selectedDate.getMonth());
            args.putInt("day", selectedDate.getDay());
        }
        dialog.setArguments(args);
        dialog.setTargetFragment(this, 0);
        dialog.show(getParentFragmentManager(), "AddEventDialog");
    }

    public void addEvent(CalendarDay date, String title, String time) {
        dbHelper.insertEvent(title, time, date);
        updateEventsForSelectedDate();
        calendarView.addDecorator(new EventDecorator(new HashSet<>(dbHelper.getAllEvents())));
    }

    private void updateEventsForSelectedDate() {
        eventsForSelectedDate.clear();
        eventsForSelectedDate.addAll(dbHelper.getEventsForDate(selectedDate));
        eventAdapter.notifyDataSetChanged();

        if (eventsForSelectedDate.isEmpty()) {
            noEventsTextView.setVisibility(View.VISIBLE);
        } else {
            noEventsTextView.setVisibility(View.GONE);
        }
    }
}
