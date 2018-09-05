package com.jay.translator.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.jay.translator.R;
import com.jay.translator.adapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class SpeechActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
//    private TextView speechText;
    private AnimationDrawable backgroundAnimation;

    private ArrayList<Integer> images = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);

        backgroundAnimation = (AnimationDrawable) bottomSheet.getBackground();
        backgroundAnimation.setExitFadeDuration(4000);

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

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        final RecyclerView recyclerView = findViewById(R.id.languages_from_recycler);
        recyclerView.setLayoutManager(manager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(images,names,this);
        recyclerView.setAdapter(adapter);

         adapter.setOnItemClickListener(new RecyclerViewAdapter.ClickListener() {
             @Override
             public void onItemClick(int position, View v) {

                 Log.d(TAG, "onItemClick: " + position);

             }

             @Override
             public void onItemLongClick(int position, View v) {

             }
         });
    }


    public void onSpeechToText(View view){

        String language =  "es";
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,language);
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
}
