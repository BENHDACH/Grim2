package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MamieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mamie);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Remèdes de Grand-mère");
        }

        ImageView backHome3 = findViewById(R.id.backHome3);
        backHome3.setOnClickListener(this::onClick);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.backHome3) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }

    }

}