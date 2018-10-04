package com.jay.translator.activities;

import android.animation.ValueAnimator;
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
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
    private boolean isInputLanguageSupported;
    private boolean isOutputLanguageSupported;
    private boolean isSeekBarInputSpeechSpeedShow;
    private boolean isSeekBarInputSpeechFeedShow;
    private TextToSpeech inputTextToSpeech;
    private TextToSpeech outputTextToSpeech;

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int REQ_CODE_SPEECH_OUTPUT = 200;

    private TextView inputLanguageHint;
    private TextView outputLanguageHint;
    private EditText editInputText;
    private EditText editOutputText;
    private SeekBar seekBarInputSpeechFeed;
    private SeekBar seekBarInputSpeechSpeed;
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
        outputTextLayout = findViewById(R.id.reverse_layout);
        seekBarInputSpeechSpeed = findViewById(R.id.input_speech_speed);

        seekBarInputSpeechSpeed.setProgress(50);

        isFlipped = true;
        outputTextLayout.animate().rotationX(180).rotationY(180).start();


        isSeekBarInputSpeechFeedShow = false;
        isSeekBarInputSpeechSpeedShow = false;

        backgroundAnimation = (AnimationDrawable) bottomSheet.getBackground();
        backgroundAnimation.setExitFadeDuration(4000);


        SharedPreferences preferences = this.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        editor = preferences.edit();


        posInputLang = preferences.getInt("posInputLang", 0);
        posOutputLang = preferences.getInt("posOutputLang", 5);


        initializeRecyclerView();


        inputLanguage = preferences.getString("inputLanguage", "en");
        outputLanguage = preferences.getString("outputLanguage", "it");


        inputLanguageHint.setText(preferences.getString("inputLanguageHint", "English"));
        outputLanguageHint.setText(preferences.getString("outputLanguageHint", "Italiano"));


        editInputText.setHint(preferences.getString("setHintToEditText", "Enter some text"));
        editOutputText.setHint(preferences.getString("setHintFromEditText", "Inserisci del testo"));

        initializeInputTextToSpeech(inputLanguage);

        initializeOutputTextToSpeech(outputLanguage);
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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        inputTextToSpeech.shutdown();
        outputTextToSpeech.shutdown();
    }

    public void clearInputText(View view) {
        editInputText.setText("");
    }


    public void clearOutputText(View view) {
        editOutputText.setText("");
    }


    public void inputSpeechSpeed(View view) {

        final int seekBarWidth = 400;

        if (!isSeekBarInputSpeechSpeedShow) {

            //stretch animation for speech speed seek bar
            ValueAnimator anim = ValueAnimator.ofInt(seekBarInputSpeechSpeed.getMeasuredWidth(), seekBarWidth);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = seekBarInputSpeechSpeed.getLayoutParams();
                    layoutParams.width = val;
                    seekBarInputSpeechSpeed.setLayoutParams(layoutParams);
                }
            });
            anim.setDuration(500);
            anim.start();

        } else {

            //constriction animation for speed seek bar
            ValueAnimator anim = ValueAnimator.ofInt(seekBarInputSpeechSpeed.getMeasuredWidth(), 0);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = seekBarInputSpeechSpeed.getLayoutParams();
                    layoutParams.width = val;
                    seekBarInputSpeechSpeed.setLayoutParams(layoutParams);
                }
            });
            anim.setDuration(500);
            anim.start();
        }

        isSeekBarInputSpeechSpeedShow = !isSeekBarInputSpeechSpeedShow;
    }


    public void inputSpeechFeed(View view) {

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

    public void outputSpeechToText(View view) {

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


    public void translateInputText(View view) {
        startTranslateInputText();
    }

    public void translateOutputText(View view) {
        startTranslateOutputText();
    }


    public void flipOutputLayout(View view) {

        if (isFlipped) {
            outputTextLayout.animate().rotationX(360).rotationY(360).start();
        } else {
            outputTextLayout.animate().rotationX(180).rotationY(180).start();
        }

        isFlipped = !isFlipped;
    }


    public void speechInputText(View view) {
        speechInputText();
    }


    public void speechOutputText(View view) {
        speechOutputText();
    }


    /**
     * Set checked to the checkbox in recycler view
     *
     * @param list list with boolean elements
     * @param pos  number of selected recycler
     */
    private void setSelectedRecycler(ArrayList<Boolean> list, int pos) {

        for (int i = 0; i < 7; i++) {

            if (pos == i) {

                list.add(true);

                Log.d(TAG, "setSelectedRecycler: " + pos);

            } else {

                list.add(false);
            }

        }
    }

    @Override
    public void OnSelectInputLanguage(int position) {

        switch (position) {

            case 0:
                inputLanguageHint.setText(getResources().getText(R.string.english));
                inputLanguage = "en";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_en));
                break;

            case 1:
                inputLanguageHint.setText(getResources().getText(R.string.russian));
                inputLanguage = "ru";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_ru));
                break;

            case 2:
                inputLanguageHint.setText(getResources().getText(R.string.german));
                inputLanguage = "de";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_de));
                break;

            case 3:
                inputLanguageHint.setText(getResources().getText(R.string.french));
                inputLanguage = "fr";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_fr));
                break;

            case 4:
                inputLanguageHint.setText(getResources().getText(R.string.spanish));
                inputLanguage = "es";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_es));
                break;

            case 5:
                inputLanguageHint.setText(getResources().getText(R.string.italian));
                inputLanguage = "it";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_it));
                break;

            case 6:
                inputLanguageHint.setText(getResources().getText(R.string.polish));
                inputLanguage = "pl";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_pl));
                break;
        }

        initializeInputTextToSpeech(inputLanguage);

        editor.putInt("posInputLang", position);
        editor.putString("inputLanguage", inputLanguage);
        editor.putString("inputLanguageHint", inputLanguageHint.getText().toString());
        editor.apply();

    }

    @Override
    public void OnSelectOutputLanguage(int position) {

        switch (position) {

            case 0:
                outputLanguageHint.setText(getResources().getText(R.string.english));
                outputLanguage = "en";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_en));
                break;

            case 1:
                outputLanguageHint.setText(getResources().getText(R.string.russian));
                outputLanguage = "ru";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_ru));
                break;

            case 2:
                outputLanguageHint.setText(getResources().getText(R.string.german));
                outputLanguage = "de";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_de));
                break;

            case 3:
                outputLanguageHint.setText(getResources().getText(R.string.french));
                outputLanguage = "fr";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_fr));
                break;

            case 4:
                outputLanguageHint.setText(getResources().getText(R.string.spanish));
                outputLanguage = "es";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_es));
                break;

            case 5:
                outputLanguageHint.setText(getResources().getText(R.string.italian));
                outputLanguage = "it";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_it));
                break;

            case 6:
                outputLanguageHint.setText(getResources().getText(R.string.polish));
                outputLanguage = "pl";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_pl));
                break;
        }

        initializeOutputTextToSpeech(outputLanguage);

        editor.putInt("posOutputLang", position);
        editor.putString("outputLanguage", outputLanguage);
        editor.putString("outputLanguageHint", outputLanguageHint.getText().toString());
        editor.apply();
    }

    private void setHintToEditText(EditText editText, String text) {

        editText.setHint(text);

        if (editText == editInputText) {
            editor.putString("setHintToEditText", text);
        }

        if (editText == editOutputText) {
            editor.putString("setHintFromEditText", text);
        }
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

            case REQ_CODE_SPEECH_OUTPUT:
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

    private void startTranslateOutputText() {
        if (isOnline()) {

            new OutputTextTranslator(this).execute();

            //TODO set speech speed & rate

        } else {

            alertDialogNoInternetConnection();
        }

    }


    private void initializeRecyclerView() {
        images.add(R.drawable.london);
        images.add(R.drawable.moscow);
        images.add(R.drawable.berlin);
        images.add(R.drawable.paris);
        images.add(R.drawable.madrid);
        images.add(R.drawable.venice);
        images.add(R.drawable.warsaw);

        names.add("English");
        names.add("Russian");
        names.add("Deutsch");
        names.add("Français");
        names.add("Español");
        names.add("Italiano");
        names.add("Polish");

        setSelectedRecycler(checkedFrom, posInputLang);
        setSelectedRecycler(checkedTo, posOutputLang);

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


    private void speechInputText() {

        String text;
        text = editInputText.getText().toString();

        if (isInputLanguageSupported) {
            inputTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            //TODO locale
            Toast.makeText(getApplicationContext(), "language does not supported", Toast.LENGTH_SHORT).show();
        }
    }


    private void speechOutputText() {

        String text;
        text = editOutputText.getText().toString();

        if (isOutputLanguageSupported) {
            outputTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            //TODO locale
            Toast.makeText(getApplicationContext(), "language does not supported", Toast.LENGTH_SHORT).show();
        }
    }


    private void initializeInputTextToSpeech(final String locale) {

        inputTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                int result;

                if (status == TextToSpeech.SUCCESS) {

                    isInputLanguageSupported = true;

                    if (locale != null) {
                        result = inputTextToSpeech.setLanguage(new Locale(locale));
                    } else {
                        result = inputTextToSpeech.setLanguage(Locale.ENGLISH);
                    }

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {

                        isInputLanguageSupported = false;
                    }
                }
            }
        }, "com.google.android.tts");
    }


    private void initializeOutputTextToSpeech(final String locale) {

        outputTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                int result;

                if (status == TextToSpeech.SUCCESS) {

                    isOutputLanguageSupported = true;

                    if (locale != null) {
                        result = outputTextToSpeech.setLanguage(new Locale(locale));
                    } else {
                        result = outputTextToSpeech.setLanguage(Locale.ENGLISH);
                    }

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {

                        isOutputLanguageSupported = false;
                    }
                }
            }
        }, "com.google.android.tts");
    }


    private class InputTextTranslator extends AsyncTask<Void, Void, String> {

        private ProgressDialog progressBar;


        InputTextTranslator(Context context) {
            progressBar = new ProgressDialog(context);
            //TODO locale
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

            speechOutputText();

        }
    }


    private class OutputTextTranslator extends AsyncTask<Void, Void, String> {

        private ProgressDialog progressBar;


        OutputTextTranslator(Context context) {
            progressBar = new ProgressDialog(context);
            //TODO locale
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

            //TODO if speech selected
            speechInputText();

        }
    }
}
