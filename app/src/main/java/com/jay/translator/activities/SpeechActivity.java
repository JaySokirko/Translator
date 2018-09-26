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
import com.jay.translator.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

public class SpeechActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    //    private TextView speechText;
    private AnimationDrawable backgroundAnimation;

    private ArrayList<Integer> images = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private boolean[] checked = new boolean[6];

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private RecyclerViewAdapter adapterInputLanguages;
    private RecyclerViewAdapter adapterOutputLanguages;
    private RecyclerView recyclerViewInputLang;
    private LinearLayoutManager managerInputLang;

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

        adapterInputLanguages.setOnItemClickListener(new RecyclerViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {



            }
        });
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

    }

    private void setChecked(int position) {


//        RecyclerView.ViewHolder holder0 = recyclerViewInputLang.findViewHolderForAdapterPosition(5);
//        CheckBox checkBox0 = holder0.itemView.findViewById(R.id.check_box);
//        checkBox0.setChecked(checked[5] = true);


        RecyclerView.ViewHolder holder = recyclerViewInputLang.findViewHolderForAdapterPosition(position);
        CheckBox checkBox = holder.itemView.findViewById(R.id.check_box);
        checkBox.setChecked(checked[position] = !checked[position]);

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

        checked[0] = true;
        checked[1] = false;
        checked[2] = false;
        checked[3] = false;
        checked[4] = false;
        checked[5] = false;

        managerInputLang = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager managerOutputLang = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewInputLang = findViewById(R.id.languages_from_recycler);
        recyclerViewInputLang.setLayoutManager(managerInputLang);

        RecyclerView recyclerViewOutputLang = findViewById(R.id.languages_to_recycler);
        recyclerViewOutputLang.setLayoutManager(managerOutputLang);

        adapterInputLanguages = new RecyclerViewAdapter(images, names, checked, this);
        adapterOutputLanguages = new RecyclerViewAdapter(images, names, checked, this);

        recyclerViewInputLang.setAdapter(adapterInputLanguages);
        recyclerViewOutputLang.setAdapter(adapterOutputLanguages);
    }
}
