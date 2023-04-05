package com.example.grimmed;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class VaccinTimerActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private List<String> items = new ArrayList<String>();
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
            actionBar.setTitle("Vaccin Timer");
        }

        mySuperList.add(new Object[]{"nom du vaccin", "dd/mm/yy", 1});




        initAffichageIcone();

        recyclerView = findViewById(R.id.recyclerVaccin);
        items.add("A");
        items.add("O");
        items.add("I");
        items.add("E");
        items.add("Y");
        itemsObjets.add(new String[]{"nom du vaccin", "dd/mm/yy", "SET?"});
        VaccinAdapter adapter = new VaccinAdapter(items);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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

            VaccinAdapter adapter = new VaccinAdapter(items);
            recyclerView.setAdapter(adapter);

            adapter.addOne();
        }
    }
}