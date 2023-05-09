package com.example.grimmed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RemedeAdapter extends RecyclerView.Adapter<RemedeAdapter.ViewHolder> {

    private List<String> items;

    public RemedeAdapter() {
        this.items = items;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_remede, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setSymptomes(List<String> symptomes) {
        this.items = symptomes;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView symptomeTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            symptomeTextView = itemView.findViewById(R.id.symptomeTextView);
        }

        void bind(String item) {
            symptomeTextView.setText(item);
        }
    }
}

