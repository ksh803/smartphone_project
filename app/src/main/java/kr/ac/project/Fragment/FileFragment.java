package kr.ac.project.Fragment;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import kr.ac.project.R;

public class FileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment의 레이아웃 파일을 인플레이션하여 View 객체로 반환
        View rootView = inflater.inflate(R.layout.layout_file, container, false);

        // 각 ImageButton 찾기
        ImageButton githubButton = rootView.findViewById(R.id.GithubBtn);
        ImageButton linkedInButton = rootView.findViewById(R.id.LinkedinBtn);
        ImageButton rocketPuncherButton = rootView.findViewById(R.id.RocketPuncherBtn);
        ImageButton saraminButton = rootView.findViewById(R.id.SaraminBtn);
        ImageButton wantedButton = rootView.findViewById(R.id.WantedBtn);

        // 각 ImageButton에 대한 OnClickListener 설정
        githubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite("https://github.com");
            }
        });

        linkedInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite("https://www.linkedin.com");
            }
        });

        rocketPuncherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite("https://www.rocketpunch.com");
            }
        });

        saraminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite("https://www.saramin.co.kr");
            }
        });

        wantedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite("https://www.wanted.co.kr");
            }
        });

        return rootView;
    }

    // 웹 사이트 열기
    private void openWebsite(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}