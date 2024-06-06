package kr.ac.project.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.project.R;

public class FileFragment extends Fragment {

    // 상수 정의
    private static final int REQUEST_CODE_PICK_PDF = 1; // PDF 파일 선택을 위한 요청 코드
    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 2; // 읽기 권한 요청 코드

    // 멤버 변수 정의
    private ListView pdfListView; // PDF 파일 목록을 표시하는 ListView
    private ArrayList<String> pdfFileNames; // PDF 파일 이름을 저장하는 ArrayList
    private ArrayAdapter<String> pdfAdapter; // ListView를 위한 ArrayAdapter
    private HashMap<String, Uri> pdfFileUris; // 파일 이름과 Uri를 매핑하는 HashMap

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment의 레이아웃 파일을 인플레이션하여 View 객체로 반환
        View rootView = inflater.inflate(R.layout.layout_file, container, false);

        // 각 ImageButton 찾기
        ImageButton githubButton = rootView.findViewById(R.id.GithubBtn);
        ImageButton linkedInButton = rootView.findViewById(R.id.LinkedInBtn);
        ImageButton rocketPuncherButton = rootView.findViewById(R.id.RocketPuncherBtn);
        ImageButton saraminButton = rootView.findViewById(R.id.SaraminBtn);
        ImageButton wantedButton = rootView.findViewById(R.id.WantedBtn);

        // 각 ImageButton에 대한 OnClickListener 설정
        githubButton.setOnClickListener(v -> openWebsite("https://github.com"));
        linkedInButton.setOnClickListener(v -> openWebsite("https://www.linkedin.com"));
        rocketPuncherButton.setOnClickListener(v -> openWebsite("https://www.rocketpunch.com"));
        saraminButton.setOnClickListener(v -> openWebsite("https://www.saramin.co.kr"));
        wantedButton.setOnClickListener(v -> openWebsite("https://www.wanted.co.kr"));

        // ListView와 ArrayAdapter 초기화
        pdfListView = rootView.findViewById(R.id.PdfListView);
        pdfFileNames = new ArrayList<>();
        pdfFileUris = new HashMap<>(); // 파일 이름과 Uri를 저장
        pdfAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, pdfFileNames);
        pdfListView.setAdapter(pdfAdapter);

        // 파일 추가 버튼 설정
        Button uploadButton = rootView.findViewById(R.id.UploadButton);
        uploadButton.setOnClickListener(v -> {
            // 권한 확인
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
            } else {
                // 파일 선택기 열기
                openFilePicker();
            }
        });

        // ListView 항목 클릭 시 PDF 열기
        pdfListView.setOnItemClickListener((parent, view, position, id) -> {
            String fileName = pdfFileNames.get(position);
            Uri fileUri = pdfFileUris.get(fileName);
            openPdf(fileUri);
        });

        // ListView 항목 길게 클릭 시 삭제
        pdfListView.setOnItemLongClickListener((parent, view, position, id) -> {
            String fileName = pdfFileNames.get(position);
            new AlertDialog.Builder(getContext())
                    .setTitle("삭제 확인")
                    .setMessage(fileName + " 파일을 삭제하시겠습니까?")
                    .setPositiveButton("삭제", (dialog, which) -> {
                        pdfFileNames.remove(position);
                        pdfFileUris.remove(fileName);
                        pdfAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("취소", null)
                    .show();
            return true;
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

    // 파일 선택기 열기
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, REQUEST_CODE_PICK_PDF);
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                Toast.makeText(getContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 파일 선택 결과 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_PDF && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String fileName = getFileName(uri);
                pdfFileNames.add(fileName);
                pdfFileUris.put(fileName, uri);
                pdfAdapter.notifyDataSetChanged();
            }
        }
    }

    // 파일 이름 가져오기
    private String getFileName(Uri uri) {
        String displayName = null;
        if (uri != null) {
            ContentResolver contentResolver = getContext().getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) {
                        displayName = cursor.getString(index);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return displayName;
    }

    // PDF 파일 열기
    private void openPdf(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "PDF 파일을 열 수 있는 애플리케이션이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
