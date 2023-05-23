package com.example.grimmed;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class VaccinTimerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> items = new ArrayList<String>();
    private List<String> itemsDate = new ArrayList<String>();

    private List<Integer> itemsId = new ArrayList<>();


    String childName;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccin_timer);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle bundle = getIntent().getExtras();

        createNotificationChannel();
        if (bundle == null) {

            childName = "";
        } else {
            childName = bundle.getString("childName", "");
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Vaccinations");
        }


        initAffichageIcone();

        recyclerView = findViewById(R.id.recyclerVaccin);

        getData();


    }


    private void setRecyclerView() {

        ImageView imageView = findViewById(R.id.cloche);
        imageView.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.listeVide);
        textView.setVisibility(View.GONE);
        TextView nomVaccin = findViewById(R.id.nomVacc);
        nomVaccin.setVisibility(View.VISIBLE);
        TextView dateVaccin = findViewById(R.id.dateVacc);
        dateVaccin.setVisibility(View.VISIBLE);
        TextView relance = findViewById(R.id.relanceText);
        relance.setVisibility(View.VISIBLE);
        relance.setOnClickListener(this::onClick);


        recyclerView = findViewById(R.id.recyclerVaccin);

        recyclerView.setVisibility(View.VISIBLE);

        VaccinAdapter adapter = new VaccinAdapter(items, itemsDate, itemsId, this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void voidRecyclerClocheOn() {

        ImageView imageView = findViewById(R.id.cloche);
        imageView.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.listeVide);
        textView.setVisibility(View.VISIBLE);
        TextView nomVaccin = findViewById(R.id.nomVacc);
        nomVaccin.setVisibility(View.GONE);
        TextView dateVaccin = findViewById(R.id.dateVacc);
        dateVaccin.setVisibility(View.GONE);
        TextView relance = findViewById(R.id.relanceText);
        relance.setVisibility(View.GONE);

        recyclerView.setVisibility(View.GONE);

    }

    public void initAffichageIcone() {
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
            itemsId.add(0);

            setRecyclerView();
        }
        if(v.getId() == R.id.relanceText){
            setAllVaccin();
        }
    }

    private void getData() {
        items.clear();
        itemsDate.clear();
        itemsId.clear();
        DatabaseReference cibleReference;

        if (!Objects.equals(childName, "")) {
            cibleReference = FirebaseDatabase.getInstance().getReference("User")
                    .child(DataUser.username).child("enfant").child(childName).child("vaccins");
        } else {
            cibleReference = FirebaseDatabase.getInstance().getReference("User")
                    .child(DataUser.username).child("vaccins");
        }
        cibleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object nom = dataSnapshot.getValue(Object.class);
                assert nom != null;
                if (!nom.toString().equals("")) {
                    //JSONObject myInfo = new JSONObject((Map) nom);
                    //String newV = null;
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // Get the key (i.e., the identifier) of the child node
                        String key = childSnapshot.getKey();

                        Map<String, Object> childData = (Map<String, Object>) childSnapshot.getValue();

                        // Extract the date and ID values from the child data
                        assert childData != null;
                        String date = Objects.requireNonNull(childData.get("date")).toString();
                        int id = Integer.parseInt(Objects.requireNonNull(childData.get("id")).toString());



                        items.add(key);
                        itemsDate.add(date);
                        itemsId.add(id);

                    }
                    if (items.isEmpty()) {
                        items.add(" ");
                        itemsDate.add(" ");
                        itemsId.add(0);
                    }

                    setRecyclerView();
                }
                //Si c'est vide ou que sa à était vidé
                else {
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

        if (!Objects.equals(item, "DELETE") && !Objects.equals(itemDate, "DELETE")) {

            Random random = new Random();
            int idVaccin;
            do {
                idVaccin = random.nextInt(999) + 1;  // Generates a random number between 1 and 999
            } while (itemsId.contains(idVaccin));


            if (Objects.equals(childName, "")) {

                myRef.child(DataUser.username).child("vaccins").child(item)
                        .child("date").setValue(itemDate);
                myRef.child(DataUser.username).child("vaccins").child(item)
                        .child("id").setValue(idVaccin);
                createNotTime(item, itemDate, idVaccin);
            } else {
                myRef.child(DataUser.username).child("enfant").child(childName)
                        .child("vaccins").child(item).child("date")
                        .setValue(itemDate);
                myRef.child(DataUser.username).child("enfant").child(childName)
                        .child("vaccins").child(item).child("id")
                        .setValue(idVaccin);
                createNotTime(item, itemDate, idVaccin);
            }
        } else {
            if (Objects.equals(childName, "")) {
                myRef.child(DataUser.username).child("vaccins").setValue("");
            } else {
                myRef.child(DataUser.username).child("enfant").child(childName)
                        .child("vaccins").setValue("");
            }
            for (int i = 0; i < items.size(); i++) {
                if (Objects.equals(childName, "")) {
                    myRef.child(DataUser.username).child("vaccins").child(items.get(i))
                            .child("date").setValue(itemsDate.get(i));

                    myRef.child(DataUser.username).child("vaccins").child(items.get(i))
                            .child("id").setValue(itemsId.get(i));
                } else {
                    myRef.child(DataUser.username).child("enfant").child(childName)
                            .child("vaccins").child(items.get(i)).child("date")
                            .setValue(itemsDate.get(i));

                    myRef.child(DataUser.username).child("enfant").child(childName)
                            .child("vaccins").child(items.get(i)).child("id")
                            .setValue(itemsId.get(i));
                }
            }
        }
        getData();
    }

    public void deleteOnData(int position) {

        itemsDate.remove(position);
        items.remove(position);
        Integer itemId = itemsId.get(position);
        itemsId.remove(position);

        //On met à jour la bdd
        saveOnData("DELETE", "DELETE");

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationVaccin.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, itemId, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "My Notification Channel 2";
            String descriptionText = "My Notification Channel Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("my_channel_01", name, importance);
            channel.setDescription(descriptionText);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void createNotTime(String nomVaccin, String dateVaccin, int idVaccin) {
        Calendar calNow = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        Intent notificationIntent = new Intent(this, NotificationVaccin.class);
        notificationIntent.putExtra("Rappel", "Hey n'oubliez pas votre vaccin pour: " + nomVaccin);
        notificationIntent.putExtra("id", ""+idVaccin);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, idVaccin, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);



        //------------
        int jour = Integer.parseInt(dateVaccin.substring(0, 2));
        int mois = Integer.parseInt(dateVaccin.substring(3, 5));
        int an = Integer.parseInt(dateVaccin.substring(6, 10));

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, an);
        calendar.set(Calendar.MONTH, mois-1);
        calendar.set(Calendar.DAY_OF_MONTH, jour);
        calendar.set(Calendar.HOUR_OF_DAY, calNow.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calNow.get(Calendar.MINUTE)+2);
        calendar.set(Calendar.SECOND, calNow.get(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, 0);

        Log.e("TimeMili", "" + calendar.getTimeInMillis());

        Log.e("Et le curren", "" + calNow.getTimeInMillis());

        //Si la notif est set avant l'heure actuelle alors on l'envoie maitenant
        if ((calendar.getTimeInMillis() - calNow.getTimeInMillis()) < 0) {
            calendar = calNow;
        }
        // On set l'alarme avec l'action , le temps et l'intent.
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }

    private void setAllVaccin(){
        //On set sur tout les vaccins la notification
        if(items.size()>=1 && items.get(0)!=" "){
            for(int i = 0;i<items.size();i++){
                createNotTime(items.get(i),itemsDate.get(i),itemsId.get(i));
            }
            Toast.makeText(this, "Toute les notifications sont entrées !", Toast.LENGTH_SHORT).show();
        }
    }
}