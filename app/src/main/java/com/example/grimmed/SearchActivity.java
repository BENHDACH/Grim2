package com.example.grimmed;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> items = new ArrayList<String>();

    private Boolean extraCible = false;
    private Boolean checkComplet = false;
    private Bundle bundle;

    private Button bCompo;

    private Button bCible;

    private Button bNom;


    private String whichB = "Nom";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bNom = findViewById(R.id.buttonNom);
        bNom.setOnClickListener(this::onClick);

        bCible = findViewById(R.id.buttonCible);
        bCible.setOnClickListener(this::onClick);

        bCompo = findViewById(R.id.buttonCompo);
        bCompo.setOnClickListener(this::onClick);

        ImageView backHome5 = findViewById(R.id.backHome5);
        backHome5.setOnClickListener(this::clickHome);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ListNom");


        //myRef.child("Noms").push().setValue("MyTest");

        SearchView searchView = findViewById(R.id.searchView2);


        bundle = getIntent().getExtras();
        if(getIntent().hasExtra("value")){
            extraCible = bundle.getBoolean("value");
        }

        if(extraCible){
            whichB = bundle.getString("whichB");
            recupCesNoms(bundle.getString("nom"),whichB);

            if(Objects.equals(whichB, "Cible")){
                ColorStateList colorStateList1 = ContextCompat.getColorStateList(this, R.color.blue);
                bCible.setBackgroundTintList(colorStateList1);

            } else if (Objects.equals(whichB, "Compo")) {
                ColorStateList colorStateList1 = ContextCompat.getColorStateList(this, R.color.blue);
                bCompo.setBackgroundTintList(colorStateList1);
            }
            else{
                ColorStateList colorStateList1 = ContextCompat.getColorStateList(this, R.color.blue);
                bNom.setBackgroundTintList(colorStateList1);
            }
            displayResult();
        }else{
            ColorStateList colorStateList1 = ContextCompat.getColorStateList(this, R.color.blue);
            bNom.setBackgroundTintList(colorStateList1);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("SearchView", "Query submitted: " + query);
                getResultSearch(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("SearchView", "Query text changed: " + newText);
                if(!Objects.equals(newText, "")){
                    getResultSearch(newText);
                }

                return false;
            }
        });

        //firebaseSetup2();



    }

    private void onClick(View v) {
        if (v.getId() == R.id.buttonNom) {
            whichB = "Nom";

            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.boutons);
            bCible.setBackgroundTintList(colorStateList);

            ColorStateList colorStateList1 = ContextCompat.getColorStateList(this, R.color.boutons);
            bCompo.setBackgroundTintList(colorStateList1);

            ColorStateList colorStateList2 = ContextCompat.getColorStateList(this, R.color.blue);
            bNom.setBackgroundTintList(colorStateList2);

        }
        else if (v.getId() == R.id.buttonCible) {
            whichB = "Cible";
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.blue);
            bCible.setBackgroundTintList(colorStateList);

            ColorStateList colorStateList1 = ContextCompat.getColorStateList(this, R.color.boutons);
            bCompo.setBackgroundTintList(colorStateList1);

            ColorStateList colorStateList2 = ContextCompat.getColorStateList(this, R.color.boutons);
            bNom.setBackgroundTintList(colorStateList2);

        }
        else if (v.getId() == R.id.buttonCompo) {
            whichB = "Compo";

            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.boutons);
            bCible.setBackgroundTintList(colorStateList);

            ColorStateList colorStateList1 = ContextCompat.getColorStateList(this, R.color.blue);
            bCompo.setBackgroundTintList(colorStateList1);

            ColorStateList colorStateList2 = ContextCompat.getColorStateList(this, R.color.boutons);
            bNom.setBackgroundTintList(colorStateList2);

        }
    }

    private void clickHome(View v) {
        if (v.getId() == R.id.backHome5) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }
    }

    private void getResultSearch(String searchName){
        int x = searchName.length();
        checkComplet = false;

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
        checkComplet = true;
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

        SearchAdapter adapter = new SearchAdapter(items, this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    /* Une fonction pour check chaque clique sur le recycler view (appellé dans l'adapter)*/
    public void onItemClick(String item) {
        Log.e("MyName IS", item);
        //Si l'item obtenu provient du mode Nom (c'est donc un médicament)
        if(whichB.equals("Nom")){
            Intent intent = new Intent(this, PageMedicActivity.class);
            intent.putExtra("nomMedoc",item);
            startActivity(intent);
        }
        //Sinon l'item obtenu provient du mode Compo/Cible, on check si le nom est complet
        else if (checkComplet) {
            //Si oui, c'est un médicament
            Intent intent = new Intent(this, PageMedicActivity.class);
            intent.putExtra("nomMedoc",item);
            startActivity(intent);
        }
        else{
            //Si NON on le set en mode complet
            SearchView searchView = findViewById(R.id.searchView2);
            searchView.setQuery(item,true);
        }



    }

    private void firebaseSetup2(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference virgule = database.getReference("Recette");
        // Utiliser la phrase générique : "ce traitement est utilisé généralement pour +symptome
        String symptome = "Démangéaisons";
        List<String> recette = new ArrayList<>(Arrays.asList("Appliquez de l'huile d'amande douce pour apaiser les démangeaisons","Appliquez une compresse d'eau froide sur les zones touchées pour soulager les démangeaisons"));
        virgule.child(symptome).child("Traitement").setValue(recette);
        virgule.child(symptome).child("Cible").setValue("les démangeaisons");
        virgule.child(symptome).child("Score").setValue("50");
    }
    private void firebaseSetup(){


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("Medicaments");

        DatabaseReference e = database.getReference("User");

        DatabaseReference virgule = database.getReference("Recette");
        // Utiliser la phrase générique : "ce traitement est utilisé généralement pour +symptome
        String symptome = "Mal de gorge";
        List<String> recette = new ArrayList<>(Arrays.asList("Mélangez du miel, du jus de citron et de l'eau chaude. Boire cette solution apaise le mal de gorge","Gargarisez-vous avec de l'eau salée tiède plusieurs fois par jour pour aider à réduire l'inflammation et la douleur"));
        virgule.child(symptome).child("Traitement").setValue(recette);
        virgule.child(symptome).child("Cible").setValue("les maux de gorges");

        /*** /!\ Les compositions en minuscules et les cibles + nom avec une majuscule au début merci <3 ***/

        List<String> composition = new ArrayList<>(Arrays.asList("ibuprofène", "cellulose microcristalline", "stéarate de magnésium", "croscarmellose sodique"));
        //List<String> cible = new ArrayList<>(Arrays.asList("Tête", "Dos", "Gorge"));
        List<String> cible = new ArrayList<>(Arrays.asList("Menstruations"));
        String nom = "Nurofen 400mg";
        //myRef.child(userName).setValue(OBJECT);
        myRef.child(nom).child("Composition").setValue(composition);
        myRef.child(nom).child("EffectS").setValue("Douleurs abdominales, nausées, vomissements, diarrhée, vertiges, maux de tête");
        myRef.child(nom).child("Prix").setValue("4,88");
        myRef.child(nom).child("Usage").setValue("1 à 1,5 comprimés toutes les 4 à 6 heures selon la douleur");
        myRef.child(nom).child("Cible").setValue(cible);
        myRef.child(nom).child("ContreI").setValue("ulcère gastroduodénal, asthme, insuffisance cardiaque, insuffisance hépatique ou rénale sévère");
        myRef.child(nom).child("Url").setValue("https://www.pharma-gdd.com/media/cache/resolve/product_show/nurofen-400-mg-ibuprofene-douleurs-et-fievre-migraine-chez-ladulte-12-comprimes-face.jpg");
        myRef.child(nom).child("Enceinte").setValue("1");
        /*** /!\ Pas besoin de t'occuper de ce qui est en bas c'est automatisé /!\ ***/

        /** 1e etape préparation automatisé à la liste des noms (pour recherche selon noms) **/

        DatabaseReference myNameRef = database.getReference("ListNom");

        e.child("username").child("password").setValue("admin");
        e.child("username").child("allergie").setValue("ibuprofène");
        e.child("username").child("traitement").setValue("Doliprane");
        e.child("username").child("enceinte").setValue("0");

        HashMap<String, HashMap<String, String>> vaccins = new HashMap<>();
        HashMap<String, String> vaccin1 = new HashMap<>();
        vaccin1.put("nom", "Polio");
        vaccin1.put("date", "15/12/15");
        vaccins.put("vaccin1", vaccin1);

        HashMap<String, String> vaccin2 = new HashMap<>();
        vaccin2.put("nom", "Tétanos");
        vaccin2.put("date", "21/03/20");
        vaccins.put("vaccin2", vaccin2);

        e.child("username").child("vaccins").setValue(vaccins);

        HashMap<String, HashMap<String, String>> vaccinsEnfant = new HashMap<>();
        HashMap<String, String> vaccinEnfant1 = new HashMap<>();
        vaccinEnfant1.put("nom", "Polio");
        vaccinEnfant1.put("date", "15/12/15");
        vaccinsEnfant.put("vaccin1", vaccinEnfant1);

        HashMap<String, String> vaccinEnfant2 = new HashMap<>();
        vaccinEnfant2.put("nom", "Tétanos");
        vaccinEnfant2.put("date", "21/03/20");
        vaccinsEnfant.put("vaccin2", vaccinEnfant2);

        e.child("username").child("enfant").child("vaccins").setValue(vaccinsEnfant);
        e.child("username").child("enfant").child("nom").setValue("Jason");
        e.child("username").child("enfant").child("traitement").setValue("Doliprane");
        e.child("username").child("enfant").child("allergie").setValue("Aucune");





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
                Log.e("Bjr ?","");
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
                        Log.e("Ephemer",""+checkAddCible+" "+myArray);
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
                        Log.e("NomMedicCIble",""+Noms);
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
                            Log.e("L'erreur est :",""+myMedoc);
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