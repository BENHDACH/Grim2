package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PageLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_log);

        Button logPage = findViewById(R.id.logPage);
        TextView redirection = findViewById(R.id.redirection);
        logPage.setOnClickListener(this::onClick);
        redirection.setOnClickListener(this::onClick);
    }

    public void onClick(View v){
        if (v.getId() == R.id.logPage) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.redirection) {
            Intent intent = new Intent(this, PageSignActivity.class);
            startActivity(intent);
        }
    }
}