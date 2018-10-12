package com.jay.translator.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private ImageView backgroundImage;

    private boolean isTranslatorSelected;

    private int image;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_app);

        translator = findViewById(R.id.translator);
        translator.setOnClickListener(this);

        speaker = findViewById(R.id.speaker);
        speaker.setOnClickListener(this);

        accept = findViewById(R.id.accept_app_choice);
        accept.setVisibility(View.GONE);
        accept.animate().translationX(1000).start();
        accept.setOnClickListener(this);

        backgroundImage = findViewById(R.id.select_app_background_image);

        preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        image = preferences.getInt("blurImage",R.drawable.london);
        backgroundImage.setImageBitmap(ViewSettings.setImageBlurry(this,getResources().getDrawable(image)));
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
                break;
        }
    }


    private void showTranslatorFrame() {

        translator.animate().setDuration(500)
                .translationY(250)
                .scaleY(1.2f)
                .scaleX(1.2f)
                .alpha(1f)
                .start();

        speaker.animate().setDuration(500)
                .translationY(50)
                .scaleX(0.4f)
                .scaleY(0.4f)
                .alpha(0.5f)
                .start();

        isTranslatorSelected = true;
        showAccept();
    }


    private void showSpeakerFrame() {

        translator.animate().setDuration(500)
                .translationY(-50)
                .scaleY(0.4f)
                .scaleX(0.4f)
                .alpha(0.5f)
                .start();

        speaker.animate().setDuration(500)
                .translationY(-250)
                .scaleY(1.2f)
                .scaleX(1.2f)
                .alpha(1f)
                .start();

        isTranslatorSelected = false;
        showAccept();
    }


    private void showAccept() {

        accept.setVisibility(View.VISIBLE);
        accept.animate()
                .setDuration(1000)
                .translationX(0)
                .start();
    }
}
