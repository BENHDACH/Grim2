package com.example.grimmed;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> items = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.e("PROBLEME", "PROBLEME");
        TextView consignes = findViewById(R.id.consignes);
        Bundle bundle = getIntent().getExtras();
        Boolean allergies = bundle.getBoolean("allergies", false);
        Boolean usual = bundle.getBoolean("usual", false);


        if (allergies == true){
            consignes.setText(R.string.allergies);
        }
        if (usual == true){
            consignes.setText(R.string.usual_traitement);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (allergies == true){
                actionBar.setTitle("Allergies");
            }
            if (usual == true){
                actionBar.setTitle("Traitement");
            }

        }

        recyclerView = findViewById(R.id.listAT);

        items.add("");
        DetailAdapter adapter = new DetailAdapter(items);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}