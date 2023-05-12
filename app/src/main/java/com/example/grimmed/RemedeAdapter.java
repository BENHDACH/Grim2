package com.example.grimmed;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;


public class RemedeAdapter extends RecyclerView.Adapter<RemedeAdapter.ViewHolder> {

    private List<String> items;

    private List<Boolean> itemsShow;

    private List<JSONArray> itemsSoluce;


    private MamieActivity mamieActivity;


    public RemedeAdapter(List<String> items,List<JSONArray> itemsSoluce, List<Boolean> itemsShow, MamieActivity mamieActivity) {
        this.items = items;
        this.itemsSoluce = itemsSoluce;
        this.itemsShow = itemsShow;
        this.mamieActivity = mamieActivity;
    }

    // inner class to hold a reference to each item view
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        private List<String> items;
        public TextView textScore;
        private RecyclerView recyler;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txtSymptome);
            recyler = view.findViewById(R.id.littleRecycler);
            textScore = view.findViewById(R.id.textScore);
        }

    }

    // inflates the item views
    @Override
    public com.example.grimmed.RemedeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_remede, parent, false);
        return new com.example.grimmed.RemedeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);

        Boolean itemBool = itemsShow.get(position);
        holder.textView.setText(item);
        int myPosition = position;

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mamieActivity.onItemClick(myPosition,itemBool);
            }
        });

        if(itemBool){
            setInternRecyclerView(holder,myPosition);
        }
        else{
            RecyclerView recyclerView = holder.recyler;
            recyclerView.setVisibility(View.GONE);
        }

    }


    // total number of rows
    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setInternRecyclerView(ViewHolder holder, int myPosition){

        RecyclerView recyclerView = holder.recyler;
        recyclerView.setLayoutManager(new LinearLayoutManager(mamieActivity));

        MamieAdapter adapter = new MamieAdapter(itemsSoluce.get(myPosition));
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);

    }
}
