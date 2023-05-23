package com.example.grimmed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MamieActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private RemedeAdapter remedeAdapter;
    private DatabaseReference recetteRef;

    private List<String> items = new ArrayList<String>();

    private List<Integer> itemsScore = new ArrayList<Integer>();
    private List<JSONArray> itemsSoluce = new ArrayList<JSONArray>();
    private List<Boolean> itemsShow = new ArrayList<Boolean>();

    private List<Object> listVotants = new ArrayList<>();

    List<List<Object>> combinedList = new ArrayList<List<Object>>();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mamie);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Remèdes");
        }


        recetteRef = FirebaseDatabase.getInstance().getReference("Recette");

        loadDataFromFirebase(0);

        ImageView backHome3 = findViewById(R.id.backHome3);
        backHome3.setOnClickListener(this::clickHome);
    }

    private void clickHome(View v) {
        if(v.getId() == R.id.backHome3){
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }
    }


    private void setRecyclerView(int position){

        //On recup les données prises en une seul


        // On réorganise selon le score
        Collections.sort(combinedList, new Comparator<List<Object>>() {
            @Override
            public int compare(List<Object> o1, List<Object> o2) {
                Integer score1 = (Integer) o1.get(1);
                Integer score2 = (Integer) o2.get(1);
                return score2.compareTo(score1); // Ordonne selon le score
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        remedeAdapter = new RemedeAdapter(items,itemsSoluce,itemsShow,this,itemsScore,combinedList);
        recyclerView.setAdapter(remedeAdapter);

        //On remet notre scroll au point où on étais
        recyclerView.scrollToPosition(position);
    }

    private void loadDataFromFirebase(int position) {
        items.clear();
        itemsSoluce.clear();
        itemsShow.clear();
        itemsScore.clear();
        combinedList.clear();


        recetteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Boolean> itemsVoted = new ArrayList<>();
                List<Integer> itemsVote = new ArrayList<>();
                for (DataSnapshot symptomeSnapshot : dataSnapshot.getChildren()) {
                    Boolean userHasVoted = false;
                    int voteOfUser = 0;
                    String symptome = symptomeSnapshot.getKey();
                    items.add(symptome);

                    Log.e("key?",""+symptome);


                    Object nom = symptomeSnapshot.getValue();
                    JSONObject myInfo = new JSONObject((Map) nom);
                    JSONArray newV = new JSONArray();
                    Integer score = 50;
                    List<Object> votantAndS = new ArrayList<>();

                    try {
                        newV = myInfo.getJSONArray("Traitement");
                        score = myInfo.getInt("Score");
                    } catch (JSONException e) {
                        newV.put(" ");
                    }

                    if(symptomeSnapshot.child("Votant").exists()) {

                        List<Object> votantsList = (List<Object>) symptomeSnapshot
                                .child("Votant").getValue();

                        for (Object votantObj : votantsList) {
                            if (votantObj instanceof List) {
                                List<Object> votant = (List<Object>) votantObj;
                                String username = (String) votant.get(0);
                                Long vote = (Long) votant.get(1);

                                if (Objects.equals(username, DataUser.username)) {
                                    userHasVoted = true;
                                    voteOfUser = vote.intValue();

                                }
                            }
                        }
                    }
                    //Pour chaque item on verif si il a voté dedans si oui alors [True , 1 ou -1]
                    itemsVoted.add(userHasVoted);
                    itemsVote.add(voteOfUser);
                    itemsSoluce.add(newV);
                    itemsShow.add(false);
                    itemsScore.add(score);
                }

                if(items.isEmpty()){
                    items.add(" ");
                }

                // On met tout dans une seul liste (plus pratique)
                for (int i = 0; i < items.size(); i++) {
                    List<Object> item = new ArrayList<Object>();
                    item.add(items.get(i));
                    item.add(itemsScore.get(i));
                    item.add(itemsSoluce.get(i));
                    item.add(itemsShow.get(i));
                    item.add(itemsVoted.get(i));
                    item.add(itemsVote.get(i));
                    combinedList.add(item);
                }


                setRecyclerView(position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MamieActivity.this, "Erreur de chargement des données.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onItemClick(int position, Boolean itemBool) {
        Log.e("pos onItem",""+position);
        if(itemBool){
            combinedList.get(position).set(3,false);
        }
        else{
            combinedList.get(position).set(3,true);
        }
        setRecyclerView(position);

    }

    public void onScoreChange(int myPosition, int i) {

        listVotants.clear();

        //On verifie que l'utilisateur n'a pas déjà voté la même chose
        recetteRef.child((String) combinedList.get(myPosition).get(0)).child("Votant").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Boolean userFound = false;
                    Boolean voteDiff = false;
                    //Il y'a des votants, verifion que l'utilisateur n'a pas déjà fait ce vote

                    List<Object> votantsList = (List<Object>) dataSnapshot.getValue();


                    for (Object votantObj : votantsList) {
                        Log.e("Number","-------");
                        //Si c'est bien une liste (on sait jamais)
                        if (votantObj instanceof List) {
                            List<Object> votant = (List<Object>) votantObj;
                            String username = (String) votant.get(0);
                            Long vote = (Long) votant.get(1);

                            if(Objects.equals(username, DataUser.username)){
                                userFound = true;
                                //et que l'utilisateur à voté une chose différente on change son vote
                                if(vote.intValue()!=i){
                                    voteDiff = true;
                                }else{
                                    //Si il a fait le même vote on ne le met pas dans la liste voilà tout
                                    //listVotants.add(Arrays.asList(username, vote));
                                }
                            }else{
                                listVotants.add(Arrays.asList(username, vote));
                            }
                        }
                    }
                    //L'utilisateur n'a pas été trouvé
                    if(!userFound){
                        voteAdd(myPosition,i);
                    }
                    else if(voteDiff){
                        voteChange(myPosition,i);
                    }
                    //L'utilisateur est trouvé et à fait le même vote on l'enlève
                    else {
                        voteRemove(myPosition,-i);
                    }

                } else {
                    //Aucun votant on créer la list avec le premier votant
                    voteAdd(myPosition,i);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void voteAdd(int myPosition, int i) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recette");


        myRef.child((String) combinedList.get(myPosition).get(0)).child("Score").setValue((int)combinedList.get(myPosition).get(1)+i);

        listVotants.add(Arrays.asList(DataUser.username, i));
        myRef.child((String) combinedList.get(myPosition).get(0)).child("Votant").setValue(listVotants);

        loadDataFromFirebase(myPosition);

    }

    private void voteRemove(int myPosition, int i) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recette");


        myRef.child((String) combinedList.get(myPosition).get(0)).child("Score").setValue((int)combinedList.get(myPosition).get(1)+i);

        myRef.child((String) combinedList.get(myPosition).get(0)).child("Votant").setValue(listVotants);

        loadDataFromFirebase(myPosition);

    }

    private void voteChange(int myPosition, int i) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recette");



        if (combinedList.size() != 0) {
            //On attend que la value est set completement avant de continuer le code
            myRef.child((String) combinedList.get(myPosition).get(0)).child("Score").setValue((int)combinedList.get(myPosition).get(1)+2*i, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null) {
                        //On ajoute le votant avec son nouveau vote
                        listVotants.add(Arrays.asList(DataUser.username, i));
                        if (combinedList.size() != 0) {
                            myRef.child((String) combinedList.get(myPosition).get(0)).child("Votant").setValue(listVotants, new DatabaseReference.CompletionListener() {

                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        loadDataFromFirebase(myPosition);
                                    }
                                    else{
                                        Log.e("voteChange() Votant", databaseError.getMessage());
                                    }
                                }
                            });
                        }



                    } else {
                        Log.e("voteChange() Score", error.getMessage());
                    }
                }
            });
        }
    }
}


