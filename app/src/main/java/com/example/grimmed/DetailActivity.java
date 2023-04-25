package com.example.grimmed;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
    }
}