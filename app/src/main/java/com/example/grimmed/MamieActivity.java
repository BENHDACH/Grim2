package com.example.grimmed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MamieActivity extends AppCompatActivity implements RemedeAdapter.OnItemClickListener {

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
        remedeAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(remedeAdapter);

        recetteRef = FirebaseDatabase.getInstance().getReference("Recette");

        loadDataFromFirebase();
    }

    private void loadDataFromFirebase() {
        recetteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> symptomes = new ArrayList<>();
                Map<String, List<String>> traitementsMap = new HashMap<>();

                for (DataSnapshot symptomeSnapshot : dataSnapshot.getChildren()) {
                    String symptome = symptomeSnapshot.getKey();
                    symptomes.add(symptome);

                    GenericTypeIndicator<List<String>> indicator = new GenericTypeIndicator<List<String>>() {};
                    List<String> traitements = symptomeSnapshot.child("Traitement").getValue(indicator);
                    traitementsMap.put(symptome, traitements);
                }

                remedeAdapter.setSymptomes(symptomes);
                remedeAdapter.setTraitements(traitementsMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MamieActivity.this, "Erreur de chargement des données.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(String symptome, List<String> traitements) {
        // Créer une liste contenant les traitements
        String[] traitementsArray = new String[traitements.size()];
        traitementsArray = traitements.toArray(traitementsArray);

        // Créer un AlertDialog avec une liste déroulante
        AlertDialog.Builder builder = new AlertDialog.Builder(MamieActivity.this);
        String[] finalTraitementsArray = traitementsArray;
        builder.setTitle(symptome)
                .setItems(traitementsArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Action à effectuer lorsque l'utilisateur sélectionne un traitement
                        String selectedTraitement = finalTraitementsArray[which];
                        // Faites quelque chose avec le traitement sélectionné
                    }
                })
                .setPositiveButton("Fermer", null);

        // Afficher l'AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
