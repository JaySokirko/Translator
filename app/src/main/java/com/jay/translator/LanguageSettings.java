package com.jay.translator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

public class LanguageSettings {


    private static final String TAG = "TAG";
    public static String LANGUAGE;

    public static void setLocale(String language, Context context) {

//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//
//        Configuration configuration = context.getResources().getConfiguration();
//        configuration.setLocale(locale);
//
//        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        context.createConfigurationContext(configuration);
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        SharedPreferences.Editor editor = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();

        editor.putString("language", language);

        editor.apply();
    }


    public static void loadLocale(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "en");
        setLocale(language, context);

        LANGUAGE = language;
    }

}
