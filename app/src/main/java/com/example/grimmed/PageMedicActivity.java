package com.example.grimmed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

//Implementing the interface OnTabSelectedListener to our MainActivity
//This interface would help in swiping views
public class PageMedicActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    private String enceinteUser;

    private List<String> itemsAllergiesUser = new ArrayList<String>();

    private List<String> itemsAllergiesEnfant = new ArrayList<String>();

    private String enceinteMedoc;
    private String ordoMedoc;

    private List<String> itemsCompoMedoc = new ArrayList<String>();
    Bundle bundle;



    private static final Logger LOGGER = Logger.getLogger(PageMedicActivity.class.getName());

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_medic);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        ImageView buttonMedoc = findViewById(R.id.buttonMedoc);
        buttonMedoc.setOnClickListener(this::onClick);

        ImageView buttonVac = findViewById(R.id.buttonVac);
        buttonVac.setOnClickListener(this::onClick);

        ImageView buttonProfil = findViewById(R.id.buttonProfil);
        buttonProfil.setOnClickListener(this::onClick);

        ImageView backHome4= findViewById(R.id.backHome4);
        backHome4.setOnClickListener(this::onClick);

        //Adding toolbar to the activity
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);



        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Notice"));
        tabLayout.addTab(tabLayout.newTab().setText("Effets Secondaires"));
        tabLayout.addTab(tabLayout.newTab().setText("Détails"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);


        //----------------------------->
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Médicaments");
        }



        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            bundle = getIntent().getExtras();
            if(getIntent().hasExtra("nomMedoc")){
                //Log.e("MArche?",":"+bundle.getString("nomMedoc"));
                getValueUser(true);
            }
            else{
                getValueUser(false);
            }

            pagerSetup();

        }

        
    }

    private void getValueUser(Boolean parMedoc) {
        itemsAllergiesUser.clear();

        DatabaseReference medReference = FirebaseDatabase.getInstance().getReference("User").child(DataUser.username);
        medReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object nom = dataSnapshot.getValue(Object.class);
                JSONObject myInfo = new JSONObject((Map) nom);
                String enceinte = "0";
                JSONArray listAllergie = null;


                try {
                    enceinte = myInfo.getString("enceinte");
                    listAllergie = myInfo.getJSONArray("allergie");
                } catch (JSONException e) {
                    listAllergie = new JSONArray();
                }

                enceinteUser = enceinte;

                for(int i = 0 ; i<listAllergie.length();i++){
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            itemsAllergiesUser.add(AES.decrypt(listAllergie.get(i).toString()));
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                if(parMedoc){
                    getValueEnfant(parMedoc);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getValueEnfant(Boolean parMedoc) {
        itemsAllergiesEnfant.clear();

        DatabaseReference medReference = FirebaseDatabase.getInstance().getReference("User").child(DataUser.username)
                .child("enfant");
        medReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Object nom = dataSnapshot.getValue();
                        JSONObject myInfo = new JSONObject((Map) nom);
                        String i = childSnapshot.getKey();
                        JSONArray listAllergie = null;
                        try {
                            JSONObject nenfant = myInfo.getJSONObject(i);
                            listAllergie = nenfant.getJSONArray("allergie");
                         //   Log.e("TAG",""+nenfantAll);
                        } catch (JSONException e) {
                            listAllergie = new JSONArray();
                        }

                        Log.e("TAG",""+i);

                        for (int y = 0; y < listAllergie.length(); y++) {
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    itemsAllergiesEnfant.add(AES.decrypt(listAllergie.get(y).toString()));
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    if (parMedoc) {
                        myListeningTest(bundle.getString("nomMedoc"));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void myListeningTest(String nomMedoc) {

        itemsCompoMedoc.clear();

        DatabaseReference medReference = FirebaseDatabase.getInstance().getReference("Medicaments").child(nomMedoc);
        medReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object nom = dataSnapshot.getValue(Object.class);
                JSONObject myMedoc = new JSONObject((Map) nom);


                int enceinte = 0;
                int ordo = 0;
                JSONArray listCompo = new JSONArray();


                try {
                    enceinte = myMedoc.getInt("Enceinte");
                    ordo = myMedoc.getInt("Ordo");
                    listCompo = myMedoc.getJSONArray("Composition");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                enceinteMedoc = ""+enceinte;
                ordoMedoc = ""+ordo;

                for(int y=0;y<listCompo.length();y++){
                    try {
                        itemsCompoMedoc.add(listCompo.get(y).toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }


                DataUser.defaultObject = myMedoc;
                pagerSetup();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting data failed, log a message
                Log.e("Hein", "loadData:onCancelled", databaseError.toException());
            }
        });
    }

    private void pagerSetup(){
        //Initializing viewPager
        Boolean enceinteCheck = false;
        Boolean ordoCheck = false;
        List<String> dangerAllergies = new ArrayList<String>();
        List<String> dangerEnfant = new ArrayList<String>();

        for(int i = 0;i<itemsAllergiesUser.size();i++){
            for(int y = 0;y<itemsCompoMedoc.size();y++){
                if(Objects.equals(itemsCompoMedoc.get(y), itemsAllergiesUser.get(i))){
                    dangerAllergies.add(itemsCompoMedoc.get(y));
                }
            }
        }

        for(int i = 0;i<itemsAllergiesEnfant.size();i++){
            for(int y = 0;y<itemsCompoMedoc.size();y++){
                if(Objects.equals(itemsCompoMedoc.get(y), itemsAllergiesEnfant.get(i))){
                    dangerEnfant.add(itemsCompoMedoc.get(y));
                }
            }
        }

        if(Objects.equals(enceinteMedoc, enceinteUser) && Objects.equals(enceinteUser, "1")){
            enceinteCheck = true;
        }
        if(Objects.equals(ordoMedoc, "1")){
            ordoCheck = true;
        }

        viewPager = (ViewPager) findViewById(R.id.pager);

        Log.e("Pager:","man"+getSupportFragmentManager());

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount(),DataUser.defaultObject,
                enceinteCheck,dangerAllergies,dangerEnfant,ordoCheck,this);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        dangerAllergies.clear();
        dangerEnfant.clear();
    }

    private void myListening() {

        DatabaseReference medReference = FirebaseDatabase.getInstance().getReference("Medicaments");
        medReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object nom = dataSnapshot.getValue(Object.class);
                //On peut connaître le nombre de nom de médicament dans notre base
                long number = dataSnapshot.getChildrenCount();
                Log.e("MedocLenght","Here is the lenght !!:"+number);
                JSONObject myMedoc = new JSONObject((Map) nom);
                //On demande de recup un Object ou Array ou String etc...
                JSONArray newV = null;
                try {
                    newV = myMedoc.getJSONObject("Nom").getJSONArray("Cible");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Log.e("My value","Hi :"+newV.length());
                //On recup une valeur d'un array
                try {
                    Log.e("DirectVa","Hi f"+newV.get(0));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // Use the value of nom here
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting data failed, log a message
                Log.e("Hein", "loadData:onCancelled", databaseError.toException());
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.buttonMedoc) {
            Intent intent = new Intent(this, PageMedicActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonVac) {
            Intent intent = new Intent(this, VaccinTimerActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonProfil) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.backHome4) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
