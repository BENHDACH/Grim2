package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class VaccinTimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccin_timer);

        ImageView buttonMedoc3 = findViewById(R.id.buttonMedoc3);
        buttonMedoc3.setOnClickListener(this::onClick);

        ImageView buttonVac3 = findViewById(R.id.buttonVac3);
        buttonVac3.setOnClickListener(this::onClick);

        ImageView buttonProfil3 = findViewById(R.id.buttonProfil3);
        buttonProfil3.setOnClickListener(this::onClick);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.buttonMedoc3) {
            Intent intent = new Intent(this, PageMedicActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonVac3) {
            Intent intent = new Intent(this, VaccinTimerActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonProfil3) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
        }
    }
}