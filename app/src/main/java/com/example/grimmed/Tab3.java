package com.example.grimmed;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
@SuppressLint("ValidFragment")
public class Tab3 extends Fragment {

    private JSONArray composition;
    private String prix;
    private String contreIndic;
    public Tab3(JSONArray composition, String prix, String contreIndic) {

        this.composition = composition;
        this.prix = prix;
        this.contreIndic = contreIndic;
    }


    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView textComposition = view.findViewById(R.id.textCompo);
        TextView textContreI = view.findViewById(R.id.textContreIndic);
        TextView textPrice = view.findViewById(R.id.textPrice);
        String concatPrice = prix+"€";



        String textCompo ="";
        for(int i =0; i<composition.length();i++){
            try {
                if(i==0){
                    textCompo= (String)composition.get(i)+".";
                }
                else{
                    textCompo=(CharSequence)composition.get(i)+", "+textCompo;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        textComposition.setText(textCompo);
        textContreI.setText(contreIndic);
        textPrice.setText(concatPrice);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return (view);
    }
}