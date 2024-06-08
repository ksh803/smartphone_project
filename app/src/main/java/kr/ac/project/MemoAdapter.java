package kr.ac.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.project.Activity.MemoActivity;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    private List<MemoActivity> memoList;
    private OnMemoListener onMemoListener;

    public MemoAdapter(List<MemoActivity> memoList, OnMemoListener onMemoListener) {
        this.memoList = memoList;
        this.onMemoListener = onMemoListener;
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MemoViewHolder(view, onMemoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        MemoActivity memo = memoList.get(position);
        holder.textViewTitle.setText(memo.getTitle());
        holder.textViewMemo.setText(memo.getText()); // 수정된 부분
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public class MemoViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView textViewTitle;
        TextView textViewMemo;
        OnMemoListener onMemoListener;

        public MemoViewHolder(@NonNull View itemView, OnMemoListener onMemoListener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewMemo = itemView.findViewById(R.id.textViewMemo);
            this.onMemoListener = onMemoListener;

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (onMemoListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onMemoListener.onMemoLongClick(position);
                }
            }
            return true;
        }
    }

    public interface OnMemoListener {
        void onMemoLongClick(int position);
    }
}
