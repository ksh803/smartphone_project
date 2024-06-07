// 회원가입

package kr.ac.project.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kr.ac.project.Database.DatabaseHelper;
import kr.ac.project.R;

// SignUpActivity.java

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextEmail, editTextUsername, editTextPassword, editTextConfirmPassword;
    private Button buttonSignUp;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_id_password);

        dbHelper = DatabaseHelper.getInstance(this);

        editTextName = findViewById(R.id.EditTextName);
        editTextPhone = findViewById(R.id.EditTextPhone);
        editTextEmail = findViewById(R.id.EditTextEmail);
        editTextUsername = findViewById(R.id.EditTextUsername);
        editTextPassword = findViewById(R.id.EditTextPassword);
        editTextConfirmPassword = findViewById(R.id.EditTextConfirmPassword);
        buttonSignUp = findViewById(R.id.ButtonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (password.equals(confirmPassword)) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME, name);
            values.put(DatabaseHelper.COLUMN_PHONE, phone);
            values.put(DatabaseHelper.COLUMN_EMAIL, email);
            values.put(DatabaseHelper.COLUMN_USERNAME, username);
            values.put(DatabaseHelper.COLUMN_PASSWORD, password);

            long newRowId = db.insert(DatabaseHelper.TABLE_USER, null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                finish(); // 회원가입 후 로그인 화면으로 돌아가기
            } else {
                Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
        }
    }
}