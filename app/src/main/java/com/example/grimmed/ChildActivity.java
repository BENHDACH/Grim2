package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        ImageView plusDetail = findViewById(R.id.plusEnfants);
        plusDetail.setOnClickListener(this::onClick);

        ImageView backHome7 = findViewById(R.id.backHome7);
        backHome7.setOnClickListener(this::onClick);

        items.add("");
        ChildAdapter adapter = new ChildAdapter(items, this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }
    public void changeActivity(int i) {
        if(i==1) {
            Intent intent = new Intent(this, VaccinTimerActivity.class);
            startActivity(intent);
        }
        if(i==2) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("use", true);
            startActivity(intent);
        }
        if(i==3) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("all", true);
            startActivity(intent);
        }
    }
    public void onClick(View v) {
        if (v.getId() == R.id.plusEnfants) {
            ChildAdapter adapter = new ChildAdapter(items, this);
            recyclerView.setAdapter(adapter);
            adapter.addOne();
        }

        if (v.getId() == R.id.backHome7) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }
    }

    public void saveOnData(String item) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        //Un enfant de l'username est enregistrez
        myRef.child(DataUser.username).child(item).setValue("");
    }
}