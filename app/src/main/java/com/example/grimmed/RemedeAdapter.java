package com.example.grimmed;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RemedeAdapter extends RecyclerView.Adapter<RemedeAdapter.ViewHolder> {

    private List<String> items;

    private List<Boolean> itemsShow;

    private List<JSONArray> itemsSoluce;

    private List<Integer> itemsScore;

    private MamieActivity mamieActivity;

    List<List<Object>> combinedList;


    public RemedeAdapter(List<String> items, List<JSONArray> itemsSoluce, List<Boolean> itemsShow, MamieActivity mamieActivity,
                         List<Integer> itemsScore, List<List<Object>> combinedList) {
        this.items = items;
        this.itemsSoluce = itemsSoluce;
        this.itemsShow = itemsShow;
        this.mamieActivity = mamieActivity;
        this.itemsScore = itemsScore;
        this.combinedList = combinedList;
    }

    // inner class to hold a reference to each item view
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        private List<String> items;
        public TextView textScore;
        private RecyclerView recyler;

        public ImageView plusScore;
        public ImageView moinScore;


        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txtSymptome);
            recyler = view.findViewById(R.id.littleRecycler);
            textScore = view.findViewById(R.id.textScore);
            plusScore = view.findViewById(R.id.plusScore);
            moinScore = view.findViewById(R.id.moinScore);
        }

    }

    // inflates the item views
    @Override
    public com.example.grimmed.RemedeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_remede, parent, false);
        return new com.example.grimmed.RemedeAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //String item = items.get(position);
        String item = (String) combinedList.get(position).get(0);

        //Boolean itemBool = itemsShow.get(position);
        Boolean itemBool = (Boolean) combinedList.get(position).get(3);

        String score = combinedList.get(position).get(1).toString();

        Boolean hasVoted = (Boolean) combinedList.get(position).get(4);

        int isVote = (int) combinedList.get(position).get(5);

        Log.e("combined",""+combinedList.get(position));

        holder.textView.setText(item);

        int myPosition = position;

        //Si l'utilisateur à voté dans cette recette
        if(hasVoted){
            //Si il a voté upvote (pouce vert, flèche haut vert/bleu)
            if(isVote==1){
                int colorRes = ContextCompat.getColor(mamieActivity, R.color.teal_200);
                holder.plusScore.setColorFilter(colorRes);
            }
            //Si il a voté downvote (pouce rouge, flèche bas rouge)
            else{
                int colorES = ContextCompat.getColor(mamieActivity, R.color.purple_200);
                holder.moinScore.setColorFilter(colorES);
            }
        }



        //itemsScore.get(position).toString() //Inside the setText()
        holder.textScore.setText(score);

        //Faire le dérouler en cliquant sur la textView
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mamieActivity.onItemClick(myPosition,itemBool);
            }
        });

        //Faire le renouveau du score en cliquant
        holder.plusScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mamieActivity.onScoreChange(myPosition,1);
            }
        });
        holder.moinScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mamieActivity.onScoreChange(myPosition,-1);
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
        return combinedList.size();
        //return items.size();
    }

    private void setInternRecyclerView(ViewHolder holder, int myPosition){

        RecyclerView recyclerView = holder.recyler;
        recyclerView.setLayoutManager(new LinearLayoutManager(mamieActivity));

        //itemsSoluce.get(myPosition) //Inside mamieAdapte(..)
        MamieAdapter adapter = new MamieAdapter((JSONArray) combinedList.get(myPosition).get(2));
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);

    }
}


