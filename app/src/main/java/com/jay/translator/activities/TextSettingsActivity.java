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

    private int textColorRed;
    private int textColorGreen;
    private int textColorBlue;

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
        textColorRed = preferences.getInt("textColorRed", 0);
        redBar.setMax(255);
        redBar.incrementProgressBy(1);
        redBar.setProgress(textColorRed);
    }


    private void initializeGreenSeekBar() {
        greenBar = findViewById(R.id.text_settings_green);
        textColorGreen = preferences.getInt("textColorGreen", 0);
        greenBar.setMax(255);
        greenBar.incrementProgressBy(1);
        greenBar.setProgress(textColorGreen);
    }


    private void initializeBlueSeekBar() {
        blueBar = findViewById(R.id.text_settings_blue);
        textColorBlue = preferences.getInt("textColorBlue", 0);
        blueBar.setMax(255);
        blueBar.incrementProgressBy(1);
        blueBar.setProgress(textColorBlue);
    }


    private void onRedSeekBarChangeListener() {

        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textColorTV.setTextColor(Color.rgb(progress, textColorGreen, textColorBlue));
                textStyleTV.setTextColor(Color.rgb(progress, textColorGreen, textColorBlue));
                styleNormalBtn.setTextColor(Color.rgb(progress, textColorGreen, textColorBlue));
                styleBoldBtn.setTextColor(Color.rgb(progress, textColorGreen, textColorBlue));
                styleItalicBtn.setTextColor(Color.rgb(progress, textColorGreen, textColorBlue));
                textExampleTV.setTextColor(Color.rgb(progress, textColorGreen, textColorBlue));

                textColorRed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("textColorRed", textColorRed);
                editor.apply();
            }
        });
    }


    private void onGreenSeekBarChangeListener() {

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textColorTV.setTextColor(Color.rgb(textColorRed, progress, textColorBlue));
                textStyleTV.setTextColor(Color.rgb(textColorRed, progress, textColorBlue));
                styleNormalBtn.setTextColor(Color.rgb(textColorRed, progress, textColorBlue));
                styleBoldBtn.setTextColor(Color.rgb(textColorRed, progress, textColorBlue));
                styleItalicBtn.setTextColor(Color.rgb(textColorRed, progress, textColorBlue));
                textExampleTV.setTextColor(Color.rgb(textColorRed, progress, textColorBlue));

                textColorGreen = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("textColorGreen", textColorGreen);
                editor.apply();
            }
        });
    }


    private void onBlueSeekBarChangeListener() {

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textColorTV.setTextColor(Color.rgb(textColorRed, textColorGreen, progress));
                textStyleTV.setTextColor(Color.rgb(textColorRed, textColorGreen, progress));
                styleNormalBtn.setTextColor(Color.rgb(textColorRed, textColorGreen, progress));
                styleBoldBtn.setTextColor(Color.rgb(textColorRed, textColorGreen, progress));
                styleItalicBtn.setTextColor(Color.rgb(textColorRed, textColorGreen, progress));
                textExampleTV.setTextColor(Color.rgb(textColorRed, textColorGreen, progress));

                textColorBlue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("textColorBlue", textColorBlue);
                editor.apply();
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.text_style_normal:

                textColorTV.setTypeface(null,Typeface.NORMAL);
                textStyleTV.setTypeface(null,Typeface.NORMAL);
                textExampleTV.setTypeface(null,Typeface.NORMAL);

                editor.putInt("textStyle",Typeface.NORMAL);
                break;


            case R.id.text_style_bold:

                textColorTV.setTypeface(null,Typeface.BOLD);
                textStyleTV.setTypeface(null,Typeface.BOLD);
                textExampleTV.setTypeface(null,Typeface.BOLD);

                editor.putInt("textStyle",Typeface.BOLD);
                break;


            case R.id.text_style_italic:

                textColorTV.setTypeface(null,Typeface.ITALIC);
                textStyleTV.setTypeface(null,Typeface.ITALIC);
                textExampleTV.setTypeface(null,Typeface.ITALIC);

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


    private void loadSettings() {

        //load text color
        textColorTV.setTextColor(Color.rgb(textColorRed, textColorGreen, textColorBlue));
        textStyleTV.setTextColor(Color.rgb(textColorRed, textColorGreen, textColorBlue));
        styleNormalBtn.setTextColor(Color.rgb(textColorRed, textColorGreen, textColorBlue));
        styleBoldBtn.setTextColor(Color.rgb(textColorRed, textColorGreen, textColorBlue));
        styleItalicBtn.setTextColor(Color.rgb(textColorRed, textColorGreen, textColorBlue));
        textExampleTV.setTextColor(Color.rgb(textColorRed, textColorGreen, textColorBlue));

        //load text style
        textColorTV.setTypeface(null,preferences.getInt("textStyle",0));
        textStyleTV.setTypeface(null,preferences.getInt("textStyle",0));
        textExampleTV.setTypeface(null,preferences.getInt("textStyle",0));

        LinearLayout substrateLayout = findViewById(R.id.text_settings_substrate_background);

        LayerDrawable layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.view_rounded);
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.rounded_view_color);

        //load substrate color
        int  substrateColor = Color.rgb(
                (preferences.getInt("substrateColorRed", 255)),
                (preferences.getInt("substrateColorGreen", 255)),
                (preferences.getInt("substrateColorBlue", 255)));

        gradientDrawable.setColor(substrateColor);
        substrateLayout.setBackground(layerDrawable);

        //load substrate alpha
        int substrateAlpha = preferences.getInt("substrateAlpha",255);
        substrateLayout.getBackground().setAlpha(substrateAlpha);
    }
}
