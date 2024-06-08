package kr.ac.project.Activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import kr.ac.project.Database.DatabaseHelper;
import kr.ac.project.R;

public class IDPWFindActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private Button findButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id_pw);

        // 뷰 초기화
        nameEditText = findViewById(R.id.NameFindText);
        phoneEditText = findViewById(R.id.PhoneFindText);
        findButton = findViewById(R.id.FindButton);

        // DatabaseHelper 초기화
        dbHelper = DatabaseHelper.getInstance(this);

        // 버튼 클릭 리스너 설정
        findButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(IDPWFindActivity.this, "이름과 휴대폰 번호를 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            findUser(name, phone);
        });
    }

    // 유저 찾기
    private void findUser(String name, String phone) {
        // 데이터베이스에서 사용자를 찾기 위한 쿼리
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                DatabaseHelper.COLUMN_USERNAME,
                DatabaseHelper.COLUMN_PASSWORD
        };
        String selection = DatabaseHelper.COLUMN_NAME + " = ? AND " + DatabaseHelper.COLUMN_PHONE + " = ?";
        String[] selectionArgs = {name, phone};

        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));

            // 사용자를 찾았을 경우 ID와 비밀번호를 표시
            showUserInfo(username, password);
            cursor.close();
        } else {
            // 사용자를 찾지 못했을 경우
            Toast.makeText(this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUserInfo(String username, String password) {
        new AlertDialog.Builder(this)
                .setTitle("사용자 정보")
                .setMessage("ID: " + username + "\n비밀번호: " + password)
                .setPositiveButton("확인", null)
                .show();
    }
}
