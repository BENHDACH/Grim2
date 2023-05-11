package com.example.grimmed;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ProfilActivity extends AppCompatActivity {

    Boolean estEnceinte = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ImageView buttonMedoc4 = findViewById(R.id.buttonMedoc4);
        buttonMedoc4.setOnClickListener(this::onClick);

        ImageView buttonVac4 = findViewById(R.id.buttonVac4);
        buttonVac4.setOnClickListener(this::onClick);

        ImageView buttonProfil4 = findViewById(R.id.buttonProfil4);
        buttonProfil4.setOnClickListener(this::onClick);

        ImageView backHome2 = findViewById(R.id.backHome2);
        backHome2.setOnClickListener(this::onClick);

        TextView usual = findViewById(R.id.usual);
        usual.setOnClickListener(this :: onClick);

        TextView allergies = findViewById(R.id.allergies);
        allergies.setOnClickListener(this :: onClick);

        TextView child = findViewById(R.id.child);
        child.setOnClickListener(this :: onClick);

        Switch enceinte = findViewById(R.id.enc);
        enceinte.setOnClickListener(this::onClick);

        TextView name = findViewById(R.id.name);
        name.setText(DataUser.username);

        Button logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(this::onClick);

        checkPregnant();


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Profil");
        }
    }

    private void checkPregnant() {
        DatabaseReference cibleReference = FirebaseDatabase.getInstance().getReference("User")
                .child(DataUser.username);
        cibleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object nom = dataSnapshot.getValue(Object.class);
                JSONObject myInfo = new JSONObject((Map) nom);
                String newV = null;

                try {
                    newV = myInfo.getString("enceinte");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if(newV.equals("0")){
                    estEnceinte = false;
                }else{
                    estEnceinte = true;
                }

                Switch enceinte = findViewById(R.id.enc);
                if(estEnceinte){
                    enceinte.setChecked(true);
                }
                else{
                    enceinte.setChecked(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.buttonMedoc4) {
            Intent intent = new Intent(this, PageMedicActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonVac4) {
            Intent intent = new Intent(this, VaccinTimerActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.buttonProfil4) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.backHome2) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.usual) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("usual", true);
            startActivity(intent);
        }

        if (v.getId() == R.id.allergies) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("allergies", true);
            startActivity(intent);
        }

        if (v.getId() == R.id.child) {
            Intent intent = new Intent(this, ChildActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.logOut) {
            DataUser.username="Default";
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }

        if (v.getId() == R.id.enc) {
            Switch enceinte = findViewById(R.id.enc);

            if (enceinte.isChecked() && !estEnceinte) {
                AlertDialog alertDialog = new AlertDialog.Builder(ProfilActivity.this).create();
                alertDialog.setTitle("Attention !");
                alertDialog.setMessage("Certains médicaments ne sont pas compatibles avec la grossesse. Veuillez consulter votre médecin.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                changeData("1");
            }
            else{
                changeData("0");
            }
        }
    }

    private void changeData(String s) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        myRef.child(DataUser.username).child("enceinte").setValue(s);
    }

    @Override
    protected void onDestroy() {
        Log.e("deco", ""+DataUser.username);
        super.onDestroy();
    }
}