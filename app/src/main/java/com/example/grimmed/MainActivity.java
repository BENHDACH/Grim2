package com.example.grimmed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.titleCo);

        /** Ici vous pouvez set la base de données
         La réference sera Medicaments -- modifié le nom (du médicament) et la setValue(...),
         si vous utilisez le même nom de médicament les valeurs seront écrasé pour ce médicament
         enlever les commentaires avec *** et lancer, dès la première page sa sera envoyé  **/

        /***
         FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference myRef = database.getReference("Medicaments");

         List<String> composition = new ArrayList<>(Arrays.asList("C1", "C2", "C3"));
         List<String> cible = new ArrayList<>(Arrays.asList("Tête", "Dos", "Gorge"));

         myRef.child("Nom").child("Composition").setValue(composition);
         myRef.child("Nom").child("EffectS").setValue("Description");
         myRef.child("Nom").child("Prix").setValue("3");
         myRef.child("Nom").child("Usage").setValue("Par voie orale = avale simple non ?");
         myRef.child("Nom").child("Cible").setValue(cible);
        ***/

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor("#D6D6D6")); // Récupère la couleur gris à partir des ressources de l'application
        gradientDrawable.setCornerRadius(50);

        Button log = findViewById(R.id.log);
        log.setBackground(gradientDrawable);
        log.setOnClickListener(this::onClick);

        Button sig = findViewById(R.id.sig);
        sig.setBackground(gradientDrawable);
        sig.setOnClickListener(this::onClick);


        //On change la police
        Typeface font = Typeface.createFromAsset(getAssets(), "font/MysteryQuest-Regular.ttf");
        textView.setTypeface(font);


    }


    //Fonction pour désigner l'action de cliquer sur un bouton
    public void onClick(View v) {
        if (v.getId() == R.id.log) {
            Intent intent = new Intent(this, PageLogActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.sig) {
            Intent intent = new Intent(this, PageSignActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        // On n'appelle pas le mode super (donc pas de retour possible)
    }
}