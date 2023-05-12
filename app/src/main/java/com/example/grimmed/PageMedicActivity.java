package com.example.grimmed;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//Implementing the interface OnTabSelectedListener to our MainActivity
//This interface would help in swiping views
public class PageMedicActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    private static final Logger LOGGER = Logger.getLogger(PageMedicActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_medic);

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

        //makeRequest();
        new NetworkTask().execute();

        //----------------------------->
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Médicaments");
        }


        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("nomMedoc")){
           //Log.e("MArche?",":"+bundle.getString("nomMedoc"));
            myListeningTest(bundle.getString("nomMedoc"));
        }
        //myListening();

        pagerSetup();
        
    }

    private void myListeningTest(String nomMedoc) {

        DatabaseReference medReference = FirebaseDatabase.getInstance().getReference("Medicaments").child(nomMedoc);
        medReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object nom = dataSnapshot.getValue(Object.class);
                JSONObject myMedoc = new JSONObject((Map) nom);
                Log.e("JsonObject",""+myMedoc);

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
        viewPager = (ViewPager) findViewById(R.id.pager);


        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount(),DataUser.defaultObject);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
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
    private void makeRequest() {
            try {
                // Create a URL object from the API endpoint
                URL url = new URL("https://restcountries.com/v3.1/all");

                // Open a connection to the URL using HttpURLConnection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // Read the response from the API
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder responseBuilder = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseBuilder.append(inputLine);
                }
                in.close();

                // Print the response to the console using a log
                LOGGER.log(Level.INFO, responseBuilder.toString());

                // Disconnect the connection
                conn.disconnect();

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error: " + e.getMessage(), e);
            }
        }


        //----------> AJOUT TEST

    private class NetworkTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            // Perform network operation here
            String result = "";
            try {
                URL url = new URL("https://restcountries.com/v3.1/all");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("VOICIII",result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Update UI with the result
            //mTextView.setText(result);
        }
    }

}
