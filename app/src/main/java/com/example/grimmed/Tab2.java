package com.example.grimmed;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
@SuppressLint("ValidFragment")
public class Tab2 extends Fragment {

    private String effectS;

    public Tab2(String effectS) {
        this.effectS = effectS;
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView textView = view.findViewById(R.id.textViewTest);

        textView.setText(effectS);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return (view);
    }
}