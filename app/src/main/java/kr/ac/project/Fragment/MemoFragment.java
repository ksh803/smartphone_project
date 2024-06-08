package kr.ac.project.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.project.Database.DatabaseHelper;
import kr.ac.project.MemoAdapter;
import kr.ac.project.Activity.MemoActivity;
import kr.ac.project.R;

public class MemoFragment extends Fragment {

    private RecyclerView recyclerView;
    private MemoAdapter adapter;
    private List<MemoActivity> memoList;
    private DatabaseHelper dbHelper;

    private EditText editTextTitle;
    private EditText editTextMemo;
    private Button buttonAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_memo, container, false);

        // 데이터베이스 초기화
        dbHelper = DatabaseHelper.getInstance(getContext());

        // 뷰 초기화
        recyclerView = view.findViewById(R.id.recyclerView);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextMemo = view.findViewById(R.id.editTextMemo);
        buttonAdd = view.findViewById(R.id.buttonAdd);

        // 리사이클러뷰 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        memoList = new ArrayList<>();
        adapter = new MemoAdapter(memoList, new MemoAdapter.OnMemoListener() {
            @Override
            public void onMemoLongClick(int position) {
                // 롱클릭 시 수정/삭제 기능 추가
                showMemoOptionsDialog(position);
            }
        });
        recyclerView.setAdapter(adapter);

        // 버튼 클릭 리스너 설정
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMemo();
            }
        });

        // 저장된 메모 불러오기
        loadMemos();

        return view;
    }

    // 메모 추가 메서드
    private void addMemo() {
        String title = editTextTitle.getText().toString().trim();
        String text = editTextMemo.getText().toString().trim();

        if (!title.isEmpty() && !text.isEmpty()) {
            long id = dbHelper.insertMemo(title, text);
            MemoActivity memo = new MemoActivity(id, title, text, getCurrentTimestamp());
            memoList.add(memo);
            adapter.notifyItemInserted(memoList.size() - 1);

            editTextTitle.setText("");
            editTextMemo.setText("");
        }
    }

    // 저장된 메모 불러오는 메서드
    private void loadMemos() {
        memoList.clear();
        memoList.addAll(dbHelper.getAllMemos());
        adapter.notifyDataSetChanged();
    }

    // 현재 타임스탬프 가져오기
    private long getCurrentTimestamp() {  // Changed return type to long
        return System.currentTimeMillis();  // Directly returning the long value
    }

    // 메모 수정/삭제 옵션 다이얼로그 표시
    private void showMemoOptionsDialog(int position) {
        MemoActivity memo = memoList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("메모 수정/삭제")
                .setItems(new String[]{"수정", "삭제"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            // 메모 수정
                            showEditMemoDialog(memo, position);
                            break;
                        case 1:
                            // 메모 삭제
                            dbHelper.deleteMemo(memo.getId());
                            memoList.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(getContext(), "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                })
                .show();
    }

    // 메모 수정 다이얼로그 표시
    private void showEditMemoDialog(MemoActivity memo, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_memo, null);
        builder.setView(dialogView);

        EditText editTextTitle = dialogView.findViewById(R.id.editTextEditTitle);
        EditText editTextMemo = dialogView.findViewById(R.id.editTextEditMemo);

        editTextTitle.setText(memo.getTitle());
        editTextMemo.setText(memo.getText());

        builder.setTitle("메모 수정")
                .setPositiveButton("저장", (dialog, which) -> {
                    String newTitle = editTextTitle.getText().toString().trim();
                    String newText = editTextMemo.getText().toString().trim();

                    if (!newTitle.isEmpty() && !newText.isEmpty()) {
                        memo.setTitle(newTitle);
                        memo.setText(newText);
                        dbHelper.updateMemo(memo);
                        memoList.set(position, memo);
                        adapter.notifyItemChanged(position);
                        Toast.makeText(getContext(), "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }
}
