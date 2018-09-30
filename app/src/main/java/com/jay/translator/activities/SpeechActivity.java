package com.jay.translator.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jay.translator.GoogleTranslate;
import com.jay.translator.R;
import com.jay.translator.adapters.InputLanguageItemClick;
import com.jay.translator.adapters.InputLanguageRecyclerViewAdapter;
import com.jay.translator.adapters.OutputLanguageItemClickTo;
import com.jay.translator.adapters.OutputLanguageRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechActivity extends AppCompatActivity implements InputLanguageItemClick, OutputLanguageItemClickTo {

    private static final String TAG = "TAG";
    //    private TextView speechText;
    private AnimationDrawable backgroundAnimation;

    private ArrayList<Integer> images = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Boolean> checkedFrom = new ArrayList<>();
    private ArrayList<Boolean> checkedTo = new ArrayList<>();
    private String inputLanguage;
    private String outputLanguage;
    private SharedPreferences.Editor editor;
    private int posInputLang;
    private int posOutputLang;
    private boolean isFlipped;
    private boolean isLanguageSupported;
    private TextToSpeech textToSpeech;

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int REQ_CODE_SPEECH_OUTPUT = 200;

    private TextView inputLanguageHint;
    private TextView outputLanguageHint;
    private TextView selectedLanguagesHint;
    private EditText editInputText;
    private EditText editOutputText;
    private FrameLayout outputTextLayout;

    @SuppressLint({"ClickableViewAccessibility", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
        inputLanguageHint = findViewById(R.id.language_from_recycler);
        outputLanguageHint = findViewById(R.id.language_to_recycler);
        editInputText = findViewById(R.id.edit_input_text);
        editOutputText = findViewById(R.id.edit_output_text);
        selectedLanguagesHint = findViewById(R.id.language_list);
        outputTextLayout = findViewById(R.id.reverse_layout);


        isFlipped = true;
        outputTextLayout.animate().rotationX(180).rotationY(180).start();


        backgroundAnimation = (AnimationDrawable) bottomSheet.getBackground();
        backgroundAnimation.setExitFadeDuration(4000);


        initializeRecyclerView();


        SharedPreferences preferences = this.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        editor = preferences.edit();

        posInputLang = preferences.getInt("posInputLang",0);
        posOutputLang = preferences.getInt("posOutputLang",1);

        inputLanguage = preferences.getString("inputLanguage","en");
        outputLanguage = preferences.getString("outputLanguage","it");

        Log.d(TAG, "onCreate: " + inputLanguage);
        initializeTextToSpeech(inputLanguage);
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
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

        textToSpeech.shutdown();
    }


    public void clearInputText(View view){
        editInputText.setText("");
    }


    public void clearOutputText(View view){
        editOutputText.setText("");
    }


    public void inputSpeechToText(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, inputLanguage);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, inputLanguage);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, inputLanguage);
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, inputLanguage);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
        }
    }

    public void outputSpeechToText(View view){

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, outputLanguage);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, outputLanguage);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, outputLanguage);
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, outputLanguage);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
        }
    }


    public void translateInputText(View view){
        startTranslateInputText();
    }

    public void translateOutputText(View view){
        startTranslateOutputText();
    }


    public void flipOutputLayout(View view){

        if (isFlipped) {
            outputTextLayout.animate().rotationX(360).rotationY(360).start();
        }
        else {
            outputTextLayout.animate().rotationX(180).rotationY(180).start();
        }

        isFlipped = !isFlipped;
    }


    public void speechInputText(View view){
        speechInputText();
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

        setSelectedRecycler(checkedFrom,posInputLang);
        setSelectedRecycler(checkedTo,posOutputLang);

        LinearLayoutManager managerInputLang = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager managerOutputLang = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView recyclerViewInputLang = findViewById(R.id.languages_from_recycler);
        recyclerViewInputLang.setLayoutManager(managerInputLang);

        RecyclerView recyclerViewOutputLang = findViewById(R.id.languages_to_recycler);
        recyclerViewOutputLang.setLayoutManager(managerOutputLang);

        InputLanguageRecyclerViewAdapter adapterInputLanguages = new InputLanguageRecyclerViewAdapter(images, names, checkedFrom, this, this);
        OutputLanguageRecyclerViewAdapter adapterOutputLanguages = new OutputLanguageRecyclerViewAdapter(images, names, checkedTo, this, this);

        recyclerViewInputLang.setAdapter(adapterInputLanguages);
        recyclerViewOutputLang.setAdapter(adapterOutputLanguages);
    }


    /**
     * Set checked to the checkbox in recycler view
     * @param list list with boolean elements
     * @param pos number of selected recycler
     */
    private void setSelectedRecycler(ArrayList<Boolean> list, int pos){

        for (int i = 0; i < 6; i++){

            if (pos == i) {

                list.add(true);

            }else {

                list.add(false);
            }

        }
    }

    @Override
    public void OnSelectInputLanguage(int position) {

        switch (position) {

            case 0:
                inputLanguageHint.setText("English");
                inputLanguage = "en";
                break;

            case 1:
                inputLanguageHint.setText("Русский");
                inputLanguage = "ru";
                break;

            case 2:
                inputLanguageHint.setText("Deutsch");
                inputLanguage = "de";
                break;

            case 3:
                inputLanguageHint.setText("Français");
                inputLanguage = "fr";
                break;

            case 4:
                inputLanguageHint.setText("Español");
                inputLanguage = "es";
                break;

            case 5:
                inputLanguageHint.setText("Italiano");
                inputLanguage = "it";
                break;
        }

        editor.putInt("posInputLang",position);
        editor.putString("inputLanguage",inputLanguage);
        editor.apply();

    }

    @Override
    public void OnSelectOutputLanguage(int position) {

        switch (position) {

            case 0:
                outputLanguageHint.setText("English");
                outputLanguage = "en";
                break;

            case 1:
                outputLanguageHint.setText("Русский");
                outputLanguage = "ru";
                break;

            case 2:
                outputLanguageHint.setText("Deutsch");
                outputLanguage = "de";
                break;

            case 3:
                outputLanguageHint.setText("Français");
                outputLanguage = "fr";
                break;

            case 4:
                outputLanguageHint.setText("Español");
                outputLanguage = "es";
                break;

            case 5:
                outputLanguageHint.setText("Italiano");
                outputLanguage = "it";
                break;
        }

        editor.putInt("posOutputLang",position);
        editor.putString("outputLanguage",outputLanguage);
        editor.apply();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editInputText.setText(result.get(0));
                    startTranslateInputText();
                }
                break;

            case REQ_CODE_SPEECH_OUTPUT :
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editOutputText.setText(result.get(0));
                    startTranslateOutputText();
                }
                break;
        }
    }


    private void startTranslateInputText() {
        if (isOnline()) {

            new InputTextTranslator(this).execute();

           //TODO set speech speed & rate

        } else {

            alertDialogNoInternetConnection();
        }
    }

    private void startTranslateOutputText(){
        if (isOnline()) {

            new OutputTextTranslator(this).execute();

            //TODO set speech speed & rate

        } else {

            alertDialogNoInternetConnection();
        }

    }



    //check an internet connection
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return (cm != null ? cm.getActiveNetworkInfo() : null) != null;
    }

    //If there is no internet connection then show the dialog
    public void alertDialogNoInternetConnection() {
        Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();
        //TODO alert dialog
    }



    private void speechInputText(){

        String text;
        text = editInputText.getText().toString();

        Log.d(TAG, "speechInputText: " + text);

        if (isLanguageSupported) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Toast.makeText(getApplicationContext(),"language does not supported", Toast.LENGTH_SHORT).show();
        }

    }

    public void initializeTextToSpeech(final String locale) {

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                Log.d(TAG, "onInit: " + locale);
                int result;

                if (status == TextToSpeech.SUCCESS) {

                    isLanguageSupported = true;

                    if (locale != null) {
                        result = textToSpeech.setLanguage(new Locale(locale));
                    } else {
                        result = textToSpeech.setLanguage(Locale.ENGLISH);
                    }

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {

                        isLanguageSupported = false;
                    }
                }
            }
        });
    }


    private class InputTextTranslator extends AsyncTask<Void, Void, String> {

        private ProgressDialog progressBar;


        InputTextTranslator(Context context) {
            progressBar = new ProgressDialog(context);
            progressBar.setTitle("идет перевод");
        }

        @Override
        protected void onPreExecute() {
            progressBar.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            String text = null;
            try {

                GoogleTranslate googleTranslate = new GoogleTranslate();

                String textToTranslate = editInputText.getText().toString();

                text = googleTranslate.translate(textToTranslate, outputLanguage, inputLanguage, getApplicationContext());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                result = result.replace("&#39;", "'");
                result = result.replace("&quot;", "`");
            }

            editOutputText.setText(result);
            progressBar.dismiss();

        }
    }


    private class OutputTextTranslator extends AsyncTask<Void, Void, String> {

        private ProgressDialog progressBar;


        OutputTextTranslator(Context context) {
            progressBar = new ProgressDialog(context);
            progressBar.setTitle("идет перевод");
        }

        @Override
        protected void onPreExecute() {
            progressBar.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            String text = null;
            try {

                GoogleTranslate googleTranslate = new GoogleTranslate();

                String textToTranslate = editOutputText.getText().toString();

                text = googleTranslate.translate(textToTranslate, inputLanguage, outputLanguage, getApplicationContext());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                result = result.replace("&#39;", "'");
                result = result.replace("&quot;", "`");
            }

            editInputText.setText(result);
            progressBar.dismiss();

        }
    }
}
