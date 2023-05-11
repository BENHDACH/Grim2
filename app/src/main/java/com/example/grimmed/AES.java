package com.example.grimmed;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AES {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    //Oui c'est un scandale mais on a pas de KMS pour conservé la secret key
    private static final String SECRET_KEY = "6c3ea0477630ce21a2ce334aa0e19d785ca28fd3097d981d44f6e7f8b671ec39";
    private static final String IV = "0000000000000000";

    public static String encrypt(String value) throws Exception {
        SecretKeySpec secretKeySpec = generateKey(SECRET_KEY);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(encryptedValue);
        }
        else{
            return(null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String value) throws Exception {
        SecretKeySpec secretKeySpec = generateKey(SECRET_KEY);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(value));
        return new String(decryptedValue, StandardCharsets.UTF_8);
    }

    private static SecretKeySpec generateKey(String secretKey) throws NoSuchAlgorithmException {
        byte[] key = (secretKey).getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, "AES");
    }
}

