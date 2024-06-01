package kr.ac.project.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class MemoFragment extends Fragment {
    private List<Memo> memoList = new ArrayList<>();
    private MemoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_memo, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MemoAdapter(memoList);
        recyclerView.setAdapter(adapter);

        EditText editTextMemo = view.findViewById(R.id.editTextMemo);
        Button buttonAdd = view.findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(v -> {
            String memoText = editTextMemo.getText().toString();
            if (!memoText.isEmpty()) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                memoList.add(new Memo("일상", memoText, timestamp));
                adapter.notifyDataSetChanged();
                editTextMemo.setText("");
            }
        });

        return view;
    }
}
