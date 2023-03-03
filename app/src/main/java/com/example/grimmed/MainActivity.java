package com.example.grimmed;

import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.titleCo);

        //On change la police
        Typeface font = Typeface.createFromAsset(getAssets(), "font/MysteryQuest-Regular.ttf");
        textView.setTypeface(font);
    }
}