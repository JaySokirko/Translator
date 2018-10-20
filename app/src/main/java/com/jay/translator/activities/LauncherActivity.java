package com.jay.translator.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        boolean isAppRunFirstTime = preferences.getBoolean("isAppRunFirstTime", true);

        boolean isTranslatorSelected = preferences.getBoolean("isTranslatorSelected", false);
        boolean isRememberChoice = preferences.getBoolean("isRememberChoice", false);

        if (isAppRunFirstTime) {
            startActivity(new Intent(this, SelectLanguageActivity.class));
        }

        else if (!isRememberChoice){
            startActivity(new Intent(this, SelectAppActivity.class));
        }

        else if (!isTranslatorSelected){
            startActivity(new Intent(this, SpeechActivity.class));
        } else {

            startActivity(new Intent(this, TranslatorActivity.class));
        }

        finish();

        editor.putBoolean("isAppRunFirstTime", false);
        editor.apply();
    }

}
