package com.example.grimmed;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VaccinTimerActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private List<String> items = new ArrayList<String>();
    private List<String> itemsDate = new ArrayList<String>();
    private List<Object> itemsObjets = new ArrayList<>();

    public static List<Object[]> createList(Object[]... elements) {
        List<Object[]> list = new ArrayList<>();
        for (Object[] array : elements) {
            list.add(array);
        }
        return list;
    }

    List<Object[]> mySuperList = createList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccin_timer);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Vaccinations");
        }

        mySuperList.add(new Object[]{"nom du vaccin", "dd/mm/yy", 1});




        initAffichageIcone();

        recyclerView = findViewById(R.id.recyclerVaccin);

        itemsObjets.add(new String[]{"nom du vaccin", "dd/mm/yy", "SET?"});

        getData();

    }

    private void setRecyclerView(){

        ImageView imageView = findViewById(R.id.cloche);
        imageView.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.listeVide);
        textView.setVisibility(View.GONE);
        TextView nomVaccin = findViewById(R.id.nomVacc);
        nomVaccin.setVisibility(View.VISIBLE);
        TextView dateVaccin = findViewById(R.id.dateVacc);
        dateVaccin.setVisibility(View.VISIBLE);


        recyclerView = findViewById(R.id.recyclerVaccin);

        recyclerView.setVisibility(View.VISIBLE);

        VaccinAdapter adapter = new VaccinAdapter(items, itemsDate,this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void voidRecyclerClocheOn(){

        ImageView imageView = findViewById(R.id.cloche);
        imageView.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.listeVide);
        textView.setVisibility(View.VISIBLE);
        TextView nomVaccin = findViewById(R.id.nomVacc);
        nomVaccin.setVisibility(View.GONE);
        TextView dateVaccin = findViewById(R.id.dateVacc);
        dateVaccin.setVisibility(View.GONE);

        recyclerView.setVisibility(View.GONE);

    }

    public void initAffichageIcone(){
        ImageView buttonMedoc3 = findViewById(R.id.buttonMedoc3);
        buttonMedoc3.setOnClickListener(this::onClick);

        ImageView buttonVac3 = findViewById(R.id.buttonVac3);
        buttonVac3.setOnClickListener(this::onClick);

        ImageView buttonProfil3 = findViewById(R.id.buttonProfil3);
        buttonProfil3.setOnClickListener(this::onClick);

        ImageView backHome = findViewById(R.id.backHome);
        backHome.setOnClickListener(this::onClick);

        ImageView plusVaccin = findViewById(R.id.plusVaccinNotif);
        plusVaccin.setOnClickListener(this::onClick);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.buttonMedoc3) {
            Intent intent = new Intent(this, PageMedicActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonVac3) {
            Intent intent = new Intent(this, VaccinTimerActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonProfil3) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.backHome) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.plusVaccinNotif) {

            items.add(" ");
            itemsDate.add(" ");

            setRecyclerView();
        }
    }

    private void getData(){
        items.clear();
        itemsDate.clear();

        DatabaseReference cibleReference = FirebaseDatabase.getInstance().getReference("User")
                .child(DataUser.username).child("vaccins");
        cibleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object nom = dataSnapshot.getValue(Object.class);
                assert nom != null;
                if(!nom.toString().equals("")){
                    JSONObject myInfo = new JSONObject((Map) nom);
                    String newV = null;

                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // Get the key (i.e., the identifier) of the child node
                        String key = childSnapshot.getKey();

                        try {
                            newV = myInfo.getString(key);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        items.add(key);
                        itemsDate.add(newV);

                    }
                    if(items.isEmpty()){
                        items.add(" ");
                        itemsDate.add(" ");
                    }

                    setRecyclerView();
                }
                //Si c'est vide ou que sa à était vidé
                else{
                    voidRecyclerClocheOn();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void saveOnData(String item, String itemDate) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        if(!Objects.equals(item, "DELETE") && !Objects.equals(itemDate, "DELETE")){
            myRef.child(DataUser.username).child("vaccins").child(item)
                    .setValue(itemDate);
        }
        else{
                myRef.child(DataUser.username).child("vaccins").setValue("");
                for(int i = 0; i<items.size(); i++){
                    myRef.child(DataUser.username).child("vaccins").child(items.get(i))
                            .setValue(itemsDate.get(i));
                }

        }
        getData();
    }

    public void deleteOnData(int position) {

        Log.e("Pos",""+position+" l'item est:"+items.get(position));
        itemsDate.remove(position);
        items.remove(position);

        //On met à jour la bdd
        saveOnData("DELETE","DELETE");
    }
}