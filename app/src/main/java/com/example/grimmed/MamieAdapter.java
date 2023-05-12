package com.example.grimmed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class MamieAdapter extends RecyclerView.Adapter<MamieAdapter.ViewHolder>{

        private JSONArray items;


        public MamieAdapter(JSONArray items) {
            this.items = items;
        }

        // inner class to hold a reference to each item view
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public ViewHolder(View view) {
                super(view);
                textView = view.findViewById(R.id.textSoluce);
            }

        }

        // inflates the item views
        @Override
        public com.example.grimmed.MamieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_mamies, parent, false);
            return new com.example.grimmed.MamieAdapter.ViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

            JSONArray myArray = items;
            String concatInfo = "";

            Log.e("item obtenu",""+items);
            Log.e("item leng",""+myArray.length());


            for(int i=0;i<myArray.length();i++){
                try {
                    Log.e("item getString","i="+i+" string:"+myArray.getString(i));
                    concatInfo = concatInfo+myArray.getString(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                concatInfo = concatInfo+"\n\n";
            }

            viewHolder.textView.setText(concatInfo);

    }


        @Override
        public int getItemCount() {
            return 1;
        }

}
