package com.example.grimmed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;


public class RemedeAdapter extends RecyclerView.Adapter<RemedeAdapter.ViewHolder> {

    private List<String> symptomes;
    private Map<String, List<String>> traitementsMap;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String symptome, List<String> traitements);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_remede, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String symptome = symptomes.get(position);
        List<String> traitements = traitementsMap.get(symptome);

        holder.bind(symptome);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(symptome, traitements);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return symptomes != null ? symptomes.size() : 0;
    }

    public void setSymptomes(List<String> symptomes) {
        this.symptomes = symptomes;
        notifyDataSetChanged();
    }

    public void setTraitements(Map<String, List<String>> traitementsMap) {
        this.traitementsMap = traitementsMap;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView symptomeTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            symptomeTextView = itemView.findViewById(R.id.txtSymptome);
        }

        void bind(String symptome) {
            symptomeTextView.setText(symptome);
        }
    }
}
