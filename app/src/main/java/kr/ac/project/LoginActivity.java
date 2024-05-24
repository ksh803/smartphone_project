// 로그인 화면

package kr.ac.project;// LoginActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button idFindButton = findViewById(R.id.FindIdPWButton);
        Button signUpButton = findViewById(R.id.SignUpButton);

        idFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ID 찾기 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, IDPWFindActivity.class);
                startActivity(intent);
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // 간단한 로그인 버튼 기능 추가
        Button loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 성공 시 메인 액티비티로 이동 (임시)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
