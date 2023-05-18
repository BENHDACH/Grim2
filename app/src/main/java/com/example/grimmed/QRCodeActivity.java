package com.example.grimmed;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class QRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Button button = findViewById(R.id.reScan);
        button.setOnClickListener(this::onClick);

        ImageView backHome5 = findViewById(R.id.backHomeQR);
        backHome5.setOnClickListener(this::onClick);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && result.getContents() != null) {
            // Handle the QR code value here
            String qrCodeValue = result.getContents();

            nomMedocCheck(qrCodeValue);

        }
    }

    private void onClick(View v) {
        if (v.getId() == R.id.reScan) {
            restartScan();
        } else if (v.getId() == R.id.backHomeQR) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        }
    }

    private void nomMedocCheck(String nomMedoc){
        DatabaseReference medReference = FirebaseDatabase.getInstance().getReference("ListNom");
        medReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean foundMedoc = false;
                Object nom = dataSnapshot.getValue(Object.class);
                //On peut connaître le nombre de nom de médicament dans notre base
                JSONObject myMedoc = new JSONObject((Map) nom);
                //On demande de recup un Object ou Array ou String etc...
                JSONArray newV = null;

                try {

                    String val;
                    newV = myMedoc.getJSONArray("Noms");
                    //newV = myMedoc.getJSONObject(result);

                    for (int i = 0; i < newV.length(); i++) {
                        //On recup le nom coupé de chaque médicament
                        val = newV.getString(i);
                        if(val.equals(nomMedoc)){
                            foundMedoc = true;
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if(foundMedoc){
                    launchMedocActivity(nomMedoc);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void restartScan() {
        Toast.makeText(this, "Valeur inconnue", Toast.LENGTH_SHORT).show();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    private void launchMedocActivity(String nomMedoc) {
        Intent intent = new Intent(this, PageMedicActivity.class);
        intent.putExtra("nomMedoc",nomMedoc);
        startActivity(intent);
    }
}