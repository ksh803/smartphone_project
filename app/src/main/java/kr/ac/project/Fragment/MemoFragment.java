package kr.ac.project.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import kr.ac.project.Memo;
import kr.ac.project.MemoAdapter;
import kr.ac.project.R;

public class MemoFragment extends Fragment implements MemoAdapter.OnMemoListener {
    private List<Memo> memoList;
    private MemoAdapter adapter;
    private EditText editTextTitle;
    private EditText editTextMemo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_memo, container, false);

        memoList = new ArrayList<>(); // Initialize memoList

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MemoAdapter(memoList, this); // MemoFragment를 OnMemoListener로 전달
        recyclerView.setAdapter(adapter);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextMemo = view.findViewById(R.id.editTextMemo);
        Button buttonAdd = view.findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(v -> {
            String memoText = editTextMemo.getText().toString();
            String memoTitle = editTextTitle.getText().toString();
            if (!memoText.isEmpty() && !memoTitle.isEmpty()) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                memoList.add(new Memo(memoTitle, memoText, timestamp));
                adapter.notifyDataSetChanged();
                editTextTitle.setText("");
                editTextMemo.setText("");
            } else {
                Toast.makeText(getContext(), "제목과 메모를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onMemoLongClick(int position) {
        showEditDeleteDialog(position);
    }

    private void showEditDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("메모 수정/삭제");
        builder.setMessage("메모를 수정하거나 삭제하시겠습니까?");
        builder.setPositiveButton("수정", (dialog, which) -> showEditDialog(position));
        builder.setNegativeButton("삭제", (dialog, which) -> {
            memoList.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        });
        builder.setNeutralButton("취소", null);
        builder.show();
    }

    private void showEditDialog(int position) {
        Memo memo = memoList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("메모 수정");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_memo, (ViewGroup) getView(), false);
        final EditText inputTitle = viewInflated.findViewById(R.id.editTextTitle);
        final EditText inputMemo = viewInflated.findViewById(R.id.editTextMemo);
        inputTitle.setText(memo.getTitle());
        inputMemo.setText(memo.getMemo());

        builder.setView(viewInflated);
        builder.setPositiveButton("저장", (dialog, which) -> {
            String updatedMemoTitle = inputTitle.getText().toString();
            String updatedMemoText = inputMemo.getText().toString();
            if (!updatedMemoText.isEmpty() && !updatedMemoTitle.isEmpty()) {
                memoList.set(position, new Memo(updatedMemoTitle, updatedMemoText, memo.getTimestamp()));
                adapter.notifyItemChanged(position);
                Toast.makeText(getContext(), "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "제목과 메모를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();
    }
}
