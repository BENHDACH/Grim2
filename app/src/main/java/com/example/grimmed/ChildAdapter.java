package com.example.grimmed;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder>{

    private List<String> items;

    public ChildAdapter(List<String> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextInputLayout nameChildEditText;
        public TextInputLayout nameChildLayout;
        public ImageView vaccinChild;
        public ImageView allChild;
        public ImageView usualChild;
        public ViewHolder(View view) {

            super(view);
            nameChildEditText = view.findViewById(R.id.nameChild);
            nameChildLayout = view.findViewById(R.id.nameChild);
            vaccinChild = view.findViewById(R.id.vaccinChild);
            allChild = view.findViewById(R.id.allChild);
            usualChild = view.findViewById(R.id.usualChild);
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