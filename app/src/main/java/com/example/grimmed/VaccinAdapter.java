package com.example.grimmed;

import android.annotation.SuppressLint;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class VaccinAdapter extends RecyclerView.Adapter<VaccinAdapter.ViewHolder> {

    private List<String> items;
    private List<String> itemsDate;

    private VaccinTimerActivity listener;

    public VaccinAdapter(List<String> items, List<String> itemsDate, VaccinTimerActivity listener) {
        this.items = items;
        this.itemsDate = itemsDate;
        this.listener = listener;
    }

    // inner class to hold a reference to each item view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView textView;
        public TextInputLayout dateExpiLayout;
        public TextInputLayout nameVaccinLayout;
        public TextView textSetNameVaccin;
        public TextView textSetDateExpi;
        public View setBackground;

        public EditText nameVaccinEditText;
        public EditText dateVaccinEditText;
        public Boolean setVaccin = false;

        public ImageView saveVaccin;

        public ImageView deleteVacc;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(View view) {
            super(view);
            //textView = view.findViewById(R.id.textView);
            saveVaccin = view.findViewById(R.id.saveVaccin);
            dateExpiLayout = view.findViewById(R.id.dateExpi);
            nameVaccinLayout = view.findViewById(R.id.nameVaccin);
            textSetNameVaccin = view.findViewById(R.id.textSetNameVaccin);
            textSetDateExpi = view.findViewById(R.id.textSetDateExpi);
            setBackground = view.findViewById(R.id.setBackground);
            nameVaccinEditText = nameVaccinLayout.getEditText();
            dateVaccinEditText = dateExpiLayout.getEditText();
            //saveVaccin.setTextColor(R.color.checkVaccin);
            deleteVacc = view.findViewById(R.id.delete);

        }

    }

    // inflates the item views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_vaccin, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = items.get(position);
        String itemDate = itemsDate.get(position);
        int myPosition = position;


        if(!Objects.equals(item, " ") && !Objects.equals(itemDate, " ")){
            holder.textSetNameVaccin.setText(item);
            holder.textSetDateExpi.setText(itemDate);
            checkerVisibility(false, holder);

            holder.deleteVacc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        listener.deleteOnData(myPosition);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        else{
            checkerVisibility(true,holder);
            holder.saveVaccin.setOnClickListener(new View.OnClickListener(){
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {


                    try {
                        listener.saveOnData(holder.nameVaccinEditText.getText().toString(),holder.dateVaccinEditText.getText().toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }




        //Click sur la poubelle (logo poubelle visible après avoir cliqué sur le bouton Enregistrer)


    }

    private void checkerVisibility(boolean enrg, ViewHolder holder) {
        if(!enrg){
            holder.dateExpiLayout.setVisibility(View.GONE);
            holder.nameVaccinLayout.setVisibility(View.GONE);
            holder.textSetNameVaccin.setVisibility(View.VISIBLE);
            holder.textSetDateExpi.setVisibility(View.VISIBLE);
            holder.setBackground.setVisibility(View.VISIBLE);
            holder.deleteVacc.setVisibility(View.VISIBLE);
            holder.saveVaccin.setVisibility(View.GONE);

        }else{
            holder.dateExpiLayout.setVisibility(View.VISIBLE);
            holder.nameVaccinLayout.setVisibility(View.VISIBLE);
            holder.textSetNameVaccin.setVisibility(View.GONE);
            holder.textSetDateExpi.setVisibility(View.GONE);
            holder.setBackground.setVisibility(View.GONE);
            holder.deleteVacc.setVisibility(View.GONE);
            holder.saveVaccin.setVisibility(View.VISIBLE);
        }
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return items.size();
    }

}