package com.jay.translator.activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jay.translator.GoogleTranslate;
import com.jay.translator.LanguageSettings;
import com.jay.translator.OnSwipeTouchListener;
import com.jay.translator.R;
import com.jay.translator.ViewSettings;
import com.jay.translator.adapters.languages_spenner.Language;
import com.jay.translator.adapters.languages_spenner.LanguagesSpinner;

import java.util.ArrayList;
import java.util.Locale;

import de.mateware.snacky.Snacky;


public class TranslatorActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "TAG";
    private FloatingActionButton fabSettings;
    private FloatingActionButton fabChoiceLanguage;
    private FloatingActionButton fabBackgroundSettings;
    private FloatingActionButton fabStartTranslate;
    private FloatingActionButton fabSpeechSettings;
    private FloatingActionButton fabSpeechFeed;
    private FloatingActionButton fabSpeechSpeed;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabSave;
    private FloatingActionButton fabSend;
    private ImageView backgroundImage;
    private CoordinatorLayout inputTextLayout;
    private CoordinatorLayout outputTextLayout;
    private CoordinatorLayout onTouchEventField;
    private SeekBar seekBarSpeechSpeed;
    private SeekBar seekBarSpeechFeed;
    private EditText editedText;
    private TextView translatedText;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;

    private AnimationDrawable toolBarAnimation;
    private boolean isSettingsOpen;
    private boolean isSpeechSettingsOpen;
    private boolean isShowTranslatedTextFrame;
    private boolean isShowInputTextFrame;
    private boolean isShowShareSettings;
    private Context context;
    private ValueAnimator valueAnimator;
    private String languageFrom;
    private String languageTo;
    private TextToSpeech textToSpeech;
    private String locale;
    private SharedPreferences.Editor editor;

    private float pitchValue;


    @SuppressLint({"ClickableViewAccessibility", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        context = TranslatorActivity.this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);

        AppBarLayout barLayout = findViewById(R.id.app_bar);
        barLayout.addOnOffsetChangedListener(this);

        fabSettings = findViewById(R.id.fab_settings);
        fabChoiceLanguage = findViewById(R.id.fab_language_settings);
        fabBackgroundSettings = findViewById(R.id.fab_view_settings);
        fabStartTranslate = findViewById(R.id.fab_translation);
        fabSpeechSettings = findViewById(R.id.fab_speech_settings);
        fabSpeechFeed = findViewById(R.id.fab_speech_feed);
        fabSpeechSpeed = findViewById(R.id.fab_speech_speed);
        fabShare = findViewById(R.id.fab_share);
        fabSave = findViewById(R.id.fab_save);
        fabSend = findViewById(R.id.fab_send);
        editedText = findViewById(R.id.edited_text);
        translatedText = findViewById(R.id.translated_text);

        inputTextLayout = findViewById(R.id.input_text_layout);

        outputTextLayout = findViewById(R.id.output_text_layout);

        onTouchEventField = findViewById(R.id.container);

        backgroundImage = findViewById(R.id.image_view_translator_background);

        seekBarSpeechSpeed = findViewById(R.id.seek_bar_speech_speed);
        seekBarSpeechSpeed.setMax(100);
        seekBarSpeechSpeed.setProgress(50);

        seekBarSpeechFeed = findViewById(R.id.seek_bar_speech_feed);
        seekBarSpeechFeed.setMax(100);
        seekBarSpeechFeed.setProgress(50);

        spinnerFrom = findViewById(R.id.spinner_from);
        spinnerTo = findViewById(R.id.spinner_to);

        toolBarAnimation = (AnimationDrawable) toolbarLayout.getBackground();
        toolBarAnimation.setExitFadeDuration(4000);

        LanguageSettings.loadLocale(context);

        setBackground();

        //settings menu is collapsed by default
        isSettingsOpen = false;

        isSpeechSettingsOpen = false;

        isShowTranslatedTextFrame = false;

        isShowInputTextFrame = true;

        isShowShareSettings = false;

        outputTextLayout.setVisibility(View.GONE);

        onSwipeTouchListener();

        initLanguagesList();

        onSelectLanguageFromListener();
        onSelectLanguageToListener();

        SharedPreferences preferences = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        editor = preferences.edit();
        locale = preferences.getString("language", "en");

        spinnerFrom.setSelection(preferences.getInt("selectionFrom", 0));
        spinnerTo.setSelection(preferences.getInt("selectionTo", 1));

        editedText.setText(preferences.getString("editedText",""));

        onPitchChangeListener();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (toolBarAnimation != null && !toolBarAnimation.isRunning())
            toolBarAnimation.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (toolBarAnimation != null && toolBarAnimation.isRunning())
            toolBarAnimation.stop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.putString("editedText",editedText.getText().toString());
        editor.apply();
    }



    private void startTranslate() {

        if (isOnline()) {

            new TranslateTask(context).execute();
        } else {

            alertDialogNoInternetConnection();
        }
    }


    //on translate button click
    public void onTranslateClick(View view) {
        showTranslatedTextFrame();

        startTranslate();
    }


    //on clear text button click
    public void onClearTextClick(View view) {
        editedText.setText("");
    }



    public void onSpeechSettingsClick(View view) {

        if (!isSpeechSettingsOpen) {

            showSpeechSettings();
        } else {

            closeSpeechSettings();
        }
    }



    public void onSettingsClick(View view) {

        if (!isSettingsOpen) {

            showSettings();
        } else {

            closeSettings();
        }
    }



    public void onSpeechSpeedClick(View view) {

        Snacky.builder()
                .setActivity(TranslatorActivity.this)
                .setText(R.string.change_speech_speed)
                .setTextColor(getResources().getColor(R.color.colorText))
                .centerText()
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setDuration(Snacky.LENGTH_SHORT)
                .build()
                .show();
    }


    public void onSpeechFeedClick(View view) {

        Snacky.builder()
                .setActivity(TranslatorActivity.this)
                .setText(R.string.change_speech_feed)
                .setTextColor(getResources().getColor(R.color.colorText))
                .centerText()
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setDuration(Snacky.LENGTH_SHORT)
                .build()
                .show();
    }



    public void onShareClick(View view) {

        if (!isShowShareSettings) {

            showShareSettings();
        } else {
            closeShareSettings();
        }
    }



    private void showShareSettings() {

        float x = getResources().getDimension(R.dimen.standard_55);
        float y = fabSpeechSettings.getHeight() + 10;

        fabSave.animate().translationY(y).translationX(x).start();
        fabSend.animate().translationY(y).start();

        isShowShareSettings = true;

        closeSpeechSettings();
    }



    private void closeShareSettings() {

        fabSave.animate().translationY(0).translationX(0).start();
        fabSend.animate().translationY(0).start();

        isShowShareSettings = false;
    }


    private void showSpeechSettings() {

        fabSpeechSpeed.setVisibility(View.VISIBLE);
        fabSpeechFeed.setVisibility(View.VISIBLE);
        seekBarSpeechSpeed.setVisibility(View.VISIBLE);

        //set speech speed button under share button
        final float x = fabSpeechSettings.getX() - fabShare.getX();
        final float y = fabShare.getHeight() + 10;

        //set speech feed button under speech speed
        float y1 = y + fabShare.getHeight();

        fabSpeechSpeed.animate().translationX(-x).translationY(y).start();

        fabSpeechFeed.animate().translationX(-x).translationY(y1).start();

        seekBarSpeechSpeed.animate().translationY(y).start();
        seekBarSpeechFeed.animate().translationY(y1).start();

        final int seekBarWidth = onTouchEventField.getWidth() - fabShare.getWidth() * 2;
        Animation animation = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ViewGroup.LayoutParams seekBarParams = seekBarSpeechSpeed.getLayoutParams();
                seekBarParams.width = (int) (seekBarWidth * interpolatedTime);
                seekBarSpeechSpeed.setLayoutParams(seekBarParams);
                seekBarSpeechFeed.setLayoutParams(seekBarParams);
            }
        };
        animation.setDuration(500); // in ms
        seekBarSpeechSpeed.startAnimation(animation);
        seekBarSpeechFeed.startAnimation(animation);

        isSpeechSettingsOpen = true;

        closeShareSettings();
    }



    private void closeSpeechSettings() {

        fabSpeechSpeed.animate().translationX(0).translationY(0).start();
        fabSpeechFeed.animate().translationX(0).translationY(0).start();

        seekBarSpeechSpeed.animate().translationY(0).start();
        seekBarSpeechFeed.animate().translationY(0).start();

        final int seekBarWidth = 30;
        Animation animation = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ViewGroup.LayoutParams seekBarParams = seekBarSpeechSpeed.getLayoutParams();
                seekBarParams.width = seekBarWidth;
                seekBarSpeechSpeed.setLayoutParams(seekBarParams);
                seekBarSpeechFeed.setLayoutParams(seekBarParams);
            }
        };
        animation.setDuration(500); // in ms
        seekBarSpeechSpeed.startAnimation(animation);
        seekBarSpeechFeed.startAnimation(animation);

        isSpeechSettingsOpen = false;
    }



    private void showSettings() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                isSettingsOpen = true;

                fabSettings.animate().rotation(360).setDuration(1500).start();

                fabChoiceLanguage.animate().translationY(getResources().getDimension(R.dimen.standard_55));
                fabBackgroundSettings.animate().translationY(getResources().getDimension(R.dimen.standard_105));
            }
        });
    }



    private void closeSettings() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                isSettingsOpen = false;

                fabSettings.animate().rotation(0).setDuration(1500).start();

                fabChoiceLanguage.animate().translationY(0);
                fabBackgroundSettings.animate().translationY(0);
            }
        });
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
            //if app bar is collapsed hide settings view
            closeSettings();

        }

        float a = (float) (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange()) * -1;

        float b = (a / (appBarLayout.getTotalScrollRange() / 2)) / 2;
    }



    private void setBackground() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ViewSettings.setBackgroundImage(LanguageSettings.getLanguage(), backgroundImage);
                backgroundImage.setImageBitmap
                        (ViewSettings.setImageBlurry(context, backgroundImage.getDrawable()));
            }
        });
    }



    private void showTranslatedTextFrame() {

        //input text field is going
        inputTextLayout.animate().alpha(0f).setDuration(700).x(-1000).start();

        outputTextLayout.setVisibility(View.VISIBLE);

        valueAnimator = ValueAnimator.ofFloat(1000f, 0f);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                outputTextLayout.setAlpha(1f);
                outputTextLayout.setTranslationX((float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();

        isShowTranslatedTextFrame = true;
        isShowInputTextFrame = false;
    }



    private void showInputTextFrame() {

        // output text field is going
        outputTextLayout.animate().alpha(0).x(1000).setDuration(700).start();

        valueAnimator = ValueAnimator.ofFloat(-1000f, 0f);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                inputTextLayout.setAlpha(1f);
                inputTextLayout.setTranslationX((float) animation.getAnimatedValue());
                fabStartTranslate.setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();

        isShowTranslatedTextFrame = false;
        isShowInputTextFrame = true;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void onSwipeTouchListener() {

        onTouchEventField.setOnTouchListener(new OnSwipeTouchListener(context) {

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                if (!isShowTranslatedTextFrame) {
                    showTranslatedTextFrame();
                }
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                if (!isShowInputTextFrame) {
                    showInputTextFrame();
                }
            }
        });
    }



    private void onSelectLanguageFromListener() {

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        languageFrom = GoogleTranslate.ENGLISH;
                        break;

                    case 1:
                        languageFrom = GoogleTranslate.RUSSIAN;
                        break;

                    case 2:
                        languageFrom = GoogleTranslate.FRENCH;
                        break;

                    case 3:
                        languageFrom = GoogleTranslate.GERMAN;
                        break;

                    case 4:
                        languageFrom = GoogleTranslate.ITALIAN;
                        break;

                    case 5:
                        languageFrom = GoogleTranslate.SPANISH;
                        break;
                }

                editor.putInt("selectionFrom", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public void onSelectLanguageToListener() {

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        languageTo = GoogleTranslate.ENGLISH;
                        locale = "en";
                        break;

                    case 1:
                        languageTo = GoogleTranslate.RUSSIAN;
                        locale = "ru";
                        break;

                    case 2:
                        languageTo = GoogleTranslate.FRENCH;
                        locale = "fr";
                        break;

                    case 3:
                        languageTo = GoogleTranslate.GERMAN;
                        locale = "de";
                        break;

                    case 4:
                        languageTo = GoogleTranslate.ITALIAN;
                        locale = "it";
                        break;

                    case 5:
                        languageTo = GoogleTranslate.SPANISH;
                        locale = "es";
                        break;
                }

                editor.putString("language", locale);
                editor.putInt("selectionTo", position);
                editor.apply();

                initializeTextToSpeech(locale);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public void onSpeechText(View view) {
        String text;
        text = translatedText.getText().toString();
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }



    public void initializeTextToSpeech(final String locale) {

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                int result;

                if (status == TextToSpeech.SUCCESS) {

                    if (locale != null) {
                        result = textToSpeech.setLanguage(new Locale(locale));
                    } else {
                        result = textToSpeech.setLanguage(Locale.ENGLISH);
                    }

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {

                        Toast.makeText(context, "Язык не поддерживается",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    private void onPitchChangeListener() {

        seekBarSpeechFeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                pitchValue = (float) seekBar.getProgress() / 50;

                if (pitchValue < 0.1)
                    pitchValue = 0.1f;

                textToSpeech.setPitch(pitchValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarSpeechSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                pitchValue = (float) seekBarSpeechSpeed.getProgress() / 50;

                if (pitchValue < 0.1)
                    pitchValue = 0.1f;

                textToSpeech.setSpeechRate(pitchValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    //check an internet connection
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return (cm != null ? cm.getActiveNetworkInfo() : null) != null;
    }

    //If there is no internet connection then show the dialog
    public void alertDialogNoInternetConnection() {
        Toast.makeText(context, "no internet connection", Toast.LENGTH_SHORT).show();
    }


    private void initLanguagesList() {

        ArrayList<Language> languageList = new ArrayList<>();
        languageList.add(new Language("English"));
        languageList.add(new Language("Русский"));
        languageList.add(new Language("Français"));
        languageList.add(new Language("Deutsch"));
        languageList.add(new Language("Italiano"));
        languageList.add(new Language("Español"));

        LanguagesSpinner adapter = new LanguagesSpinner(context, languageList);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
    }


    private class TranslateTask extends AsyncTask<Void, Void, String> {

        private Context context;
        private ProgressDialog progressBar;


        TranslateTask(Context context) {
            this.context = context;
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

                String textToTranslate = editedText.getText().toString();

                text = googleTranslate.translate(textToTranslate, languageTo, languageFrom, getApplicationContext());

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

            translatedText.setText(result);

            progressBar.dismiss();

        }
    }
}
