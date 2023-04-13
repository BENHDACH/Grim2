package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Home");
        }

        ImageView buttonMedoc = findViewById(R.id.buttonMedoc);
        buttonMedoc.setOnClickListener(this::onClick);

        ImageView buttonVac = findViewById(R.id.buttonVac);
        buttonVac.setOnClickListener(this::onClick);

        ImageView buttonProfil = findViewById(R.id.buttonProfil);
        buttonProfil.setOnClickListener(this::onClick);

        ImageView sympTete = findViewById(R.id.tete);
        sympTete.setOnClickListener(this::onClick);

        ImageView sympEstomac = findViewById(R.id.estomac);
        sympEstomac.setOnClickListener(this::onClick);

        ImageView sympGorge = findViewById(R.id.gorge);
        sympGorge.setOnClickListener(this::onClick);

        ImageView sympCycle = findViewById(R.id.cycle);
        sympCycle.setOnClickListener(this::onClick);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.buttonMedoc) {
            Intent intent = new Intent(this, PageMedicActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonVac) {
            Intent intent = new Intent(this, VaccinTimerActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonProfil) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.tete) {
            Toast.makeText(getApplicationContext(),"Maux de tête", Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.estomac) {
            Toast.makeText(getApplicationContext(),"Brûlure d'estomac", Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.gorge) {
            Toast.makeText(getApplicationContext(),"Toux", Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.cycle) {
            Toast.makeText(getApplicationContext(),"Menstruations", Toast.LENGTH_SHORT).show();
        }
    }
}