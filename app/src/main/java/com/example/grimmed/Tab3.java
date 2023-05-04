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

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
@SuppressLint("ValidFragment")
public class Tab3 extends Fragment {

    private JSONArray composition;
    public Tab3(JSONArray composition) {
        this.composition = composition;
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView textView = view.findViewById(R.id.textViewTest);
        String textCompo ="";
        for(int i =0; i<composition.length();i++){
            try {
                textCompo=(CharSequence)composition.get(i)+","+textCompo;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        textView.setText(textCompo);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return (view);
    }
}