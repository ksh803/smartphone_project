// 메인
package kr.ac.project;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                switch (itemId) {
                    case 1: // Fragment File
                        selectedFragment = new FileFragment();
                        break;
                    case 2: // Fragment Map
                        selectedFragment = new MapFragment();
                        break;
                    case 3: // Fragment Home
                        selectedFragment = new HomeFragment();
                        break;
                    case 4: // Fragment Memo
                        selectedFragment = new MemoFragment();
                        break;
                    case 5: // Fragment Calendar
                        selectedFragment = new CalendarFragment();
                        break;
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, selectedFragment).commit();
                }
                return true;
            }
        });

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(3); // Fragment Home
        }
    }
}
