package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PageSignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_sign);

        Button logPage = findViewById(R.id.sigPage);
        logPage.setOnClickListener(this::onClick);
    }

    public void onClick(View v){
        if (v.getId() == R.id.sigPage) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }
    }
}