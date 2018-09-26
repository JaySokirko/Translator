package com.jay.translator.activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jay.translator.R;
import com.jay.translator.adapters.OnItemClick;
import com.jay.translator.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

public class SpeechActivity extends AppCompatActivity implements OnItemClick {

    private static final String TAG = "TAG";
    //    private TextView speechText;
    private AnimationDrawable backgroundAnimation;

    private ArrayList<Integer> images = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Boolean> checked = new ArrayList<>();

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    private TextView languageFromHint;
    private TextView languageToHint;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
        languageFromHint = findViewById(R.id.language_from_recycler);
        languageToHint = findViewById(R.id.language_to_recycler);

        backgroundAnimation = (AnimationDrawable) bottomSheet.getBackground();
        backgroundAnimation.setExitFadeDuration(4000);

        initializeRecyclerView();

    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

    }


    public void onSpeechToText(View view) {

        String language = "es";
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    speechText.setText(result.get(0));
                }
                break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (backgroundAnimation != null && !backgroundAnimation.isRunning())
            backgroundAnimation.start();
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (backgroundAnimation != null && backgroundAnimation.isRunning())
            backgroundAnimation.stop();
    }


    private void initializeRecyclerView() {
        images.add(R.drawable.london);
        images.add(R.drawable.moscow);
        images.add(R.drawable.berlin);
        images.add(R.drawable.paris);
        images.add(R.drawable.madrid);
        images.add(R.drawable.venice);

        names.add("English");
        names.add("Russian");
        names.add("Deutsch");
        names.add("Français");
        names.add("Español");
        names.add("Italiano");

        checked.add(false);
        checked.add(true);
        checked.add(false);
        checked.add(false);
        checked.add(false);
        checked.add(false);

        LinearLayoutManager managerInputLang = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager managerOutputLang = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView recyclerViewInputLang = findViewById(R.id.languages_from_recycler);
        recyclerViewInputLang.setLayoutManager(managerInputLang);

        RecyclerView recyclerViewOutputLang = findViewById(R.id.languages_to_recycler);
        recyclerViewOutputLang.setLayoutManager(managerOutputLang);

        RecyclerViewAdapter adapterInputLanguages = new RecyclerViewAdapter(images, names, checked, this, this);
        RecyclerViewAdapter adapterOutputLanguages = new RecyclerViewAdapter(images, names, checked, this, this);

        recyclerViewInputLang.setAdapter(adapterInputLanguages);
        recyclerViewOutputLang.setAdapter(adapterOutputLanguages);
    }

    @Override
    public void Onclick(int position) {

        switch (position) {

            case 0:
                languageFromHint.setText("English");
                break;

            case 1:
                languageFromHint.setText("Русский");
                break;

            case 2:
                languageFromHint.setText("Deutsch");
                break;

            case 3:
                languageFromHint.setText("Français");
                break;

            case 4:
                languageFromHint.setText("Español");
                break;

            case 5:
                languageFromHint.setText("Italiano");
                break;
        }

    }
}
