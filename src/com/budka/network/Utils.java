package com.budka.network;

import android.content.res.Resources;
import android.util.JsonWriter;
import android.util.Log;

import com.example.cat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Raaw on 7/21/2016.
 */
public class Utils {

    public static String getHash(String str)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String Pack(String method, String data) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("method", method);
        json.put("value", data);
        String js = json.toString();
        try {
            return Utils.getHash(js) + js;
        } catch (Exception e) {
            Log.e("Voice::Utils", "Oh no! : " + e.toString());
            return null;
        }

    }

    public static String getUrl(Resources resources, String relativeUrl) {
        return resources.getString(R.string.schema)+"://"+resources.getString(R.string.host) + "/" + relativeUrl;
    }
}
