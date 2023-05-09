package com.example.grimmed;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageSignActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText usernameEditText;
    EditText pswEditText;
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

            if(verificationDonnees()){
                saveDataUser();
                Intent intent = new Intent(this, BaseActivity.class);
                startActivity(intent);
            }

        }

        if (v.getId() == R.id.redirectionLog) {
            Intent intent = new Intent(this, PageLogActivity.class);
            startActivity(intent);
        }
    }



    private Boolean verificationDonnees() {
        //Etape 1 verif mail
        Boolean mailCheck = false;
        Boolean pswCheck = false;
        final Boolean[] usernameCheck = {false};

        String emailText = emailEditText.getText().toString();
        String pswText = pswEditText.getText().toString();
        String userText = usernameEditText.getText().toString();

        if (isValidEmail(emailText)) {
            mailCheck = true;
        }
        Log.e("MailValid?", "" + mailCheck);


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
        }
        Log.e("PswValid?", "" + pswCheck);

        /** Etape 3 username check (être ou ne pas être ?) **/
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("User").child(userText);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Si il n'y a pas d'user avec ce nom..
                if (!dataSnapshot.exists()) {
                    usernameCheck[0] = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return usernameCheck[0] && pswCheck && mailCheck;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void saveDataUser() {
        String emailText = emailEditText.getText().toString();
        String pswText = pswEditText.getText().toString();
        String userText = usernameEditText.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        myRef.child(userText).child("email").setValue(emailText);
        //A cacher avec un HASH !
        myRef.child(userText).child("password").setValue(pswText);

        //Il faut remplir le reste par def
        myRef.child(userText).child("traitement").setValue("");
        myRef.child(userText).child("enceinte").setValue("0");
        myRef.child(userText).child("vaccins").setValue("");
        myRef.child(userText).child("allergie").setValue("Aucune");
    }
}