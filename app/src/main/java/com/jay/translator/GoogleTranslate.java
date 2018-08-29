package com.jay.translator;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GoogleTranslate {

    public static final String RUSSIAN = "ru";
    public static final String ENGLISH = "en";
    public static final String ITALIAN = "it";
    public static final String SPANISH = "es";
    public static final String GERMAN = "de";
    public static final String FRENCH = "fr";
    private static final String TAG = "TAG";

    public GoogleTranslate() {
    }


    public String translate(String text, String to, String from, Context context) {


        final StringBuilder result = new StringBuilder();

        if(text != null) {

            text = text.replaceAll("[ ]", "+");
            text = text.replace("\n","+");

            String key = "AIzaSyDRHUUlswBtl2BW6ISaZkLQTvg69Y4pecM";
            String urlStr = "https://translation.googleapis.com/language/translate/v2?q=" + text +
                    "&target=" + to + "&source=" + from + "&key=" + key;

            try {

                URL url = new URL(urlStr);

                HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();

                InputStream stream;

                if (urlConnection.getResponseCode() == 200) //success
                {
                    stream = urlConnection.getInputStream();
                } else {
                    stream = urlConnection.getErrorStream();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result.toString());

                if (element.isJsonObject()) {
                    JsonObject obj = element.getAsJsonObject();
                    if (obj.get("error") == null) {

                        return obj.get("data").getAsJsonObject().
                                get("translations").getAsJsonArray().get(0).getAsJsonObject().
                                get("translatedText").getAsString();

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}