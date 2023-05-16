package com.example.grimmed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    JSONObject myMedoc;

    Boolean enceinteCheck;
    List<String> dangerAllergies;
    PageMedicActivity pageMedicActivity;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount, JSONObject myMedoc,
                 Boolean enceinteCheck, List<String> dangerAllergies,
                 PageMedicActivity pageMedicActivity) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        this.myMedoc = myMedoc;
        this.enceinteCheck = enceinteCheck;
        this.dangerAllergies = dangerAllergies;
        this.pageMedicActivity = pageMedicActivity;

    }


    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Tab1 tab1 = null;
                try {
                    tab1 = new Tab1(myMedoc.getString("Usage"),myMedoc.getString("Url"),myMedoc.getString("Cible"),enceinteCheck,dangerAllergies,pageMedicActivity);
                } catch (JSONException e) {
                    Log.e("Error tab1 Pager","Usage,Url, Cible non conforme");
                    throw new RuntimeException(e);
                }
                return tab1;
            case 1:
                Tab2 tab2 = null;
                try {
                    tab2 = new Tab2(myMedoc.getString("EffectS"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                return tab2;
            case 2:
                Tab3 tab3 = null;
                try {
                    tab3 = new Tab3(myMedoc.getJSONArray("Composition"),myMedoc.getString("Prix"),myMedoc.getString("ContreI"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
