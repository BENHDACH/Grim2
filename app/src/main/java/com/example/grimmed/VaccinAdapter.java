package com.example.grimmed;

import android.annotation.SuppressLint;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class VaccinAdapter extends RecyclerView.Adapter<VaccinAdapter.ViewHolder> {

    private List<String> items;
    private List<String> itemsDate;

    private List<Integer> itemsId;

    private VaccinTimerActivity listener;

    public VaccinAdapter(List<String> items, List<String> itemsDate, List<Integer> itemsId, VaccinTimerActivity listener) {
        this.items = items;
        this.itemsDate = itemsDate;
        this.listener = listener;
        this.itemsId = itemsId;
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
        Integer itemId = itemsId.get(position);
        int myPosition = position;


        if(!Objects.equals(item, " ") && !Objects.equals(itemDate, " ") && itemId!=0){
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
                    //On peut check la sauvegarde si la date est une date pour commencer



                    if(holder.dateVaccinEditText.getText().toString().matches("^\\d{2}\\/\\d{2}\\/\\d{4}$")){
                        String test =holder.dateVaccinEditText.getText().toString();
                        int jour = Integer.parseInt(test.substring(0, 2));
                        int mois = Integer.parseInt(test.substring(3, 5));
                        int an = Integer.parseInt(test.substring(6, 10));

                        //check l'existance de la date
                        // Create a Calendar instance for the desired future date
                        Calendar futureDate = Calendar.getInstance();
                        futureDate.set(Calendar.YEAR, an);
                        futureDate.set(Calendar.MONTH, mois-1);
                        futureDate.set(Calendar.DAY_OF_MONTH,jour);

                        // Check si la date est valid
                        boolean dateExists = isValidDate(""+mois+"/"+jour+"/"+an);

                        if(dateExists){
                            try {
                                listener.saveOnData(holder.nameVaccinEditText.getText().toString(),holder.dateVaccinEditText.getText().toString());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else{
                            Toast.makeText(listener, "La date n'existe pas", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(listener, "La date n'est pas conforme à dd/mm/yyyy exemple : 18/01/2000", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    private static boolean isValidDate(String input) {
        String formatString = "MM/dd/yyyy";

        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            format.parse(input);
        } catch (ParseException | IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    public static void main(String[] args){
        System.out.println(isValidDate("45/23/234")); // false
        System.out.println(isValidDate("12/12/2111")); // true
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