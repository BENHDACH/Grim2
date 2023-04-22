package com.example.grimmed;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> items = new ArrayList<String>();

    private String whichB = "Nom";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ListNom");


        //myRef.child("Noms").push().setValue("MyTest");

        SearchView searchView = findViewById(R.id.searchView2);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("SearchView", "Query submitted: " + query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("SearchView", "Query text changed: " + newText);
                if(!Objects.equals(newText, "")){
                    getResultSearch(newText);
                }
                /* permet de set la valeur sur la barre de recherche
                if(newText.equals("Do")){
                    String suggestedText = "Doliprane"; // Replace with your own logic to suggest text
                    searchView.setQuery(suggestedText, false);
                }*/

                return false;
            }
        });

        firebaseSetup();

        Button bNom = findViewById(R.id.buttonNom);
        bNom.setOnClickListener(this::onClick);

        Button bCible = findViewById(R.id.buttonCible);
        bCible.setOnClickListener(this::onClick);

        Button bCompo = findViewById(R.id.buttonCompo);
        bCompo.setOnClickListener(this::onClick);


    }

    private void onClick(View v) {
        if (v.getId() == R.id.buttonNom) {
            whichB = "Nom";
        }
        else if (v.getId() == R.id.buttonCible) {
            whichB = "Cible";
        }
        else if (v.getId() == R.id.buttonCompo) {
            whichB = "Compo";
        }
    }

    private void getResultSearch(String searchName){
        int x = searchName.length();

        items.clear();
        //Cas simple on cherche par nom

        if(whichB.equals("Nom")){
            DatabaseReference medReference = FirebaseDatabase.getInstance().getReference("ListNom");
            medReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object nom = dataSnapshot.getValue(Object.class);
                    //On peut connaître le nombre de nom de médicament dans notre base
                    JSONObject myMedoc = new JSONObject((Map) nom);
                    //On demande de recup un Object ou Array ou String etc...
                    JSONArray newV = null;

                    try {
                        //Le "Nom" sera une variable passer en para depuis searchActivity
                        //On traitera pour avoir la première lettre en Majuscule
                        String result = Character.toUpperCase(searchName.charAt(0)) + searchName.substring(1);
                        // Output: message --> Message
                        String val;
                        newV = myMedoc.getJSONArray("Noms");
                        //newV = myMedoc.getJSONObject(result);

                        for (int i = 0; i < newV.length(); i++) {
                            //On recup le nom coupé de chaque médicament
                            val = newV.getString(i).substring(0, Math.min(x, newV.getString(i).length()));
                            if(val.equals(result)){
                                checkAddingItem(newV.getString(i));
                            }
                        }
                        displayResult();
                        // Log.e("Here is my NewV","h:"+newV.getString(0));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    // Use the value of nom here
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        if(whichB.equals("Cible")){
            //Cas on cherche par cible
            DatabaseReference cibleReference = FirebaseDatabase.getInstance().getReference("Cible");
            cibleReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object nom = dataSnapshot.getValue(Object.class);
                    //On peut connaître le nombre de nom de médicament dans notre base
                    JSONObject myMedoc = new JSONObject((Map) nom);
                    //On demande de recup un Object ou Array ou String etc...
                    JSONArray newV = null;

                    try {
                        //Le "Nom" sera une variable passer en para depuis searchActivity
                        //On traitera pour avoir la première lettre en Majuscule
                        String result = Character.toUpperCase(searchName.charAt(0)) + searchName.substring(1);
                        // Output: message --> Message
                        String val;
                        newV = myMedoc.getJSONArray("Noms");
                        //newV = myMedoc.getJSONObject(result);

                        for (int i = 0; i < newV.length(); i++) {
                            //On recup le nom coupé de chaque médicament
                            val = newV.getString(i).substring(0, Math.min(x, newV.getString(i).length()));
                            if(val.equals(result)){
                                checkAddingItem(newV.getString(i));
                                if(newV.getString(i).length()==result.length()){
                                    recupCesNoms(newV.getString(i),whichB);
                                    return;
                                }
                            }
                        }
                        displayResult();
                        // Log.e("Here is my NewV","h:"+newV.getString(0));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    // Use the value of nom here
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if(whichB.equals("Compo")){
            //Cas on cherche par cible
            DatabaseReference compoReference = FirebaseDatabase.getInstance().getReference("Composition");
            compoReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object nom = dataSnapshot.getValue(Object.class);
                    //On peut connaître le nombre de nom de médicament dans notre base
                    JSONObject myMedoc = new JSONObject((Map) nom);
                    //On demande de recup un Object ou Array ou String etc...
                    JSONArray newV = null;

                    try {
                        //Le "Nom" sera une variable passer en para depuis searchActivity
                        //On traitera pour avoir la première lettre en Majuscule
                        String result = Character.toLowerCase(searchName.charAt(0)) + searchName.substring(1);
                        // Output: message --> Message
                        String val;
                        newV = myMedoc.getJSONArray("Noms");
                        //newV = myMedoc.getJSONObject(result);

                        for (int i = 0; i < newV.length(); i++) {
                            //On recup le nom coupé de chaque médicament
                            val = newV.getString(i).substring(0, Math.min(x, newV.getString(i).length()));
                            if(val.equals(result)){
                                checkAddingItem(newV.getString(i));
                                //Si qqch s'en approche et est de même longueur donc sont les même
                                if(newV.getString(i).length()==result.length()){
                                    recupCesNoms(newV.getString(i),whichB);
                                    return;
                                }
                            }
                        }
                        displayResult();
                        // Log.e("Here is my NewV","h:"+newV.getString(0));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void recupCesNoms(String nomComplet, String compoOrCible) {
        items.clear();
        if(compoOrCible.equals("Cible")){
            DatabaseReference cibleReference = FirebaseDatabase.getInstance().getReference("Cible").child(nomComplet);
            cibleReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object nom = dataSnapshot.getValue(Object.class);
                    JSONObject myMedoc = new JSONObject((Map) nom);
                    JSONArray newV = null;
                    try {
                        newV = myMedoc.getJSONArray("Noms");
                        for (int i = 0; i < newV.length(); i++) {
                            checkAddingItem(newV.getString(i));
                        }
                        displayResult();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else{
            DatabaseReference compoReference = FirebaseDatabase.getInstance().getReference("Composition").child(nomComplet);
            compoReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object nom = dataSnapshot.getValue(Object.class);
                    JSONObject myMedoc = new JSONObject((Map) nom);
                    JSONArray newV = null;
                    try {
                        newV = myMedoc.getJSONArray("Noms");
                        for (int i = 0; i < newV.length(); i++) {
                            checkAddingItem(newV.getString(i));
                        }
                        displayResult();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

    }

    private void checkAddingItem(String ajout) {
        //On check si il y est déjà
        boolean mycheck = true;
        for (int i = 0; i < items.size(); i++) {
            if(Objects.equals(items.get(i), ajout)){
                 mycheck = !mycheck;
            }
        }
        if(mycheck){
            items.add(ajout);
        }
    }

    private void displayResult() {
        recyclerView = findViewById(R.id.recyclerSearch);

        SearchAdapter adapter = new SearchAdapter(items);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void firebaseSetup(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Medicaments");

        /*** /!\ Les compositions en minuscules et les cibles + nom avec une majuscule au début merci <3 ***/
        
        List<String> composition = new ArrayList<>(Arrays.asList("paracétamol", "povidone K 30", "monoglycérides diacétyles", "arôme", "maltodextrine","gomme arabique" ,"eau"  ));
        //List<String> cible = new ArrayList<>(Arrays.asList("Tête", "Dos", "Gorge"));
        List<String> cible = new ArrayList<>(Arrays.asList("Tête"));
        String nom = "Doliprane 200mg poudre pour solution buvable";

        myRef.child(nom).child("Composition").setValue(composition);
        myRef.child(nom).child("EffectS").setValue(": Affectations du systèmes immunitaires, de la peau, des tissus sous-cutanés. Un sur dosage est dangereux et peut causer des érythèmes, des urticaires ou encore des chocs anaphylactiques");
        myRef.child(nom).child("Prix").setValue("2,45");
        myRef.child(nom).child("Usage").setValue("Enfant de 11 à 25kg, 1 sachet toutes les 6 heures (4 max par jours)");
        myRef.child(nom).child("Cible").setValue(cible.get(0));
        myRef.child(nom).child("ContreI").setValue("Insuffisance hépatocellulaire sévère");
        myRef.child(nom).child("Url").setValue("https://www.medicament.com/8276/doliprane-200mg-12-sachets-dose-pour-solution-buvable.jpg");

        /*** /!\ Pas besoin de t'occuper de ce qui est en bas c'est automatisé /!\ ***/

        /** 1e etape préparation automatisé à la liste des noms (pour recherche selon noms) **/
        DatabaseReference myNameRef = database.getReference("ListNom");
        myNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean checkAddCible = true;
                //Si il existe pas on le creer
                if (!dataSnapshot.exists()) {
                    List<String> nomFirstMedic = new ArrayList<>(Arrays.asList(nom));
                    myNameRef.child("Noms").setValue(nomFirstMedic);

                }
                //Si il existe
                else{
                    Object nomObject = dataSnapshot.getValue(Object.class);
                    //On peut connaître le nombre de nom de médicament dans notre base
                    JSONObject myMedoc = new JSONObject((Map) nomObject);
                    //On demande de recup un Object ou Array ou String etc...
                    JSONArray myArray = null;
                    try {
                        myArray = myMedoc.getJSONArray("Noms");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    //On parcours la liste

                    for (int i = 0; i < myArray.length(); i++) {
                        //On check si il existe dans la liste
                        try {
                            if(myArray.get(i).equals(nom)){
                                checkAddCible = !checkAddCible;
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //Après avoir test si il n'existe pas on l'ajoute
                    if(checkAddCible){
                        myArray.put(nom);

                        FirebaseLister medicationList = new FirebaseLister();
                        List<String> Noms = new ArrayList<>();
                        for (int i = 0; i < myArray.length(); i++) {
                            try {
                                Noms.add(myArray.getString(i));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        medicationList.setMedications(Noms);
                        myNameRef.child("Noms").setValue(medicationList.getMedications());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Error: " + databaseError.getMessage());
            }
        });


        /** 2e etape préparation automatisé à la liste des cibles (pour recherche selon cible) **/
        DatabaseReference myCibleRef = database.getReference("Cible");

        myCibleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean checkAddCible = true;
                //Si il existe pas on le creer
                if (!dataSnapshot.exists()) {
                    myCibleRef.child("Noms").setValue(cible);
                }
                //Si il existe
                else{
                    Object nomObject = dataSnapshot.getValue(Object.class);
                    //On peut connaître le nombre de nom de médicament dans notre base
                    JSONObject myMedoc = new JSONObject((Map) nomObject);
                    //On demande de recup un Object ou Array ou String etc...
                    JSONArray myArray = null;
                    JSONArray ephemer = new JSONArray();
                    try {
                        myArray = myMedoc.getJSONArray("Noms");
                    } catch (JSONException e) {
                        myCibleRef.child("Noms").setValue(cible);
                        throw new RuntimeException(e);
                    }
                    //On parcours la liste

                    for (int i = 0; i < cible.size(); i++) {
                        //On check si il existe dans la liste
                        for (int y = 0; y < myArray.length();y++){
                            try {
                                if(myArray.get(y).equals(cible.get(i))){
                                    checkAddCible = false;
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //Si la composition en i n'est pas présente on la save
                        if(checkAddCible){
                            ephemer.put(cible.get(i));
                        }
                        //On retest avec un autre
                        checkAddCible = true;
                    }
                    //Si on a qqch a ajouter
                    if(ephemer.length()!=0){
                        for(int i = 0; i < ephemer.length(); i++){
                            try {
                                myArray.put(ephemer.get(i));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        FirebaseLister medicationList = new FirebaseLister();
                        List<String> Noms = new ArrayList<>();
                        for (int i = 0; i < myArray.length(); i++) {
                            try {
                                Noms.add(myArray.getString(i));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        medicationList.setMedications(Noms);
                        myCibleRef.child("Noms").setValue(medicationList.getMedications());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Error: " + databaseError.getMessage());
            }
        });

        //On parcours toutes les cibles et on les ajoutes si non fait avant
        /*** cible à mettre en liste seul au dessus ***/
        for (int j = 0; j < cible.size(); j++){
            //Les cibles avec une majuscule au début merci :)
            String targetName = cible.get(j);
            DatabaseReference testreference = myCibleRef.child(targetName);
            testreference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean checkAddCible = true;
                    //Si il existe pas on le creer
                    if (!dataSnapshot.exists()) {
                        List<String> nomFirstMedic = new ArrayList<>(Arrays.asList(nom));
                        testreference.child("Noms").setValue(nomFirstMedic);


                    }
                    //Si il existe
                    else{
                        Object nomObject = dataSnapshot.getValue(Object.class);
                        //On peut connaître le nombre de nom de médicament dans notre base
                        JSONObject myMedoc = new JSONObject((Map) nomObject);
                        //On demande de recup un Object ou Array ou String etc...
                        JSONArray myArray = null;

                        try {
                            myArray = myMedoc.getJSONArray("Noms");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //On parcours la liste

                        for (int i = 0; i < myArray.length(); i++) {
                            //On check si il existe dans la liste
                            try {
                                if(myArray.get(i).equals(nom)){
                                    Log.e("Exist!!","MEOOE");
                                    checkAddCible = !checkAddCible;
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //Après avoir test si il n'existe pas on l'ajoute
                        if(checkAddCible){
                            myArray.put(nom);

                            FirebaseLister medicationList = new FirebaseLister();
                            List<String> Noms = new ArrayList<>();
                            for (int i = 0; i < myArray.length(); i++) {
                                try {
                                    Noms.add(myArray.getString(i));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            medicationList.setMedications(Noms);
                            Log.e("----",""+medicationList);
                            testreference.child("Noms").setValue(medicationList.getMedications());

                            //testreference.child(targetName).child("Noms").setValue(myArray);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("TAG", "Error: " + databaseError.getMessage());
                }
            });
        }

        /** 3e etape préparation automatisé à la liste des compositions (pour recherche selon compo) **/
        DatabaseReference myCompoRef = database.getReference("Composition");


        myCompoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean checkAddCible = true;
                //Si il existe pas on le creer
                if (!dataSnapshot.exists()) {
                    myCompoRef.child("Noms").setValue(composition);
                }
                //Si il existe
                else{
                    Object nomObject = dataSnapshot.getValue(Object.class);
                    //On peut connaître le nombre de nom de médicament dans notre base
                    JSONObject myMedoc = new JSONObject((Map) nomObject);
                    //On demande de recup un Object ou Array ou String etc...
                    JSONArray myArray = null;
                    JSONArray ephemer = new JSONArray();
                    try {
                        myArray = myMedoc.getJSONArray("Noms");
                    } catch (JSONException e) {
                        myCompoRef.child("Noms").setValue(composition);
                        throw new RuntimeException(e);
                    }
                    //On parcours la liste

                    for (int i = 0; i < composition.size(); i++) {
                        //On check si il existe dans la liste
                        for (int y = 0; y < myArray.length();y++){
                            try {
                                if(myArray.get(y).equals(composition.get(i))){
                                    checkAddCible = false;
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //Si la composition en i n'est pas présente on la save
                        if(checkAddCible){
                            ephemer.put(composition.get(i));
                        }
                        //On retest avec un autre
                        checkAddCible = true;
                    }
                    //Si on a qqch a ajouter
                    if(ephemer.length()!=0){
                        for(int i = 0; i < ephemer.length(); i++){
                            try {
                                myArray.put(ephemer.get(i));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        FirebaseLister medicationList = new FirebaseLister();
                        List<String> Noms = new ArrayList<>();
                        for (int i = 0; i < myArray.length(); i++) {
                            try {
                                Noms.add(myArray.getString(i));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        medicationList.setMedications(Noms);
                        myCompoRef.child("Noms").setValue(medicationList.getMedications());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Error: " + databaseError.getMessage());
            }
        });

        //On parcours toutes les cibles et on les ajoutes si non fait avant
        /*** cible à mettre en liste seul au dessus ***/
        for (int j = 0; j < composition.size(); j++){
            //Les cibles avec une majuscule au début merci :)
            String targetName = composition.get(j);
            DatabaseReference testreference = myCompoRef.child(targetName);
            testreference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean checkAddCible = true;
                    //Si il existe pas on le creer
                    if (!dataSnapshot.exists()) {
                        List<String> nomFirstMedic = new ArrayList<>(Arrays.asList(nom));
                        testreference.child("Noms").setValue(nomFirstMedic);

                    }
                    //Si il existe
                    else{
                        Object nomObject = dataSnapshot.getValue(Object.class);
                        //On peut connaître le nombre de nom de médicament dans notre base
                        JSONObject myMedoc = new JSONObject((Map) nomObject);
                        //On demande de recup un Object ou Array ou String etc...
                        JSONArray myArray = null;

                        try {
                            myArray = myMedoc.getJSONArray("Noms");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //On parcours la liste

                        for (int i = 0; i < myArray.length(); i++) {
                            //On check si il existe dans la liste
                            try {
                                if(myArray.get(i).equals(nom)){
                                    checkAddCible = !checkAddCible;
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //Après avoir test si il n'existe pas on l'ajoute
                        if(checkAddCible){
                            myArray.put(nom);

                            FirebaseLister medicationList = new FirebaseLister();
                            List<String> Noms = new ArrayList<>();
                            for (int i = 0; i < myArray.length(); i++) {
                                try {
                                    Noms.add(myArray.getString(i));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            medicationList.setMedications(Noms);
                            Log.e("----",""+medicationList);
                            testreference.child("Noms").setValue(medicationList.getMedications());

                            //testreference.child(targetName).child("Noms").setValue(myArray);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("TAG", "Error: " + databaseError.getMessage());
                }
            });
        }

    }



    public class FirebaseLister {
        private List<String> Noms;

        public FirebaseLister() {
            // Required empty constructor for Firebase serialization
        }

        public FirebaseLister(List<String> Noms) {
            this.Noms = Noms;
        }

        public List<String> getMedications() {
            return Noms;
        }

        public void setMedications(List<String> Noms) {
            this.Noms = Noms;
        }
    }
}