package kr.ac.project.Fragment;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import kr.ac.project.Database.DatabaseHelper;
import kr.ac.project.R;

public class HomeFragment extends Fragment {

    private TextView homeIDTextView, homeNumberTextView, homeEmailTextView;
    private EditText nameEditText, ageEditText, majorEditText, uniEditText;
    private Button editButton, saveButton;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 레이아웃을 인플레이트합니다.
        View view = inflater.inflate(R.layout.layout_home, container, false);

        // 뷰 초기화
        initializeViews(view);

        // 데이터베이스와 SharedPreferences 초기화
        initializeDatabase();

        // 사용자 정보 표시
        displayUserInfo();

        // 초기에는 편집 모드를 비활성화
        setEditMode(false);

        // 수정 버튼 클릭 리스너 설정
        editButton.setOnClickListener(v -> setEditMode(true));
        saveButton.setOnClickListener(v -> {
            saveUserInfo();
            setEditMode(false);
        });

        return view;
    }

    // 뷰를 초기화하는 메서드
    private void initializeViews(View view) {
        homeIDTextView = view.findViewById(R.id.HomeID);
        homeNumberTextView = view.findViewById(R.id.HomeNumber);
        homeEmailTextView = view.findViewById(R.id.HomeEmail);
        nameEditText = view.findViewById(R.id.Name_Information);
        ageEditText = view.findViewById(R.id.Age_Information);
        majorEditText = view.findViewById(R.id.Major_Information);
        uniEditText = view.findViewById(R.id.Uni_Information);
        editButton = view.findViewById(R.id.Home_EditBtn);
        saveButton = view.findViewById(R.id.Home_SaveBtn);
    }

    // 데이터베이스와 SharedPreferences를 초기화하는 메서드
    private void initializeDatabase() {
        preferences = getContext().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        dbHelper = DatabaseHelper.getInstance(getContext());
        db = dbHelper.getWritableDatabase();
    }

    // 사용자 정보를 표시하는 메서드
    private void displayUserInfo() {
        // SharedPreferences에서 사용자 정보를 가져옴
        String userName = preferences.getString("name", "");
        String userID = preferences.getString("username", "");
        String userPhone = preferences.getString("phone", "");
        String userEmail = preferences.getString("email", "");
        String userAge = preferences.getString("age", "");
        String userMajor = preferences.getString("major", "");
        String userUniversity = preferences.getString("university", "");

        // 가져온 정보를 뷰에 설정
        homeIDTextView.setText(userID);
        nameEditText.setText(userName);
        homeNumberTextView.setText(userPhone);
        homeEmailTextView.setText(userEmail);
        ageEditText.setText(userAge);
        majorEditText.setText(userMajor);
        uniEditText.setText(userUniversity);
    }

    // 편집 모드를 설정하는 메서드
    private void setEditMode(boolean enabled) {
        // 편집 모드에 따라 입력 필드를 활성화 또는 비활성화
        nameEditText.setEnabled(enabled);
        ageEditText.setEnabled(enabled);
        majorEditText.setEnabled(enabled);
        uniEditText.setEnabled(enabled);
        editButton.setVisibility(enabled ? View.GONE : View.VISIBLE);
        saveButton.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    // 사용자 정보를 SharedPreferences에 저장하는 메서드
    private void saveUserInfo() {
        // 입력 필드에서 사용자 입력 값을 가져옴
        String id = homeIDTextView.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String phone = homeNumberTextView.getText().toString().trim();
        String email = homeEmailTextView.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();
        String major = majorEditText.getText().toString().trim();
        String university = uniEditText.getText().toString().trim();

        // SharedPreferences에 저장
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("username", id);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("age", age);
        editor.putString("major", major);
        editor.putString("university", university);
        editor.apply();

        // ContentValues를 생성하여 데이터베이스를 업데이트
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_PHONE, phone);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);

        // 새로운 사용자 정보로 데이터베이스를 업데이트
        db.update(DatabaseHelper.TABLE_USER, values, DatabaseHelper.COLUMN_USERNAME + " = ?", new String[]{id});
    }
}
