package com.example.grimmed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private List<String> items;

    public DetailAdapter(List<String> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titre;

        public ViewHolder(View view) {
            super(view);
            titre = view.findViewById(R.id.titre);
        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_detail, parent, false);
        return new DetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    // binds the data to the TextView in each row
    public void onBindViewHolder(VaccinAdapter.ViewHolder holder, int position) {
        String item = items.get(position);
        //holder.textView.setText(item);
        //holder.textSetDateExpi.setText("");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void addOne(){
        List<String> myList = Arrays.asList("L","","","false");
        items.add(String.format(Locale.getDefault(),"%d",items.size() + 1));
    }
}


