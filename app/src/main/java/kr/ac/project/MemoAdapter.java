package kr.ac.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MyViewHolder> {
    private List<kr.ac.project.Memo> myData;
    private OnMemoListener onMemoListener;

    public MemoAdapter(List<kr.ac.project.Memo> data, OnMemoListener onMemoListener) {
        myData = data != null ? data : new ArrayList<>(); // Ensure myData is not null
        this.onMemoListener = onMemoListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view, onMemoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        kr.ac.project.Memo memo = myData.get(position);
        holder.textViewTitle.setText(memo.getTitle()); // 제목을 설정
        holder.textViewMemo.setText(memo.getMemo());
        holder.textViewTimestamp.setText(memo.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView textViewTitle; // 제목을 위한 TextView 추가
        public TextView textViewMemo;
        public TextView textViewTimestamp;
        OnMemoListener onMemoListener;

        public MyViewHolder(View view, OnMemoListener onMemoListener) {
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitle); // 제목을 위한 TextView 초기화
            textViewMemo = view.findViewById(R.id.textViewMemo);
            textViewTimestamp = view.findViewById(R.id.textViewTimestamp);
            this.onMemoListener = onMemoListener;

            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            onMemoListener.onMemoLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnMemoListener {
        void onMemoLongClick(int position);
    }
}
