package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MamieActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private RemedeAdapter remedeAdapter;
    private DatabaseReference recetteRef;

    private List<String> items = new ArrayList<String>();

    private List<Integer> itemsScore = new ArrayList<Integer>();
    private List<JSONArray> itemsSoluce = new ArrayList<JSONArray>();
    private List<Boolean> itemsShow = new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mamie);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Remèdes");
        }

        ImageView backHome5 = findViewById(R.id.backHome3);
        backHome5.setOnClickListener(this::clickHome);

        recetteRef = FirebaseDatabase.getInstance().getReference("Recette");

        loadDataFromFirebase();
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        remedeAdapter = new RemedeAdapter(items,itemsSoluce,itemsShow,this);
        recyclerView.setAdapter(remedeAdapter);
    }

    private void loadDataFromFirebase() {
        items.clear();
        itemsSoluce.clear();
        itemsShow.clear();
        itemsScore.clear();

        recetteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot symptomeSnapshot : dataSnapshot.getChildren()) {
                    String symptome = symptomeSnapshot.getKey();
                    items.add(symptome);

                    Log.e("key?",""+symptome);


                    Object nom = symptomeSnapshot.getValue();
                    JSONObject myInfo = new JSONObject((Map) nom);
                    JSONArray newV = new JSONArray();
                    Integer score = 50;

                    try {
                        newV = myInfo.getJSONArray("Traitement");
                        score = myInfo.getInt("Score");
                    } catch (JSONException e) {
                        newV.put(" ");
                    }
                    itemsSoluce.add(newV);
                    itemsShow.add(false);

                }

                if(items.isEmpty()){
                    items.add(" ");

                }

                setRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MamieActivity.this, "Erreur de chargement des données.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onItemClick(int position, Boolean itemBool) {

        if(itemsShow.get(position)){
            itemsShow.set(position,false);
        }
        else{
            itemsShow.set(position,true);
        }


        setRecyclerView();

    }

    private void clickHome(View v) {
        if (v.getId() == R.id.backHome3) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }
    }

}
