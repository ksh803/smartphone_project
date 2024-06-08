package kr.ac.project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import kr.ac.project.Database.DatabaseHelper;
import kr.ac.project.MainActivity;
import kr.ac.project.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonSignUp, buttonFindIdPW;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // DatabaseHelper 초기화
        dbHelper = DatabaseHelper.getInstance(this);

        // 뷰 초기화
        editTextUsername = findViewById(R.id.Username);
        editTextPassword = findViewById(R.id.Password);
        buttonLogin = findViewById(R.id.LoginButton);
        buttonSignUp = findViewById(R.id.SignUpButton);
        buttonFindIdPW = findViewById(R.id.FindIdPWButton);

        // 로그인 버튼 클릭 리스너 설정
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // 회원가입 버튼 클릭 리스너 설정
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // ID/PW 찾기 버튼 클릭 리스너 설정
        buttonFindIdPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ID/PW 찾기 기능, IDPWFindActivity로 들어감
                Intent intent = new Intent(LoginActivity.this, IDPWFindActivity.class);
                startActivity(intent);
            }
        });
    }

    // 로그인 메서드
    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // 아이디와 비밀번호가 입력되지 않았을 경우
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 데이터베이스 읽기 모드로 열기
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_EMAIL, DatabaseHelper.COLUMN_PHONE, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_PASSWORD};
        String selection = DatabaseHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        // 사용자 정보 조회
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String dbPassword = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));
            if (password.equals(dbPassword)) {
                // 로그인 성공
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String userEmail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
                String userPhone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE));
                cursor.close();

                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();

                // 로그인 성공 후 MainActivity로 이동
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                // SharedPreferences에 사용자 정보 저장
                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("name", name);
                editor.putString("username", username);
                editor.putString("email", userEmail);
                editor.putString("phone", userPhone);
                editor.apply();

                startActivity(intent);
                finish();
            } else {
                // 비밀번호가 틀린 경우
                Toast.makeText(this, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        } else {
            // 아이디가 틀린 경우
            Toast.makeText(this, "아이디가 틀렸습니다", Toast.LENGTH_SHORT).show();
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
