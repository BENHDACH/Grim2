package com.example.grimmed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class PageLogActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText pswEditText;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_log);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Button logPage = findViewById(R.id.logPage);
        TextView redirection = findViewById(R.id.redirection);
        logPage.setOnClickListener(this::onClick);
        redirection.setOnClickListener(this::onClick);

        //Vraiment a changer pour pas avoir les même id...
        usernameEditText = findViewById(R.id.caseId);
        pswEditText = findViewById(R.id.casePasswd);


        pswEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // Do something when the user clicks "Done" or "Enter" on the keyboard
                    checkUser();
                    return true;
                }
                return false;
            }
        });
    }


    public void onClick(View v){
        if (v.getId() == R.id.logPage) {
            checkUser();
        }

        if (v.getId() == R.id.redirection) {
            Intent intent = new Intent(this, PageSignActivity.class);
            startActivity(intent);
        }
    }

    private void checkUser(){
        String pswText = pswEditText.getText().toString();
        String userText = usernameEditText.getText().toString();

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("User").child(userText);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Uniquement si il existe
                if (dataSnapshot.exists()) {
                    checkMdp(pswText,userText);
                }else{
                    incorrect();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkMdp(String pswText, String userText) {

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("User")
                .child(userText).child("password");
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Uniquement si il existe
                if (dataSnapshot.exists()) {
                    Object pswObject = dataSnapshot.getValue(Object.class);
                    JSONObject pswJson = new JSONObject((Map) pswObject);

                    String hashpsw = null;
                    String salt = null;
                    try {
                        hashpsw = pswJson.getString("mdp");
                        salt = pswJson.getString("salt");
                    } catch (JSONException e) {
                        Log.e("Not found mdp","Nothing mdp");
                    }
                    String hashedPlainTextPassword = null;
                    try {
                        hashedPlainTextPassword = DataUser.hashAndSaltPassword(pswText, salt);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    boolean isPasswordCorrect = hashedPlainTextPassword.equals(hashpsw);
                    confirmationLog(isPasswordCorrect,userText);

                }else{
                    Log.e("Error password in User","Pas de password child !");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void confirmationLog(boolean isPasswordCorrect, String userText) {
        if(isPasswordCorrect){
            DataUser.username = userText;
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }
        else{
            incorrect();
        }
    }

    private void incorrect(){
        Toast.makeText(this, "Nom d'utilisateur ou mot de passe incorrect !", Toast.LENGTH_SHORT).show();
    }
}