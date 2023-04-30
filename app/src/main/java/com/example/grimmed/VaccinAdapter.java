package com.example.grimmed;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class VaccinAdapter extends RecyclerView.Adapter<VaccinAdapter.ViewHolder> {

    private List<String> items;

    public VaccinAdapter(List<String> items) {
        this.items = items;
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
        //holder.textView.setText(item);
        //holder.textSetDateExpi.setText("");


        holder.saveVaccin.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                holder.setVaccin = !holder.setVaccin;
                //Click sur bouton Enregistrer
                if(holder.setVaccin){
                    //holder.checkVaccin.setTextColor(Color.RED);
                    holder.dateExpiLayout.setVisibility(View.GONE);
                    holder.nameVaccinLayout.setVisibility(View.GONE);
                    holder.textSetNameVaccin.setVisibility(View.VISIBLE);
                    holder.textSetDateExpi.setVisibility(View.VISIBLE);
                    holder.setBackground.setVisibility(View.VISIBLE);
                    holder.deleteVacc.setVisibility(View.VISIBLE);

                    // On récup la valeur venue des Input Layout dans une variable
                    String vaccineName = holder.nameVaccinEditText.getText().toString();
                    String dateE = holder.dateVaccinEditText.getText().toString();

                    // On set le text avec ces valeurs
                    holder.textSetNameVaccin.setText(vaccineName);
                    holder.textSetDateExpi.setText(dateE);

                }else{ //Pas de click sur bouton Enregistrer
                    holder.setBackground.setBackgroundColor(R.color.checkVaccin);
                    holder.dateExpiLayout.setVisibility(View.VISIBLE);
                    holder.nameVaccinLayout.setVisibility(View.VISIBLE);
                    holder.textSetNameVaccin.setVisibility(View.GONE);
                    holder.textSetDateExpi.setVisibility(View.GONE);
                    holder.setBackground.setVisibility(View.GONE);
                    // donc il faudra supprimer de la list cette elm
                    //items.remove(item);
                }
            }
        });

        //Click sur la poubelle (logo poubelle visible après avoir cliqué sur le bouton Enregistrer)
        holder.deleteVacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.dateExpiLayout.setVisibility(View.GONE);
                holder.nameVaccinLayout.setVisibility(View.GONE);
                holder.textSetNameVaccin.setVisibility(View.GONE);
                holder.textSetDateExpi.setVisibility(View.GONE);
                holder.setBackground.setVisibility(View.GONE);
                holder.saveVaccin.setVisibility(View.GONE);
                holder.deleteVacc.setVisibility(View.GONE);
            }
        });

    }



    // total number of rows
    @Override
    public int getItemCount() {
        return items.size();
    }

    void addOne(){
        List<String> myList = Arrays.asList("L","","","false");
        items.add(String.format(Locale.getDefault(),"%d",items.size() + 1));
    }
}