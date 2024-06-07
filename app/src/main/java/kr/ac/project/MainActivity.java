package kr.ac.project;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

import kr.ac.project.Fragment.CalendarFragment;
import kr.ac.project.Fragment.FileFragment;
import kr.ac.project.Fragment.HomeFragment;
import kr.ac.project.Fragment.MapFragment;
import kr.ac.project.Fragment.MemoFragment;

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
                if (itemId == R.id.fragment_file) {
                    selectedFragment = new FileFragment();
                } else if (itemId == R.id.fragment_map) {
                    selectedFragment = new MapFragment();
                } else if (itemId == R.id.fragment_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.fragment_memo) {
                    selectedFragment = new MemoFragment();
                } else if (itemId == R.id.fragment_calendar) {
                    selectedFragment = new CalendarFragment();
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, selectedFragment).commit();
                }
                return true;
            }
        });

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.fragment_home); // Fragment Home
        }
    }
}