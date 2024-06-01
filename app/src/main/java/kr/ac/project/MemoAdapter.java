package kr.ac.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MyViewHolder> {
    private List<Memo> myData;

    public MemoAdapter(List<Memo> data) {
        myData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Memo memo = myData.get(position);
        holder.textViewCategory.setText(memo.getCategory());
        holder.textViewMemo.setText(memo.getMemo());
        holder.textViewTimestamp.setText(memo.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCategory;
        public TextView textViewMemo;
        public TextView textViewTimestamp;

        public MyViewHolder(View view) {
            super(view);
            textViewCategory = view.findViewById(R.id.textViewCategory);
            textViewMemo = view.findViewById(R.id.textViewMemo);
            textViewTimestamp = view.findViewById(R.id.textViewTimestamp);
        }
    }
}
