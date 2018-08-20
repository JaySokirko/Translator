package com.jay.translator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageSettings {

    private static String language;

    public static void setLocale(String language, Context context) {

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
        language = sharedPreferences.getString("language", "en");
        setLocale(language, context);
    }


    public static String getLanguage() {
        return language;
    }
}
