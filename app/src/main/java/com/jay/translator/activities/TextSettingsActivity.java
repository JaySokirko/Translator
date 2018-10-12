package com.jay.translator.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jay.translator.R;

public class TextSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textStyleTV;
    private TextView textColorTV;
    private TextView textExampleTV;
    private Button styleNormalBtn;
    private Button styleBoldBtn;
    private Button styleItalicBtn;
    private SeekBar redBar;
    private SeekBar greenBar;
    private SeekBar blueBar;

    private int redBarProgress;
    private int greenBarProgress;
    private int blueBarProgress;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_text);

        textStyleTV = findViewById(R.id.set_text_style);
        textColorTV = findViewById(R.id.set_text_color);
        textExampleTV = findViewById(R.id.text_settings_example);

        styleNormalBtn = findViewById(R.id.text_style_normal);
        styleNormalBtn.setOnClickListener(this);

        styleBoldBtn = findViewById(R.id.text_style_bold);
        styleBoldBtn.setOnClickListener(this);

        styleItalicBtn = findViewById(R.id.text_style_italic);
        styleItalicBtn.setOnClickListener(this);

        preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        editor = preferences.edit();

        initializeRedSeekBar();

        initializeGreenSeekBar();

        initializeBlueSeekBar();

        onRedSeekBarChangeListener();

        onGreenSeekBarChangeListener();

        onBlueSeekBarChangeListener();

        loadSettings();
    }


    private void initializeRedSeekBar() {
        redBar = findViewById(R.id.text_settings_red);
        redBarProgress = preferences.getInt("redBarProgress", 0);
        redBar.setMax(255);
        redBar.incrementProgressBy(1);
        redBar.setProgress(redBarProgress);
    }


    private void initializeGreenSeekBar() {
        greenBar = findViewById(R.id.text_settings_green);
        greenBarProgress = preferences.getInt("greenBarProgress", 0);
        greenBar.setMax(255);
        greenBar.incrementProgressBy(1);
        greenBar.setProgress(greenBarProgress);
    }


    private void initializeBlueSeekBar() {
        blueBar = findViewById(R.id.text_settings_blue);
        blueBarProgress = preferences.getInt("blueBarProgress", 0);
        blueBar.setMax(255);
        blueBar.incrementProgressBy(1);
        blueBar.setProgress(blueBarProgress);
    }


    private void onRedSeekBarChangeListener() {

        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textColorTV.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));
                textStyleTV.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));
                styleNormalBtn.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));
                styleBoldBtn.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));
                styleItalicBtn.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));
                textExampleTV.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));

                redBarProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("redBarProgress", redBarProgress);
                editor.apply();
            }
        });
    }


    private void onGreenSeekBarChangeListener() {

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textColorTV.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));
                textStyleTV.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));
                styleNormalBtn.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));
                styleBoldBtn.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));
                styleItalicBtn.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));
                textExampleTV.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));

                greenBarProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("greenBarProgress", greenBarProgress);
                editor.apply();
            }
        });
    }


    private void onBlueSeekBarChangeListener() {

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textColorTV.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));
                textStyleTV.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));
                styleNormalBtn.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));
                styleBoldBtn.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));
                styleItalicBtn.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));
                textExampleTV.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));

                blueBarProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("blueBarProgress", blueBarProgress);
                editor.apply();
            }
        });
    }


    private void loadSettings() {

        textColorTV.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));
        textStyleTV.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));
        styleNormalBtn.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));
        styleBoldBtn.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));
        styleItalicBtn.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));
        textExampleTV.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));

        textColorTV.setTypeface(null,preferences.getInt("textStyleTV",0));
        textStyleTV.setTypeface(null,preferences.getInt("textStyleTV",0));
        textExampleTV.setTypeface(null,preferences.getInt("textStyleTV",0));

        LinearLayout substrateLayout = findViewById(R.id.text_settings_substrate_background);

        LayerDrawable layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.view_rounded);
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.rounded_view_color);

        int substrateColor = Color.rgb(
                (preferences.getInt("redBarSubstrate", 255)),
                (preferences.getInt("greenBarSubstrate", 255)),
                (preferences.getInt("blueBarSubstrate", 255)));

        gradientDrawable.setColor(substrateColor);
        substrateLayout.setBackground(layerDrawable);

        int substrateAlpha = preferences.getInt("substrateAlpha",255);

        substrateLayout.getBackground().setAlpha(substrateAlpha);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.text_style_normal:

                textColorTV.setTypeface(null,Typeface.NORMAL);
                textStyleTV.setTypeface(null,Typeface.NORMAL);
                textExampleTV.setTypeface(null,Typeface.NORMAL);

                editor.putInt("textStyleTV",Typeface.NORMAL);
                break;


            case R.id.text_style_bold:

                textColorTV.setTypeface(null,Typeface.BOLD);
                textStyleTV.setTypeface(null,Typeface.BOLD);
                textExampleTV.setTypeface(null,Typeface.BOLD);

                editor.putInt("textStyleTV",Typeface.BOLD);
                break;


            case R.id.text_style_italic:

                textColorTV.setTypeface(null,Typeface.ITALIC);
                textStyleTV.setTypeface(null,Typeface.ITALIC);
                textExampleTV.setTypeface(null,Typeface.ITALIC);

                editor.putInt("textStyleTV",Typeface.ITALIC);
                break;
        }
        editor.apply();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ChoiceSettingsActivity.class));
        super.onBackPressed();
    }
}
