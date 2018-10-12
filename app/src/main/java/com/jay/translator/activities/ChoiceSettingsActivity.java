package com.jay.translator.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jay.translator.R;

public class ChoiceSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";

    private SharedPreferences preferences;
    private LinearLayout startTextSettings;
    private LinearLayout startSubstrateSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView backgroundImage = findViewById(R.id.settings_background);

        startTextSettings = findViewById(R.id.set_text_style);
        startTextSettings.setOnClickListener(this);

        startSubstrateSettings = findViewById(R.id.start_substrate_settings);
        startSubstrateSettings.setOnClickListener(this);

        preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);

        loadSettings();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.set_text_style:
                startActivity(new Intent(this, TextSettingsActivity.class));
                finish();
                break;

            case R.id.start_substrate_settings:
                startActivity(new Intent(this, SubstrateSettingsActivity.class));
                finish();
                break;
        }
    }


    private void loadSettings() {

        TextView textSettingsTV = findViewById(R.id.settings_text_tv);
        TextView substrateSettingsTV = findViewById(R.id.settings_substrate_tv);
        TextView backgroundSettingsTV = findViewById(R.id.settings_background_tv);
        TextView navigationSettingsTV = findViewById(R.id.settings_navigation_tv);
        TextView appSettingsTV = findViewById(R.id.settings_app_tv);

        int red = preferences.getInt("redBarProgress", 0);
        int green = preferences.getInt("greenBarProgress", 0);
        int blue = preferences.getInt("blueBarProgress", 0);

        textSettingsTV.setTextColor(Color.rgb(red, green, blue));
        substrateSettingsTV.setTextColor(Color.rgb(red, green, blue));
        backgroundSettingsTV.setTextColor(Color.rgb(red, green, blue));
        navigationSettingsTV.setTextColor(Color.rgb(red, green, blue));
        appSettingsTV.setTextColor(Color.rgb(red, green, blue));

        int style = preferences.getInt("textStyle", 0);

        textSettingsTV.setTypeface(null, style);
        substrateSettingsTV.setTypeface(null, style);
        backgroundSettingsTV.setTypeface(null, style);
        navigationSettingsTV.setTypeface(null, style);
        appSettingsTV.setTypeface(null, style);

        int  substrateColor = Color.rgb(
                (preferences.getInt("redBarSubstrate", 255)),
                (preferences.getInt("greenBarSubstrate", 255)),
                (preferences.getInt("blueBarSubstrate", 255)));

        startSubstrateSettings.setBackgroundColor(substrateColor);
        startTextSettings.setBackgroundColor(substrateColor);

        int substrateAlpha = preferences.getInt("substrateAlpha",255);
        startTextSettings.getBackground().setAlpha(substrateAlpha);
        startSubstrateSettings.getBackground().setAlpha(substrateAlpha);
    }
}
