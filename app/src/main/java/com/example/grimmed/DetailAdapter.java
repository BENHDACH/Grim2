package com.example.grimmed;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private List<String> items;

    private DetailActivity listener;

    public DetailAdapter(List<String> items,DetailActivity listener) {

        this.items = items;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText nameMédocEditText;
        public TextInputLayout nameMedocLayout;
        public ImageView saveMedoc;

        public ImageView deleteMedoc;

        public Boolean setDetail = false;

        public ViewHolder(View view) {
            super(view);
            nameMédocEditText = view.findViewById(R.id.textNameMédoc);
            nameMedocLayout = view.findViewById(R.id.nameMédoc);
            saveMedoc = view.findViewById(R.id.saveM);
            deleteMedoc = view.findViewById(R.id.deleteM);

        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_detail, parent, false);
        return new DetailAdapter.ViewHolder(view);
    }

    //@Override
   // public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

  //  }

    // binds the data to the TextView in each row
    public void onBindViewHolder(DetailAdapter.ViewHolder holder, int position) {
        String item = items.get(position);
        int myPosition = position;
        //holder.textView.setText(item);
        //holder.textSetDateExpi.setText("");
        EditText nameMedocLayout = holder.nameMédocEditText;

        if(item!=" "){
            checkerVisibility(false, holder);
            nameMedocLayout.setText(item);
            holder.deleteMedoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listener.deleteOnData(myPosition);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                }
            });
        }
        else{
            Log.e("Cool2",""+item);
            checkerVisibility(true, holder);
            holder.saveMedoc.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        listener.saveOnData(nameMedocLayout.getText().toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void checkerVisibility(Boolean enrg, ViewHolder holder){
        if(!enrg){
            holder.saveMedoc.setVisibility(View.GONE);
            holder.deleteMedoc.setVisibility(View.VISIBLE);
        }else{
            holder.deleteMedoc.setVisibility(View.GONE);
            holder.saveMedoc.setVisibility(View.VISIBLE);
        }

    }

}


