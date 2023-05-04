package com.example.grimmed;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
@SuppressLint("ValidFragment")
public class Tab1 extends Fragment {

    private String msg;
    private String url;
    @SuppressLint("ValidFragment")
    public  Tab1(String msg, String url){
        this.msg = msg;
        this.url = url;
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView textView = view.findViewById(R.id.textViewTest);

        textView.setText(msg);

        ImageView imageView = view.findViewById(R.id.imageMedoc);
        Picasso.get().load(url).into(imageView);

        return (view);
    }

}