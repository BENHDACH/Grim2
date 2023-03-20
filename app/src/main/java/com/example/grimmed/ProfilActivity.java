package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ProfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ImageView buttonMedoc4 = findViewById(R.id.buttonMedoc4);
        buttonMedoc4.setOnClickListener(this::onClick);

        ImageView buttonVac4 = findViewById(R.id.buttonVac4);
        buttonVac4.setOnClickListener(this::onClick);

        ImageView buttonProfil4 = findViewById(R.id.buttonProfil4);
        buttonProfil4.setOnClickListener(this::onClick);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.buttonMedoc4) {
            Intent intent = new Intent(this, PageMedicActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonVac4) {
            Intent intent = new Intent(this, VaccinTimerActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonProfil4) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
        }
    }
}