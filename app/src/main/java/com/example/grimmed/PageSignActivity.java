package com.example.grimmed;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageSignActivity extends AppCompatActivity  {

    EditText emailEditText;
    EditText usernameEditText;
    EditText pswEditText;

    Boolean mailCheck = false;
    Boolean pswCheck = false;
    final Boolean[] usernameCheck = {false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_sign);

        Button logPage = findViewById(R.id.sigPage);
        TextView redirectionLog = findViewById(R.id.redirectionLog);
        logPage.setOnClickListener(this::onClick);
        redirectionLog.setOnClickListener(this::onClick);

        emailEditText = findViewById(R.id.caseEmail);
        usernameEditText = findViewById(R.id.caseId);
        pswEditText = findViewById(R.id.casePasswd);
    }

    public void onClick(View v){
        if (v.getId() == R.id.sigPage) {
            verificationDonnees();
        }

        if (v.getId() == R.id.redirectionLog) {
            Intent intent = new Intent(this, PageLogActivity.class);
            startActivity(intent);
        }
    }



    private void verificationDonnees() {
        //Etape 1 verif mail

        String emailText = emailEditText.getText().toString();
        String pswText = pswEditText.getText().toString();
        String userText = usernameEditText.getText().toString();

        if (isValidEmail(emailText)) {
            mailCheck = true;
        }
        Log.e("MailValid?", "" + mailCheck);


        /*
        //A t-il au moins 10 characters ?
        if (pswText.length() >= 10) {
            //A t-il au moins 1 chiffre ?
            if (pswText.matches(".*\\d.*")) {
                //A t-il au moins 1 lettre majuscule ?
                for (int i = 0; i < pswText.length(); i++) {
                    if (Character.isUpperCase(pswText.charAt(i))) {
                        pswCheck = true;
                    }
                }
            }
        }*/
        pswCheck =true;
        Log.e("PswValid?", "" + pswCheck);

        /** Etape 3 username check (être ou ne pas être ?) **/
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("User").child(userText);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Si il n'y a pas d'user avec ce nom..
                if (!dataSnapshot.exists()) {
                    usernameCheck[0] = true;
                }

                try {
                    afterCheck();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void afterCheck() throws NoSuchAlgorithmException {
        if(usernameCheck[0] && mailCheck && pswCheck){
            saveDataUser();
        }
        else{
            Toast.makeText(this, "Manque un truc bitch", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void saveDataUser() throws NoSuchAlgorithmException {
        String emailText = emailEditText.getText().toString();
        String pswText = pswEditText.getText().toString();
        String userText = usernameEditText.getText().toString();

        //Hash&Salt Password
        String salt = DataUser.generateSalt();
        String hashedPassword = DataUser.hashAndSaltPassword(pswText, salt);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        myRef.child(userText).child("email").setValue(emailText);
        //On place dans la hash et le salt pour recup le bon mdp
        myRef.child(userText).child("password").child("mdp").setValue(hashedPassword);
        myRef.child(userText).child("password").child("salt").setValue(salt);

        //Il faut remplir le reste par def
        myRef.child(userText).child("traitement").setValue("");
        myRef.child(userText).child("enceinte").setValue("0");
        myRef.child(userText).child("vaccins").setValue("");
        myRef.child(userText).child("allergie").setValue("Aucune");

        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
    }





}