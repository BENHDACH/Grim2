package com.example.grimmed;

import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class VaccinAdapter extends RecyclerView.Adapter<VaccinAdapter.ViewHolder> {

    private List<String> items;

    public VaccinAdapter(List<String> items) {
        this.items = items;
    }

    // inner class to hold a reference to each item view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextInputLayout dateExpiLayout;
        public TextInputLayout nameVaccinLayout;
        public TextView textSetNameVaccin;
        public TextView textSetDateExpi;
        public View setBackground;

        public EditText nameVaccinEditText;
        public EditText dateVaccinEditText;
        public Boolean setVaccin = false;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            dateExpiLayout = view.findViewById(R.id.dateExpi);
            nameVaccinLayout = view.findViewById(R.id.nameVaccin);
            textSetNameVaccin = view.findViewById(R.id.textSetNameVaccin);
            textSetDateExpi = view.findViewById(R.id.textSetDateExpi);
            setBackground = view.findViewById(R.id.setBackground);
            nameVaccinEditText = nameVaccinLayout.getEditText();
            dateVaccinEditText = dateExpiLayout.getEditText();
            textView.setTextColor(Color.BLUE);

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
        holder.textView.setText(item);
        //holder.textSetDateExpi.setText("");


        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.setVaccin = !holder.setVaccin;
                if(holder.setVaccin){
                    holder.textView.setTextColor(Color.RED);
                    holder.dateExpiLayout.setVisibility(View.GONE);
                    holder.nameVaccinLayout.setVisibility(View.GONE);
                    holder.textSetNameVaccin.setVisibility(View.VISIBLE);
                    holder.textSetDateExpi.setVisibility(View.VISIBLE);
                    holder.setBackground.setVisibility(View.VISIBLE);

                    // On récup la valeur venue des Input Layout dans une variable
                    String vaccineName = holder.nameVaccinEditText.getText().toString();
                    String dateE = holder.dateVaccinEditText.getText().toString();
                    // On set le text avec ces valeurs
                    holder.textSetNameVaccin.setText(vaccineName);
                    holder.textSetDateExpi.setText(dateE);

                }else{
                    holder.textView.setTextColor(Color.BLUE);
                    holder.dateExpiLayout.setVisibility(View.VISIBLE);
                    holder.nameVaccinLayout.setVisibility(View.VISIBLE);
                    holder.textSetNameVaccin.setVisibility(View.GONE);
                    holder.textSetDateExpi.setVisibility(View.GONE);
                    holder.setBackground.setVisibility(View.GONE);
                    //Quand on appuye sur le rouge on passe ici
                    // donc il faudra supprimer de la list cette elm
                    items.remove(item);
                }
            }
        });


    }



    // total number of rows
    @Override
    public int getItemCount() {
        return items.size();
    }

    void addOne(){

        items.add(String.format(Locale.getDefault(),"%d",items.size() + 1));

    }
}