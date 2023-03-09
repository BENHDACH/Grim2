package com.example.grimmed;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.titleCo);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this::onClick);

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this::onClick);

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(this::onClick);

        //On change la police
        Typeface font = Typeface.createFromAsset(getAssets(), "font/MysteryQuest-Regular.ttf");
        textView.setTypeface(font);


    }


    //Fonction pour désigner l'action de cliquer sur un bouton
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            // Switch to the new activity
            Intent intent = new Intent(this, PageMedicActivity.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.button2) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.button3) {
            Intent intent = new Intent(this, VaccinTimerActivity.class);
            startActivity(intent);
        }
    }

    private void buttonClick(){

    }
}