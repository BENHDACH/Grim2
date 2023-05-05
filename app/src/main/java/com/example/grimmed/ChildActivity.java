package com.example.grimmed;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChildActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> items = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Vos enfants");

        recyclerView = findViewById(R.id.enfantsList);

        items.add("");
        ChildAdapter adapter = new ChildAdapter(items);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}