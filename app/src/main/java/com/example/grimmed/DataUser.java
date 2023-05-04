package com.example.grimmed;

import org.json.JSONException;
import org.json.JSONObject;

public class DataUser {
            public static String url = "http://test.api.catering.bluecodegames.com/menu";
            public static String idShopKey = "id_shop";
        public static String jsonString = "{\"Composition\":[\"paracétamol\",\"amidon prégélatinisé\",\"povidone\",\"croscarmellose sodique\",\"stéarate de magnésium\"],\"Usage\":\"1 à 3 comprimés par jour, à prendre toutes les 4 à 6 heures en fonction des symptômes\",\"Cible\":\"Tête\",\"Prix\":\"1,59\",\"ContreI\":\"Allergie au paracétamol, insuffisance hépatique sévère, grossesse\",\"EffectS\":\": Hépatite, éruption cutanée, réaction allergique\",\"Url\":\"https:\\/\\/www.pharmashopi.com\\/images\\/Image\\/doliprane-1000mg-comprimes-1372663029-1.jpg\",\"Enceinte\":0}";
        public static JSONObject defaultObject;

    static {
        try {
            defaultObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public DataUser() throws JSONException {
    }
}
