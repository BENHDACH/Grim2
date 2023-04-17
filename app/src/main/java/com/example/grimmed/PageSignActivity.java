package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PageSignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_sign);

        Button logPage = findViewById(R.id.sigPage);
        TextView redirectionLog = findViewById(R.id.redirectionLog);
        logPage.setOnClickListener(this::onClick);
        redirectionLog.setOnClickListener(this::onClick);
    }

    public void onClick(View v){
        if (v.getId() == R.id.sigPage) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.redirectionLog) {
            Intent intent = new Intent(this, PageLogActivity.class);
            startActivity(intent);
        }
    }
}