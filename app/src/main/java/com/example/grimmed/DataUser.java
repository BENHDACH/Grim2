package com.example.grimmed;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class DataUser {

    public static String jsonString = "{\"Composition\":[\"paracétamol\",\"amidon prégélatinisé\",\"povidone\",\"croscarmellose sodique\",\"stéarate de magnésium\"],\"CibleText\":[\"Les maux de tête\"],\"Usage\":\"1 à 3 comprimés par jour, à prendre toutes les 4 à 6 heures en fonction des symptômes\",\"Cible\":\"Tête\",\"Prix\":\"1,59\",\"ContreI\":\"Allergie au paracétamol, insuffisance hépatique sévère, grossesse\",\"EffectS\":\"Hépatite, éruption cutanée, réaction allergique\",\"Url\":\"https://i.pinimg.com/originals/44/aa/36/44aa36e69396a8b78f37f12ed4cc585d.png\",\"Enceinte\":0}";
    public static JSONObject defaultObject;

    public static String username = "Default";

    static {
        try {
            defaultObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static final int SALT_LENGTH = 16;

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(salt);
        }
        return("");
    }

    public static String hashAndSaltPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            messageDigest.update(Base64.getDecoder().decode(salt));
        }
        byte[] hashedBytes = messageDigest.digest(password.getBytes());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(hashedBytes);
        }
        return("");
    }

    public DataUser() throws JSONException {
    }
}
