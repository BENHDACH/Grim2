package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ImageView buttonMedoc = findViewById(R.id.buttonMedoc);
        buttonMedoc.setOnClickListener(this::onClick);

        ImageView buttonVac = findViewById(R.id.buttonVac);
        buttonVac.setOnClickListener(this::onClick);

        ImageView buttonProfil = findViewById(R.id.buttonProfil);
        buttonProfil.setOnClickListener(this::onClick);
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
    }
}