package com.example.grimmed;

import static android.support.v4.content.ContextCompat.startActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder>{

    private List<String> items;
    private ChildActivity childActivity;

    public ChildAdapter(List<String> items, ChildActivity childActivity) {

        this.items = items;
        this.childActivity = childActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText nameChildEditText;
        public TextInputLayout nameChildLayout;
        public ImageView vaccinChild;
        public ImageView allChild;
        public ImageView usualChild;

        public ImageView enrgChild;
        public Boolean setVaccin = false;
        public Boolean setAll = false;
        public Boolean setUsual = false;

        public ViewHolder(View view) {

            super(view);
            nameChildEditText = view.findViewById(R.id.textNameChild);
            nameChildLayout = view.findViewById(R.id.nameChild);
            vaccinChild = view.findViewById(R.id.vaccinChild);
            allChild = view.findViewById(R.id.allChild);
            usualChild = view.findViewById(R.id.usualChild);
            enrgChild = view.findViewById(R.id.saveE);
        }
        }



    @Override
    public ChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_child, parent, false);
        return new ChildAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row


    // binds the data to the TextView in each row-
    public void onBindViewHolder(ChildAdapter.ViewHolder holder, int position) {
        String item = items.get(position);
       /** holder.allChild.set;
        holder.usualChild.setImageIcon();
        holder.vaccinChild.setImageIcon();**/
        EditText nameChildLayout = holder.nameChildEditText;

        if(!Objects.equals(item, " ")){
            nameChildLayout.setText(item);
            checkerVisibility(false, holder);
        }else{
            checkerVisibility(true, holder);
        }

        holder.enrgChild.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    childActivity.saveOnData(nameChildLayout.getText().toString());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });



        holder.vaccinChild.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                childActivity.changeActivity(1,item);
            }
        });
        holder.usualChild.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                childActivity.changeActivity(2,item);
            }
        });
        holder.allChild.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                childActivity.changeActivity(3,item);
            }
        });



    }

    private void checkerVisibility(boolean enrg, ViewHolder holder) {

        if(!enrg){
            holder.allChild.setVisibility(View.VISIBLE);
            holder.usualChild.setVisibility(View.VISIBLE);
            holder.vaccinChild.setVisibility(View.VISIBLE);
        }else{
            holder.allChild.setVisibility(View.GONE);
            holder.usualChild.setVisibility(View.GONE);
            holder.vaccinChild.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    void addOne(){
        items.add(String.format(Locale.getDefault(),"%d",items.size() + 1));
    }



    }
