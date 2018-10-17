package com.jay.translator.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
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
    private SeekBar alphaBar;
    private LinearLayout substrate;
    private LinearLayout alphaBarLayout;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private int red;
    private int green;
    private int blue;
    private int alpha;

    private LayerDrawable layerDrawable;
    private LayerDrawable layerDrawable1;
    private GradientDrawable gradientDrawable;
    private GradientDrawable gradientDrawable1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_substrate);

        substrate = findViewById(R.id.substrate_color_layout);
        alphaBarLayout = findViewById(R.id.substrate_alpha_seek_bar_background);

        layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.view_rounded);
        layerDrawable1 = (LayerDrawable) getResources().getDrawable(R.drawable.view_rounded_1);
        gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.rounded_view_color);
        gradientDrawable1 = (GradientDrawable) layerDrawable1.findDrawableByLayerId(R.id.rounded_view_color_1);

        loadSettings();

        initializeRedSeekBar();

        initializeGreenSeekBar();

        initializeBlueSeekBar();

        initializeAlphaSeekBar();

        onRedSeekBarChangeListener();

        onGreenSeekBarChangeListener();

        onBlueSeekBarChangeListener();

        onAlphaBarChangeListener();
    }

    private void initializeAlphaSeekBar() {

        alphaBar = findViewById(R.id.substrate_alpha_settings);
        alpha = preferences.getInt("substrateAlpha", 255);
        alphaBar.setMax(255);
        alphaBar.incrementProgressBy(1);
        alphaBar.setProgress(alpha);
    }

    private void initializeRedSeekBar() {
        redBar = findViewById(R.id.substrate_settings_red);
        red = preferences.getInt("substrateColorRed", 255);
        redBar.setMax(255);
        redBar.incrementProgressBy(1);
        redBar.setProgress(red);
    }


    private void initializeGreenSeekBar() {
        greenBar = findViewById(R.id.substrate_settings_green);
        green = preferences.getInt("substrateColorGreen", 255);
        greenBar.setMax(255);
        greenBar.incrementProgressBy(1);
        greenBar.setProgress(green);
    }


    private void initializeBlueSeekBar() {
        blueBar = findViewById(R.id.substrate_settings_blue);
        blue = preferences.getInt("substrateColorBlue", 255);
        blueBar.setMax(255);
        blueBar.incrementProgressBy(1);
        blueBar.setProgress(blue);
    }


    private void onRedSeekBarChangeListener() {

        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                red = progress;

                gradientDrawable.setColor(Color.rgb(red, green, blue));
                gradientDrawable1.setColor(Color.rgb(red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("substrateColorRed", red);
                editor.apply();
            }
        });
    }


    private void onGreenSeekBarChangeListener() {

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                green = progress;

                gradientDrawable.setColor(Color.rgb(red, green, blue));
                gradientDrawable1.setColor(Color.rgb(red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("substrateColorGreen", green);
                editor.apply();
            }
        });
    }


    private void onBlueSeekBarChangeListener() {

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                blue = progress;

                gradientDrawable.setColor(Color.rgb(red, green, blue));
                gradientDrawable1.setColor(Color.rgb(red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("substrateColorBlue", blue);
                editor.apply();
            }
        });
    }


    private void onAlphaBarChangeListener(){

        alphaBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                alpha = progress;

                substrate.getBackground().setAlpha(alpha);
                alphaBarLayout.getBackground().setAlpha(alpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("substrateAlpha", alpha);
                editor.apply();
            }
        });
    }


    @SuppressLint("CommitPrefEdits")
    private void loadSettings() {

        preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        editor = preferences.edit();

        TextView substrateAlphaTV = findViewById(R.id.substrate_transparent_tv);
        TextView substrateColorTV = findViewById(R.id.substrate_color_tv);

        //load text style
        int style = preferences.getInt("textStyle", 0);
        substrateAlphaTV.setTypeface(null, style);
        substrateColorTV.setTypeface(null, style);

        //load text color
        int textColor = Color.rgb(
                (preferences.getInt("textColorRed", 0)),
                (preferences.getInt("textColorGreen", 0)),
                (preferences.getInt("textColorBlue", 0)));

        substrateAlphaTV.setTextColor(textColor);
        substrateColorTV.setTextColor(textColor);

        //load substrate color
        int  substrateColor = Color.rgb(
                (preferences.getInt("substrateColorRed", 255)),
                (preferences.getInt("substrateColorGreen", 255)),
                (preferences.getInt("substrateColorBlue", 255)));

        gradientDrawable.setColor(substrateColor);
        gradientDrawable1.setColor(substrateColor);
        substrate.setBackground(layerDrawable);
        alphaBarLayout.setBackground(layerDrawable1);


        //load substrate alpha
        int alpha = preferences.getInt("substrateAlpha", 255);

        substrate.getBackground().setAlpha(alpha);
        alphaBarLayout.getBackground().setAlpha(alpha);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ChoiceSettingsActivity.class));
    }
}
