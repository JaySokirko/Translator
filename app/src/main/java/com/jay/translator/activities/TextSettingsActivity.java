package com.jay.translator.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jay.translator.R;

public class TextSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textStyle;
    private TextView textColor;
    private TextView textExample;
    private Button styleNormal;
    private Button styleBold;
    private Button styleItalic;
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

        textStyle = findViewById(R.id.set_text_style);
        textColor = findViewById(R.id.set_text_color);
        textExample = findViewById(R.id.text_settings_example);

        styleNormal = findViewById(R.id.text_style_normal);
        styleNormal.setOnClickListener(this);

        styleBold = findViewById(R.id.text_style_bold);
        styleBold.setOnClickListener(this);

        styleItalic = findViewById(R.id.text_style_italic);
        styleItalic.setOnClickListener(this);

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

                textColor.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));
                textStyle.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));
                styleNormal.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));
                styleBold.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));
                styleItalic.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));
                textExample.setTextColor(Color.rgb(progress, greenBarProgress, blueBarProgress));

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

                textColor.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));
                textStyle.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));
                styleNormal.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));
                styleBold.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));
                styleItalic.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));
                textExample.setTextColor(Color.rgb(redBarProgress, progress, blueBarProgress));

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

                textColor.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));
                textStyle.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));
                styleNormal.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));
                styleBold.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));
                styleItalic.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));
                textExample.setTextColor(Color.rgb(redBarProgress, greenBarProgress, progress));

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

        textColor.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));
        textStyle.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));
        styleNormal.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));
        styleBold.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));
        styleItalic.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));
        textExample.setTextColor(Color.rgb(redBarProgress, greenBarProgress, blueBarProgress));

        textColor.setTypeface(null,preferences.getInt("textStyle",0));
        textStyle.setTypeface(null,preferences.getInt("textStyle",0));
        textExample.setTypeface(null,preferences.getInt("textStyle",0));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.text_style_normal:

                textColor.setTypeface(null,Typeface.NORMAL);
                textStyle.setTypeface(null,Typeface.NORMAL);
                textExample.setTypeface(null,Typeface.NORMAL);

                editor.putInt("textStyle",Typeface.NORMAL);
                break;


            case R.id.text_style_bold:

                textColor.setTypeface(null,Typeface.BOLD);
                textStyle.setTypeface(null,Typeface.BOLD);
                textExample.setTypeface(null,Typeface.BOLD);

                editor.putInt("textStyle",Typeface.BOLD);
                break;

            case R.id.text_style_italic:

                textColor.setTypeface(null,Typeface.ITALIC);
                textStyle.setTypeface(null,Typeface.ITALIC);
                textExample.setTypeface(null,Typeface.ITALIC);

                editor.putInt("textStyle",Typeface.ITALIC);
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
