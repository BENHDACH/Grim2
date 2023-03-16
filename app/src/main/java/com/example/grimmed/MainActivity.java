package com.example.grimmed;

import android.annotation.SuppressLint;
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

        /*Button button = findViewById(R.id.button);
        button.setOnClickListener(this::onClick);

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this::onClick);

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(this::onClick);*/

        Button log = findViewById(R.id.log);
        log.setOnClickListener(this::onClick);

        Button sig = findViewById(R.id.sig);
        sig.setOnClickListener(this::onClick);

        //On change la police
        Typeface font = Typeface.createFromAsset(getAssets(), "font/MysteryQuest-Regular.ttf");
        textView.setTypeface(font);


    }


    //Fonction pour désigner l'action de cliquer sur un bouton
    public void onClick(View v) {
        if (v.getId() == R.id.log) {
            Intent intent = new Intent(this, PageLogActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.sig) {
            Intent intent = new Intent(this, PageMedicActivity.class);
            startActivity(intent);
        }
    }

    private void buttonClick(){

    }
}