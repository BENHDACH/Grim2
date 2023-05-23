package com.example.grimmed;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
@SuppressLint("ValidFragment")
public class Tab1 extends Fragment {

    private String msg;
    private JSONArray cibleMsg;
    private String url;

    List<String> dangerAllergies;
    List<String> dangerEnfant;
    private Boolean enceinteCheck;
    private Boolean ordoCheck;
    private PageMedicActivity pageMedicActivity;

    @SuppressLint("ValidFragment")
    public  Tab1(String msg, String url, JSONArray cibleMsg, Boolean enceinteCheck,
                 List<String> dangerAllergies, List<String> dangerEnfant, PageMedicActivity pageMedicActivity, Boolean ordoCheck){
        this.msg = msg;
        this.url = url;
        this.cibleMsg = cibleMsg;
        this.enceinteCheck = enceinteCheck;
        this.dangerAllergies = dangerAllergies;
        this.dangerEnfant = dangerEnfant;
        this.pageMedicActivity = pageMedicActivity;
        this.ordoCheck = ordoCheck;
    }



    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView textView = view.findViewById(R.id.textCompo);
        TextView secondaryText = view.findViewById(R.id.textSecondEffect);
        TextView alertText = view.findViewById(R.id.alerte);
        TextView ordoText = view.findViewById(R.id.ordo);
        String concateur = "";
        for(int j=0;j<cibleMsg.length();j++){
            try {
                concateur = concateur + cibleMsg.get(j);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        secondaryText.setText(concateur);
        int colorRes = R.color.rougeAlerte;
        int textColor = ContextCompat.getColor(pageMedicActivity, colorRes);
        alertText.setTextColor(textColor);

        if (enceinteCheck && !dangerAllergies.isEmpty() && !dangerEnfant.isEmpty()) {
            alertText.setText("Attention ce médicament est dangereux pour les femmes enceintes et vous et l'un de vos enfant possèdez des allergies à sa composition ! ");
        } else if (enceinteCheck && !dangerAllergies.isEmpty()) {
            alertText.setText("Attention ce médicament est dangereux pour les femmes enceintes et vous possèdez des allergies à sa composition ! ");
        } else if (enceinteCheck && !dangerEnfant.isEmpty()) {
            alertText.setText("Attention ce médicament est dangereux pour les femmes enceintes et l'un de vos enfants a des allergies dans sa composition ! ");
        } else if (!dangerEnfant.isEmpty() && !dangerAllergies.isEmpty()) {
            alertText.setText("Attention vous et l'un de vos enfants possèdez des allergies à sa composition ! ");
        }else if (enceinteCheck) {
            alertText.setText("Attention contre indiquez pour les femmes enceintes !");
        } else if (!dangerAllergies.isEmpty()) {
            alertText.setText("Attention vous avez des allergies dans ce médicament !");
        } else if (!dangerEnfant.isEmpty()) {
            alertText.setText("Attention votre enfant a des allergies dans ce médicament !");
        } else{
            alertText.setText("");
        }

        if(ordoCheck){
            ordoText.setText("Ce médicament nécessite une ordonnance.");
        }else{
            ordoText.setText("");
        }


        String concatMsgUsage = "";
        for(int i=0;i<msg.length();i++){
            if(msg.charAt(i) == '@'){
                concatMsgUsage = concatMsgUsage+'\n';
            }
            else{
                concatMsgUsage = concatMsgUsage+msg.charAt(i);
            }
        }
        textView.setText(concatMsgUsage);



        ImageView imageView = view.findViewById(R.id.imageMedoc);
        Picasso.get().load(url).into(imageView);

        return (view);
    }

}