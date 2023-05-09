package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MamieActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RemedeAdapter remedeAdapter;

    private DatabaseReference recetteRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mamie);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        remedeAdapter = new RemedeAdapter();
        recyclerView.setAdapter(remedeAdapter);

        recetteRef = FirebaseDatabase.getInstance().getReference("Recette");

        loadDataFromFirebase();
    }

    private void loadDataFromFirebase() {
        recetteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> symptomes = new ArrayList<>();

                for (DataSnapshot symptomeSnapshot : dataSnapshot.getChildren()) {
                    String symptome = symptomeSnapshot.getKey();
                    symptomes.add(symptome);
                }

                remedeAdapter.setSymptomes(symptomes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MamieActivity.this, "Erreur de chargement des données.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
