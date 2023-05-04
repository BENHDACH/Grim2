package com.example.grimmed;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.Map;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<String> items;
    private SearchActivity listener;
    public interface OnItemClickListener {
        void onItemClick(String item);
    }
    public SearchAdapter(List<String> items, SearchActivity listener) {
        this.items = items;
        this.listener = listener;
    }

    // inner class to hold a reference to each item view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        private List<String> items;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.nomSearch);
        }

    }

    // inflates the item views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_search, parent, false);
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
                listener.onItemClick(item);
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