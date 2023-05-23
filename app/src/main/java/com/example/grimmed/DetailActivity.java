package com.example.grimmed;

import android.annotation.SuppressLint;
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
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> items = new ArrayList<String>();
    private List<String> itemsDecrypt = new ArrayList<String>();

    Boolean allergies;
    Boolean usual;
    Boolean all;
    Boolean use;

    String childName;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        TextView consignes = findViewById(R.id.consignes);
        ImageView plusDetail = findViewById(R.id.plusDetail);
        plusDetail.setOnClickListener(this::onClick);
        Bundle bundle = getIntent().getExtras();
        allergies = bundle.getBoolean("allergies", false);
        usual = bundle.getBoolean("usual", false);
        all = bundle.getBoolean("all", false);
        use = bundle.getBoolean("use", false);
        childName = bundle.getString("childName","");

        ImageView backHome6 = findViewById(R.id.backHome6);
        backHome6.setOnClickListener(this::onClick);


        if (allergies ||all){
            consignes.setText(R.string.allergies);
        }
        if (usual || use){
            consignes.setText(R.string.usual_traitement);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (allergies || all ){
                actionBar.setTitle("Allergies");
            }
            if (usual || use){
                actionBar.setTitle("Traitement");
            }

        }

        getData();

    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.listAT);

        DetailAdapter adapter = new DetailAdapter(itemsDecrypt,this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.plusDetail) {
            itemsDecrypt.add(" ");
            setRecyclerView();
        }

        if (v.getId() == R.id.backHome6) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }
    }

    public void saveOnData(String item) throws Exception {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        if(!Objects.equals(item, "DELETE")){
            String encryptedItem = AES.encrypt(item);
            items.add(encryptedItem);
        }

        if (allergies){
            myRef.child(DataUser.username).child("allergie").setValue(items);
        }
        else if(all && !Objects.equals(childName, "")){
            myRef.child(DataUser.username).child("enfant").child(childName)
                    .child("allergie").setValue(items);
        }
        else if(usual){
            myRef.child(DataUser.username).child("traitement").setValue(items);
        }
        else if(use && !Objects.equals(childName, "")){
            myRef.child(DataUser.username).child("enfant").child(childName)
                    .child("traitement").setValue(items);
        }

        getData();
    }

    private void getData(){
        items.clear();
        itemsDecrypt.clear();

        DatabaseReference cibleReference = FirebaseDatabase.getInstance().getReference("User")
                .child(DataUser.username);
        cibleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object nom = dataSnapshot.getValue(Object.class);
                JSONObject myInfo = new JSONObject((Map) nom);
                JSONArray newV = null;

                try {
                    if(allergies){
                        newV = myInfo.getJSONArray("allergie");
                    }else if(all && !Objects.equals(childName, "")){
                        newV = myInfo.getJSONObject("enfant").getJSONObject(childName)
                                .getJSONArray("allergie");
                    }
                    else if (usual) {
                        newV = myInfo.getJSONArray("traitement");
                    }
                    else if(use && !Objects.equals(childName, "")){
                        newV = myInfo.getJSONObject("enfant").getJSONObject(childName)
                                .getJSONArray("traitement");
                    }
                } catch (JSONException e) {
                    newV = new JSONArray();
                    newV.put(" ");
                }

                //On recup les données
                for (int i = 0; i < newV.length(); i++) {

                    try {
                        if(!Objects.equals(newV.getString(i), " ")){

                            items.add(newV.getString(i));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                itemsDecrypt.add(AES.decrypt(newV.getString(i)));
                            }

                        }
                        else{
                            itemsDecrypt.add(" ");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                setRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void deleteOnData(int position) throws Exception {
        itemsDecrypt.remove(position);
        items.remove(position);

        //On met à jour la bdd
        saveOnData("DELETE");
    }
}