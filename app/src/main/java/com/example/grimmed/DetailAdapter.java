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
        Log.e("HHH",""+items);

        if(item!=""){
            nameMedocLayout.setText(item);
            Log.e("MOOO","HEIN 2 3 LoL");
            holder.saveMedoc.setOnClickListener(new View.OnClickListener() {
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
            holder.saveMedoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listener.saveOnData(item);
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

    void addOne(){
        List<String> listAT = Arrays.asList("L","","","false");
        items.add(String.format(Locale.getDefault(),"%d",items.size() + 1));
    }
}


