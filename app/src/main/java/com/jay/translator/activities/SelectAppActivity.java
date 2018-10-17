package com.jay.translator.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.jay.translator.R;
import com.jay.translator.ViewSettings;

public class SelectAppActivity extends AppCompatActivity implements View.OnClickListener {


    private CardView translator;
    private CardView speaker;
    private ImageView accept;

    private boolean isTranslatorSelected;

    private int orientation = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);

        translator = findViewById(R.id.translator);
        translator.setOnClickListener(this);

        speaker = findViewById(R.id.speaker);
        speaker.setOnClickListener(this);

        accept = findViewById(R.id.accept_app_choice);
        accept.setOnClickListener(this);

        ImageView backgroundImage = findViewById(R.id.select_app_background_image);

        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        //set background image
        int image = preferences.getInt("blurImage", R.drawable.london);
        backgroundImage.setImageBitmap(ViewSettings.setImageBlurry(this, getResources().getDrawable(image)));

        orientation = getResources().getConfiguration().orientation;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.translator:
                showTranslatorFrame();
                break;

            case R.id.speaker:
                showSpeakerFrame();
                break;

            case R.id.accept_app_choice:

                if (isTranslatorSelected) {
                    startActivity(new Intent(this, TranslatorActivity.class));
                } else {
                    startActivity(new Intent(this, SpeechActivity.class));
                }

                accept.setBackground(getResources().getDrawable(R.drawable.circle_background_cyan));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        accept.setBackground(getResources().getDrawable
                                (R.drawable.circle_background_primary_dark));
                    }
                }, 300);
                break;
        }
    }


    private void showTranslatorFrame() {

        //animation for portrait orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            translator.animate().setDuration(500)
                    .translationY(250)
                    .scaleY(1.2f)
                    .scaleX(1.18f)
                    .alpha(1f)
                    .start();

            speaker.animate().setDuration(500)
                    .translationY(50)
                    .scaleX(0.4f)
                    .scaleY(0.4f)
                    .alpha(0.5f)
                    .start();

        //animation for landscape orientation
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            translator.animate().setDuration(500)
                    .translationX(250)
                    .scaleY(1.2f)
                    .scaleX(1.2f)
                    .alpha(1f)
                    .start();

            speaker.animate().setDuration(500)
                    .translationX(50)
                    .scaleX(0.4f)
                    .scaleY(0.4f)
                    .alpha(0.5f)
                    .start();
        }

        isTranslatorSelected = true;
        showAccept();
    }


    private void showSpeakerFrame() {

        //animation for portrait orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            translator.animate().setDuration(500)
                    .translationY(-50)
                    .scaleY(0.4f)
                    .scaleX(0.4f)
                    .alpha(0.5f)
                    .start();

            speaker.animate().setDuration(500)
                    .translationY(-250)
                    .scaleY(1.2f)
                    .scaleX(1.18f)
                    .alpha(1f)
                    .start();

        //animation for landscape orientation
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            translator.animate().setDuration(500)
                    .translationX(-50)
                    .scaleY(0.4f)
                    .scaleX(0.4f)
                    .alpha(0.5f)
                    .start();

            speaker.animate().setDuration(500)
                    .translationX(-250)
                    .scaleY(1.2f)
                    .scaleX(1.2f)
                    .alpha(1f)
                    .start();
        }

        isTranslatorSelected = false;
        showAccept();
    }


    private void showAccept() {

        accept.animate()
                .setDuration(500)
                .translationX(0)
                .start();
    }
}
