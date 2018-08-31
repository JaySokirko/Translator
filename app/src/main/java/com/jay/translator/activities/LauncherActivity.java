package com.jay.translator.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.jay.translator.R;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        boolean isAppRunFirstTime = preferences.getBoolean("isAppRunFirstTime", true);

        if (isAppRunFirstTime) {
            startActivity(new Intent(this, ChoiceLanguageActivity.class));
        } else {
            startActivity(new Intent(this,TranslatorActivity.class));
        }

        finish();

        editor.putBoolean("isAppRunFirstTime",false);
        editor.apply();
    }
}
