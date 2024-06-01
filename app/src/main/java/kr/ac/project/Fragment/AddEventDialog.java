package kr.ac.project.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import kr.ac.project.R;

public class AddEventDialog extends DialogFragment {

    private EditText eventTitle; // 이벤트 제목 입력 필드
    private EditText eventTime; // 이벤트 시간 입력 필드
    private Button saveButton, cancelButton; // 저장 및 취소 버튼

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 다이얼로그 레이아웃을 인플레이션하여 View 객체로 반환
        View view = inflater.inflate(R.layout.dialog_add_event, container, false);

        // 뷰 초기화
        eventTitle = view.findViewById(R.id.eventTitle);
        eventTime = view.findViewById(R.id.eventTime);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        // 저장 버튼 클릭 리스너 설정
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

        // 취소 버튼 클릭 리스너 설정
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    // 이벤트 저장 메서드
    private void saveEvent() {
        String title = eventTitle.getText().toString(); // 입력된 이벤트 제목 가져오기
        String time = eventTime.getText().toString(); // 입력된 이벤트 시간 가져오기

        if (title.isEmpty() || time.isEmpty()) {
            // 제목이나 시간이 비어 있을 경우 오류 처리
            return;
        }

        Bundle args = getArguments();
        if (args != null) {
            int year = args.getInt("year");
            int month = args.getInt("month");
            int day = args.getInt("day");
            CalendarDay date = CalendarDay.from(year, month, day);

            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof CalendarFragment) {
                CalendarFragment calendarFragment = (CalendarFragment) parentFragment;
                calendarFragment.addEvent(date, title, time); // 이벤트 추가
            }
        }

        dismiss(); // 다이얼로그 닫기
    }
}
