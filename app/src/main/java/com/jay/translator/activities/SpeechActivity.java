package com.jay.translator.activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jay.translator.DialogLanguageNotSupported;
import com.jay.translator.DialogNoInternet;
import com.jay.translator.GoogleTranslate;
import com.jay.translator.R;
import com.jay.translator.adapters.InputLanguageItemClick;
import com.jay.translator.adapters.InputLanguageRecyclerViewAdapter;
import com.jay.translator.adapters.OutputLanguageItemClickTo;
import com.jay.translator.adapters.OutputLanguageRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Locale;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;

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
    private int seekBarWidth;
    private float inputSpeechSpeed;
    private float inputSpeechPitch;
    private float outputSpeechPitch;
    private float outputSpeechSpeed;
    private boolean isFlipped;
    private boolean isInputLanguageSupported;
    private boolean isOutputLanguageSupported;
    private boolean isSeekBarInputSpeechSpeedShow;
    private boolean isSeekBarInputSpeechPitchShow;
    private boolean isSeekBarOutputSpeechPitchShow;
    private boolean isSeekBarOutputSpeechSpeedShow;
    private TextToSpeech inputTextToSpeech;
    private TextToSpeech outputTextToSpeech;

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int REQ_CODE_SPEECH_OUTPUT = 200;

    private TextView inputLanguageHint;
    private TextView outputLanguageHint;
    private TextView outputLanguageSmallHint;
    private TextView inputLanguageSmallHint;
    private EditText editInputText;
    private EditText editOutputText;
    private SeekBar seekBarInputSpeechPitch;
    private SeekBar seekBarInputSpeechSpeed;
    private SeekBar seekBarOutputSpeechSpeed;
    private SeekBar seekBarOutputSpeechPitch;
    private FrameLayout outputTextLayout;
    private ImageView backgroundImage;
    private LinearLayout bottomSheet;

    private SharedPreferences preferences;
    private RecyclerView recyclerViewInputLang;
    private RecyclerView recyclerViewOutputLang;


    @SuppressLint({"ClickableViewAccessibility", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        bottomSheet = findViewById(R.id.bottom_sheet);
        inputLanguageHint = findViewById(R.id.language_from_recycler);
        outputLanguageHint = findViewById(R.id.language_to_recycler);
        editInputText = findViewById(R.id.edit_input_text);
        editOutputText = findViewById(R.id.edit_output_text);
        outputTextLayout = findViewById(R.id.reverse_layout);
        seekBarInputSpeechSpeed = findViewById(R.id.input_speech_speed);
        seekBarInputSpeechPitch = findViewById(R.id.input_speech_feed);
        seekBarOutputSpeechSpeed = findViewById(R.id.output_speech_speed);
        seekBarOutputSpeechPitch = findViewById(R.id.output_speech_feed);
        outputLanguageSmallHint = findViewById(R.id.output_language_hint);
        inputLanguageSmallHint = findViewById(R.id.input_language_hint);
        backgroundImage = findViewById(R.id.image_view_speech_background);

        seekBarInputSpeechSpeed.setProgress(50);
        seekBarInputSpeechPitch.setProgress(50);
        seekBarOutputSpeechPitch.setProgress(50);
        seekBarOutputSpeechSpeed.setProgress(50);


        isFlipped = true;
        outputTextLayout.animate().rotationX(180).rotationY(180).start();


        isSeekBarInputSpeechPitchShow = false;
        isSeekBarInputSpeechSpeedShow = false;
        isSeekBarOutputSpeechPitchShow = false;
        isSeekBarOutputSpeechSpeedShow = false;


        backgroundAnimation = (AnimationDrawable) bottomSheet.getBackground();
        backgroundAnimation.setExitFadeDuration(4000);


        preferences = this.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
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


        outputLanguageSmallHint.setText(preferences.getString("outputLanguageSmallHint", "Italiano"));
        inputLanguageSmallHint.setText(preferences.getString("inputLanguageSmallHint", "English"));


        initializeInputTextToSpeech(inputLanguage);

        initializeOutputTextToSpeech(outputLanguage);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        seekBarWidth = size.x / 2;

        onInputSpeechSpeedChange();

        onInputSpeechPitchChange();

        onOutputSpeechPitchChange();

        onOutputSpeechSpeedChange();

        showAppTutorial();
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

        if (inputTextToSpeech != null) {

            inputTextToSpeech.stop();
            inputTextToSpeech.shutdown();
        }

        if (outputTextToSpeech != null) {

            outputTextToSpeech.stop();
            outputTextToSpeech.shutdown();
        }

        super.onDestroy();
    }


    private void showAppTutorial() {

        boolean isAppRunFirstTime = preferences.getBoolean("isSpeechRunFirstTime", true);

        if (isAppRunFirstTime) {

            FrameLayout inputLayout = findViewById(R.id.input_layout);
            final FrameLayout outputLayout = findViewById(R.id.output_layout);

            new GuideView.Builder(this)
                    //todo translation
                    .setTitle("Это поле ввода для Вас")
                    .setGravity(GuideView.Gravity.auto) //optional
                    .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
                    .setTargetView(inputLayout)
                    .setTitleTextSize(14)//optional
                    .setGuideListener(new GuideView.GuideListener() {
                        @Override
                        public void onDismiss(View view) {
                            new GuideView.Builder(SpeechActivity.this)
                                    //todo translation
                                    .setTitle("Это для Вашего собеседника")
                                    .setGravity(GuideView.Gravity.auto) //optional
                                    .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
                                    .setTargetView(outputLayout)
                                    .setTitleTextSize(14)//optional
                                    .setGuideListener(new GuideView.GuideListener() {
                                        @Override
                                        public void onDismiss(View view) {
                                            new GuideView.Builder(SpeechActivity.this)
                                                    //todo translation
                                                    .setTitle("Потяните вверх чтобы выбрать языки")
                                                    .setGravity(GuideView.Gravity.auto) //optional
                                                    .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
                                                    .setTargetView(bottomSheet)
                                                    .setTitleTextSize(14)//optional
                                                    .build()
                                                    .show();
                                        }
                                    })
                                    .build()
                                    .show();
                        }
                    })
                    .build()
                    .show();


            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);

            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                        new GuideView.Builder(SpeechActivity.this)
                                //todo translator
                                .setTitle("Выберите Ваш язык")
                                .setGravity(GuideView.Gravity.auto) //optional
                                .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
                                .setTargetView(recyclerViewInputLang)
                                .setTitleTextSize(14)//optional
                                .setGuideListener(new GuideView.GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {
                                        new GuideView.Builder(SpeechActivity.this)
                                                //todo translator
                                                .setTitle("Язык для Вашего собеседника")
                                                .setGravity(GuideView.Gravity.auto) //optional
                                                .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
                                                .setTargetView(recyclerViewOutputLang)
                                                .setTitleTextSize(14)//optional
                                                .build()
                                                .show();
                                    }
                                })
                                .build()
                                .show();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });

            editor.putBoolean("isSpeechRunFirstTime", true);
            editor.apply();
        }
    }


    public void clearInputText(View view) {
        editInputText.setText("");

        setClickable(view);
    }


    public void clearOutputText(View view) {
        editOutputText.setText("");

        setClickable(view);
    }


    public void inputSpeechSpeed(View view) {

        if (!isSeekBarInputSpeechSpeedShow) {

            //stretch animation for speech speed seek bar
            applyTransformationSeekBar(seekBarInputSpeechSpeed, seekBarWidth);

        } else {
            //constriction animation for speed seek bar
            applyTransformationSeekBar(seekBarInputSpeechSpeed, 0);
        }

        isSeekBarInputSpeechSpeedShow = !isSeekBarInputSpeechSpeedShow;

        setClickable(view);
    }


    public void inputSpeechFeed(View view) {

        if (!isSeekBarInputSpeechPitchShow) {

            //stretch animation for speech feed seek bar
            applyTransformationSeekBar(seekBarInputSpeechPitch, seekBarWidth);
        } else {
            //constriction animation for speed seek bar
            applyTransformationSeekBar(seekBarInputSpeechPitch, 0);
        }

        isSeekBarInputSpeechPitchShow = !isSeekBarInputSpeechPitchShow;

        setClickable(view);
    }


    public void outputSpeechPitch(View view) {

        if (!isSeekBarOutputSpeechPitchShow) {

            //stretch animation for speech pitch seek bar
            applyTransformationSeekBar(seekBarOutputSpeechPitch, seekBarWidth);
        } else {
            //constriction animation for speed seek bar
            applyTransformationSeekBar(seekBarOutputSpeechPitch, 0);
        }

        isSeekBarOutputSpeechPitchShow = !isSeekBarOutputSpeechPitchShow;

        setClickable(view);
    }


    public void outputSpeechSpeed(View view) {

        if (!isSeekBarOutputSpeechSpeedShow) {

            //stretch animation for speech pitch seek bar
            applyTransformationSeekBar(seekBarOutputSpeechSpeed, seekBarWidth);
        } else {
            //constriction animation for speed seek bar
            applyTransformationSeekBar(seekBarOutputSpeechSpeed, 0);
        }

        isSeekBarOutputSpeechSpeedShow = !isSeekBarOutputSpeechSpeedShow;

        setClickable(view);
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

        setClickable(view);
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

        setClickable(view);
    }


    public void translateInputText(View view) {
        startTranslateInputText();

        setClickable(view);
    }

    public void translateOutputText(View view) {
        startTranslateOutputText();

        setClickable(view);
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

        setClickable(view);
    }


    public void speechOutputText(View view) {
        speechOutputText();

        setClickable(view);
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
                inputLanguageSmallHint.setText(getResources().getString(R.string.english));
                inputLanguage = "en";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_en));
                break;

            case 1:
                inputLanguageHint.setText(getResources().getText(R.string.russian));
                inputLanguageSmallHint.setText(getResources().getString(R.string.russian));
                inputLanguage = "ru";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_ru));
                break;

            case 2:
                inputLanguageHint.setText(getResources().getText(R.string.german));
                inputLanguageSmallHint.setText(getResources().getString(R.string.german));
                inputLanguage = "de";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_de));
                break;

            case 3:
                inputLanguageHint.setText(getResources().getText(R.string.french));
                inputLanguageSmallHint.setText(getResources().getString(R.string.french));
                inputLanguage = "fr";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_fr));
                break;

            case 4:
                inputLanguageHint.setText(getResources().getText(R.string.spanish));
                inputLanguageSmallHint.setText(getResources().getString(R.string.spanish));
                inputLanguage = "es";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_es));
                break;

            case 5:
                inputLanguageHint.setText(getResources().getText(R.string.italian));
                inputLanguageSmallHint.setText(getResources().getString(R.string.italian));
                inputLanguage = "it";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_it));
                break;

            case 6:
                inputLanguageHint.setText(getResources().getText(R.string.polish));
                inputLanguageSmallHint.setText(getResources().getString(R.string.polish));
                inputLanguage = "pl";
                setHintToEditText(editInputText, getResources().getString(R.string.enter_some_text_pl));
                break;
        }

        initializeInputTextToSpeech(inputLanguage);

        editor.putInt("posInputLang", position);
        editor.putString("inputLanguage", inputLanguage);
        editor.putString("inputLanguageHint", inputLanguageHint.getText().toString());
        editor.putString("inputLanguageSmallHint", inputLanguageSmallHint.getText().toString());
        editor.apply();

    }


    @Override
    public void OnSelectOutputLanguage(int position) {

        switch (position) {

            case 0:
                outputLanguageHint.setText(getResources().getText(R.string.english));
                outputLanguageSmallHint.setText(getResources().getString(R.string.english));
                outputLanguage = "en";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_en));
                break;

            case 1:
                outputLanguageHint.setText(getResources().getText(R.string.russian));
                outputLanguageSmallHint.setText(getResources().getString(R.string.russian));
                outputLanguage = "ru";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_ru));
                break;

            case 2:
                outputLanguageHint.setText(getResources().getText(R.string.german));
                outputLanguageSmallHint.setText(getResources().getString(R.string.german));
                outputLanguage = "de";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_de));
                break;

            case 3:
                outputLanguageHint.setText(getResources().getText(R.string.french));
                outputLanguageSmallHint.setText(getResources().getString(R.string.french));
                outputLanguage = "fr";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_fr));
                break;

            case 4:
                outputLanguageHint.setText(getResources().getText(R.string.spanish));
                outputLanguageSmallHint.setText(getResources().getString(R.string.spanish));
                outputLanguage = "es";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_es));
                break;

            case 5:
                outputLanguageHint.setText(getResources().getText(R.string.italian));
                outputLanguageSmallHint.setText(getResources().getString(R.string.italian));
                outputLanguage = "it";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_it));
                break;

            case 6:
                outputLanguageHint.setText(getResources().getText(R.string.polish));
                outputLanguageSmallHint.setText(getResources().getString(R.string.polish));
                outputLanguage = "pl";
                setHintToEditText(editOutputText, getResources().getString(R.string.enter_some_text_pl));
                break;
        }

        initializeOutputTextToSpeech(outputLanguage);

        editor.putInt("posOutputLang", position);
        editor.putString("outputLanguage", outputLanguage);
        editor.putString("outputLanguageHint", outputLanguageHint.getText().toString());
        editor.putString("outputLanguageSmallHint", outputLanguageSmallHint.getText().toString());
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


    private void applyTransformationSeekBar(final SeekBar seekBar, int width) {

        ValueAnimator anim = ValueAnimator.ofInt(seekBar.getMeasuredWidth(), width);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = seekBar.getLayoutParams();
                layoutParams.width = val;
                seekBar.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(500);
        anim.start();
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

            outputTextToSpeech.setSpeechRate(outputSpeechSpeed);
            outputTextToSpeech.setPitch(outputSpeechPitch);
        } else {

            alertDialogNoInternetConnection();
        }
    }

    private void startTranslateOutputText() {
        if (isOnline()) {

            new OutputTextTranslator(this).execute();

            inputTextToSpeech.setPitch(inputSpeechPitch);
            inputTextToSpeech.setSpeechRate(inputSpeechSpeed);
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

        recyclerViewInputLang = findViewById(R.id.languages_from_recycler);
        recyclerViewInputLang.setLayoutManager(managerInputLang);

        recyclerViewOutputLang = findViewById(R.id.languages_to_recycler);
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

        DialogNoInternet dialogNoInternet = new DialogNoInternet(this);
        dialogNoInternet.show();
    }


    private void speechInputText() {

        String text;
        text = editInputText.getText().toString();

        if (isInputLanguageSupported) {
            inputTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {

            DialogLanguageNotSupported d = new DialogLanguageNotSupported(this);
            d.show();
        }
    }


    private void speechOutputText() {

        String text;
        text = editOutputText.getText().toString();

        if (isOutputLanguageSupported) {
            outputTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {

            DialogLanguageNotSupported d = new DialogLanguageNotSupported(this);
            d.show();
        }
    }


    private void initializeInputTextToSpeech(final String locale) {

        if (inputTextToSpeech != null) {

            inputTextToSpeech.stop();
            inputTextToSpeech.shutdown();
        }

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

        if (outputTextToSpeech != null) {

            outputTextToSpeech.stop();
            outputTextToSpeech.shutdown();
        }

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


    private void onInputSpeechSpeedChange() {

        seekBarInputSpeechSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                inputSpeechSpeed = (float) seekBarInputSpeechSpeed.getProgress() / 50;

                if (inputSpeechSpeed < 0.1)
                    inputSpeechSpeed = 0.1f;

                inputTextToSpeech.setSpeechRate(inputSpeechSpeed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private void onInputSpeechPitchChange() {

        seekBarInputSpeechPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                inputSpeechPitch = (float) seekBarInputSpeechPitch.getProgress() / 50;

                if (inputSpeechPitch < 0.1)
                    inputSpeechPitch = 0.1f;

                inputTextToSpeech.setPitch(inputSpeechPitch);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private void onOutputSpeechPitchChange() {

        seekBarOutputSpeechPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                outputSpeechPitch = (float) seekBarOutputSpeechPitch.getProgress() / 50;

                if (outputSpeechPitch < 0.1)
                    outputSpeechPitch = 0.1f;

                outputTextToSpeech.setPitch(outputSpeechPitch);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private void onOutputSpeechSpeedChange() {

        seekBarOutputSpeechSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                outputSpeechSpeed = (float) seekBarOutputSpeechSpeed.getProgress() / 50;

                if (outputSpeechSpeed < 0.1)
                    outputSpeechSpeed = 0.1f;

                outputTextToSpeech.setSpeechRate(outputSpeechSpeed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private void setClickable(final View view) {

        view.setBackground(getResources().getDrawable(R.drawable.circle_background_cyan));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setBackground(getResources().getDrawable(R.drawable.circle_background_primary_dark));
            }
        }, 500);
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
