package com.jay.translator.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jay.translator.R;

public class SubstrateSettingsActivity extends AppCompatActivity {

    private SeekBar redBar;
    private SeekBar greenBar;
    private SeekBar blueBar;

    private LinearLayout substrate;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private int red;
    private int green;
    private int blue;

//    private LayerDrawable layerDrawable;
//    private GradientDrawable gradientDrawable;

    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_substrate);

        substrate = findViewById(R.id.substrate_color_layout);
        drawable = substrate.getBackground();
//        layerDrawable = (LayerDrawable) substrate.getBackground();
//        gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.rounded_view_color);

        loadSettings();

        initializeRedSeekBar();

        initializeGreenSeekBar();

        initializeBlueSeekBar();

        onRedSeekBarChangeListener();

        onGreenSeekBarChangeListener();

        onBlueSeekBarChangeListener();
    }


    private void initializeRedSeekBar() {
        redBar = findViewById(R.id.substrate_settings_red);
        red = preferences.getInt("redBarSubstrate", 216);
        redBar.setMax(255);
        redBar.incrementProgressBy(1);
        redBar.setProgress(red);
    }


    private void initializeGreenSeekBar() {
        greenBar = findViewById(R.id.substrate_settings_green);
        green = preferences.getInt("greenBarSubstrate", 216);
        greenBar.setMax(255);
        greenBar.incrementProgressBy(1);
        greenBar.setProgress(green);
    }


    private void initializeBlueSeekBar() {
        blueBar = findViewById(R.id.substrate_settings_blue);
        blue = preferences.getInt("blueBarSubstrate", 216);
        blueBar.setMax(255);
        blueBar.incrementProgressBy(1);
        blueBar.setProgress(blue);
    }


    private void onRedSeekBarChangeListener() {

        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                red = progress;

                drawable.setColorFilter(Color.rgb(progress,green,blue),PorterDuff.Mode.SRC);
                substrate.setBackground(drawable);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("redBarSubstrate", red);
                editor.apply();
            }
        });
    }


    private void onGreenSeekBarChangeListener() {

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                green = progress;
                substrate.setBackgroundColor(Color.rgb(red,progress,blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("greenBarSubstrate", green);
                editor.apply();
            }
        });
    }


    private void onBlueSeekBarChangeListener() {

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                blue = progress;
                substrate.setBackgroundColor(Color.rgb(red,green,progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("blueBarSubstrate", blue);
                editor.apply();
            }
        });
    }


    @SuppressLint("CommitPrefEdits")
    private void loadSettings() {

        preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        editor = preferences.edit();

        TextView substrateAlpha = findViewById(R.id.substrate_transparent_tv);
        TextView substrateColor = findViewById(R.id.substrate_color_tv);

        int style = preferences.getInt("textStyle", 0);
        substrateAlpha.setTypeface(null, style);
        substrateColor.setTypeface(null, style);

        int textColor = Color.rgb(
                (preferences.getInt("redBarProgress", 0)),
                (preferences.getInt("greenBarProgress", 0)),
                (preferences.getInt("blueBarProgress",0)));

        substrateAlpha.setTextColor(textColor);
        substrateColor.setTextColor(textColor);

        int substrateBackgroundColor = Color.rgb(
                (preferences.getInt("redBarSubstrate",255)),
                (preferences.getInt("greenBarSubstrate",255)),
                (preferences.getInt("blueBarSubstrate",255)));

//        substrateBackground.setColorFilter(substrateBackgroundColor,PorterDuff.Mode.SRC);
//        substrate.setBackground(substrateBackground);
    }
}
