package com.jay.translator.activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jay.translator.DialogLanguageNotSupported;
import com.jay.translator.DialogNoInternet;
import com.jay.translator.GoogleTranslate;
import com.jay.translator.LanguageSettings;
import com.jay.translator.OnSwipeTouchListener;
import com.jay.translator.R;
import com.jay.translator.ResizeAnimation;
import com.jay.translator.ViewSettings;


import java.util.Locale;

import de.mateware.snacky.Snacky;


public class TranslatorActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "TAG";
    private ImageView fabChoiceLanguage;
    private ImageView fabBackgroundSettings;
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
    private FrameLayout onTouchEventField;
    private LinearLayout swipeSettingsMenu;
    private SeekBar seekBarSpeechSpeed;
    private SeekBar seekBarSpeechFeed;
    private EditText editedText;
    private TextView translatedText;
    private NumberPicker spinnerFrom;
    private NumberPicker spinnerTo;
    private TextView languageFromHint;
    private TextView languageToHint;
    private FrameLayout inputTextFrame;
    private FrameLayout outputTextFrame;

    private AnimationDrawable toolBarAnimation;
    private boolean isSpeechSettingsOpen;
    private boolean isShowTranslatedTextFrame;
    private boolean isShowInputTextFrame;
    private boolean isShowShareSettings;
    private boolean isLanguageSupported;
    private boolean isInputFrameNarrow;
    private boolean isInputFrameStretch;
    private boolean isOutputFrameNarrow;
    private boolean isOutputFrameStretch;
    private boolean isSettingsCall;
    private Context context;
    private ValueAnimator valueAnimator;
    private String languageFrom;
    private String languageTo;
    private TextToSpeech textToSpeech;
    private SharedPreferences.Editor editor;

    private float speechSpeed;
    private float speechFeed;
    private String[] languages;
    private int allowableFrameHeight;
    private int actionBarHeight;
    private int inputTextFrameHeight;
    private int outputTextFrameHeight;
    private int settingsHeight;
    private SharedPreferences preferences;

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
        languageFromHint = findViewById(R.id.language_from);
        languageToHint = findViewById(R.id.language_to);
        inputTextLayout = findViewById(R.id.input_text_layout);
        outputTextLayout = findViewById(R.id.output_text_layout);
        onTouchEventField = findViewById(R.id.container);
        backgroundImage = findViewById(R.id.image_view_translator_background);
        swipeSettingsMenu = findViewById(R.id.swipe_settings_menu);

        seekBarSpeechSpeed = findViewById(R.id.seek_bar_speech_speed);
        seekBarSpeechSpeed.setMax(100);
        seekBarSpeechSpeed.setProgress(50);

        seekBarSpeechFeed = findViewById(R.id.seek_bar_speech_feed);
        seekBarSpeechFeed.setMax(100);
        seekBarSpeechFeed.setProgress(50);

        inputTextFrame = findViewById(R.id.input_text_frame);
        outputTextFrame = findViewById(R.id.output_text_frame);

        spinnerFrom = findViewById(R.id.spinner_from);
        spinnerTo = findViewById(R.id.spinner_to);

        toolBarAnimation = (AnimationDrawable) toolbarLayout.getBackground();
        toolBarAnimation.setExitFadeDuration(4000);


        isSpeechSettingsOpen = false;

        isShowTranslatedTextFrame = false;

        isShowInputTextFrame = true;

        isShowShareSettings = false;

        isLanguageSupported = true;

        isInputFrameNarrow = false;
        isInputFrameStretch = false;

        isOutputFrameNarrow = false;
        isOutputFrameStretch = false;

        isSettingsCall = false;

        outputTextLayout.setVisibility(View.GONE);

        onSwipeTouchListener();

        initLanguagesSpinners();

        onSelectLanguageFromListener();
        onSelectLanguageToListener();

        preferences = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        editor = preferences.edit();

        spinnerFrom.setValue(preferences.getInt("selectionFrom", 0));
        spinnerTo.setValue(preferences.getInt("selectionTo", 1));

        languageFrom = preferences.getString("languageFrom", GoogleTranslate.ENGLISH);
        languageTo = preferences.getString("languageTo", GoogleTranslate.RUSSIAN);

        editedText.setText(preferences.getString("editedText", ""));

        initializeTextToSpeech(languageTo);
        onPitchChangeListener();

        setLayoutParamsToTextViews();

        languageFromHint.setText(languages[preferences.getInt("selectionFrom", 0)]);
        languageToHint.setText(languages[preferences.getInt("selectionTo", 1)]);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        allowableFrameHeight = height - 400 - actionBarHeight;

        int image = preferences.getInt("blurImage", R.drawable.london);
        backgroundImage.setImageBitmap(ViewSettings.setImageBlurry(this, getResources().getDrawable(image)));
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

        editor.putString("editedText", editedText.getText().toString());
        editor.apply();

        //Close the Text to Speech Library
        if (textToSpeech != null) {

            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();
    }


    private void showTutorial(){

        boolean isAppRunFirstTime = preferences.getBoolean("isTranslatorRunFirstTime", true);

        //todo tutorial

        editor.putBoolean("isTranslatorRunFirstTime",false);
        editor.apply();
    }


    /**
     * Share with translated text
     *
     * @param view share button
     */
    public void sendText(View view) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, translatedText.getText());
        startActivity(Intent.createChooser(sharingIntent, "Title"));
    }


    /**
     * Start saved text activity
     *
     * @param view saved text button
     */
    public void startSavedTextActivity(View view) {

        startActivity(new Intent(this, SavedTextActivity.class));
    }


    /**
     * Set prompts in the app bar, which languages were selected
     */
    private void setLayoutParamsToTextViews() {

        //get app bar height
        TypedValue typedValue = new TypedValue();

        actionBarHeight = 0;

        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }

        //set views to center
        CollapsingToolbarLayout.LayoutParams p1 = (CollapsingToolbarLayout.LayoutParams) languageFromHint.getLayoutParams();
        p1.setMargins(0, 0, 0, actionBarHeight / 2);
        languageFromHint.setLayoutParams(p1);

        CollapsingToolbarLayout.LayoutParams p2 = (CollapsingToolbarLayout.LayoutParams) languageToHint.getLayoutParams();
        p2.setMargins(0, 0, 0, actionBarHeight / 2);
        languageToHint.setLayoutParams(p2);
    }


    private void narrowInputFrame(final View view) {

        inputTextFrameHeight = view.getHeight();

        if (inputTextFrameHeight >= allowableFrameHeight) {

            ResizeAnimation resizeAnimation = new ResizeAnimation(view, view.getHeight() / 2);
            resizeAnimation.setDuration(600);
            view.startAnimation(resizeAnimation);
        }
    }

    private void stretchInputFrame(final View view) {

        if (inputTextFrameHeight > view.getHeight()) {

            ResizeAnimation resizeAnimation = new ResizeAnimation(view, view.getHeight() * 2);
            resizeAnimation.setDuration(600);
            view.startAnimation(resizeAnimation);
        }
    }


    private void narrowOutputFrame(View view) {

        outputTextFrameHeight = view.getHeight();

        if (outputTextFrameHeight >= allowableFrameHeight) {

            ResizeAnimation resizeAnimation = new ResizeAnimation(view, view.getHeight() / 2);
            resizeAnimation.setDuration(600);
            view.startAnimation(resizeAnimation);
        }
    }

    private void stretchOutputFrame(View view) {

        if (outputTextFrameHeight > view.getHeight()) {

            ResizeAnimation resizeAnimation = new ResizeAnimation(view, view.getHeight() * 2);
            resizeAnimation.setDuration(600);
            view.startAnimation(resizeAnimation);
        }
    }


    private void setDefaultSize(View view) {

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        view.setLayoutParams(params);
    }


    private void onShowSettingsNarrow(View view) {

        int height = view.getHeight();

        ResizeAnimation resizeAnimation = new ResizeAnimation(view, height / 2);
        resizeAnimation.setDuration(600);
        view.startAnimation(resizeAnimation);

        isSettingsCall = true;
    }

    private void onCloseSettingsStretch(View view) {

        int height = view.getHeight();

        ResizeAnimation resizeAnimation = new ResizeAnimation(view, height * 2);
        resizeAnimation.setDuration(600);
        view.startAnimation(resizeAnimation);

        isSettingsCall = false;
    }


    private void startTranslate() {

        if (isOnline()) {

            new TranslateTask(context).execute();

            textToSpeech.setPitch(speechFeed);
            textToSpeech.setSpeechRate(speechSpeed);

        } else {

            alertDialogNoInternetConnection();
        }
    }


    public void setLanguage(View view) {

        startActivity(new Intent(context, ChoiceLanguageActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


    //on translate button click
    public void onTranslateClick(View view) {

        if (!languageFrom.equals(languageTo)) {

            if (editedText.getText().toString().equals("")) {

                buildSnackBar(getResources().getString(R.string.first_enter_the_text));

            } else {

                startTranslate();
            }
        } else {

            buildSnackBar(getResources().
                    getString(R.string.the_language_of_translation_coincides_with_the_original));
        }
    }


    //on clear text button click
    public void onClearTextClick(View view) {
        editedText.setText("");
        setDefaultSize(inputTextFrame);
    }


    public void onSpeechSettingsClick(View view) {

        if (!isSpeechSettingsOpen) {

            openSpeechSettings();

            settingsHeight = outputTextFrame.getHeight();

            if (settingsHeight >= allowableFrameHeight) {
                onShowSettingsNarrow(outputTextFrame);
            }

        } else {

            closeSpeechSettings();

            if (isSettingsCall) {
                onCloseSettingsStretch(outputTextFrame);
            }
        }
    }


    public void onSpeechSpeedClick(View view) {

        buildSnackBar(getResources().getString(R.string.change_speech_speed));
    }


    public void onSpeechFeedClick(View view) {

        buildSnackBar(getResources().getString(R.string.change_speech_feed));
    }


    public void onShareClick(View view) {

        if (!isShowShareSettings) {

            showShareSettings();

            settingsHeight = outputTextFrame.getHeight();

            if (settingsHeight >= allowableFrameHeight) {
                onShowSettingsNarrow(outputTextFrame);
            }

        } else {

            closeShareSettings();

            if (isSettingsCall) {
                onCloseSettingsStretch(outputTextFrame);
            }
        }
    }


    public void onSpeechText(View view) {

        String text;
        text = translatedText.getText().toString();

        if (isLanguageSupported) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {

            DialogLanguageNotSupported dialog = new DialogLanguageNotSupported(this);
            dialog.show();
        }
    }


    public void onViewSettings(View view) {

        //todo on saved translations
//        startActivity(new Intent(context, ChoiceSettingsActivity.class));
    }


    public void onStartSpeechActivity(View view) {

        startActivity(new Intent(this, SpeechActivity.class));
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        //turning the maximum value of the verticalOffset in the number = 1.0f,
        // and the minimum in 0.0
        float a = (float) (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange()) * -1;
        float b = (a / (appBarLayout.getTotalScrollRange() / 2)) / 2;

        float x = -1 * verticalOffset;
        float z = x / appBarLayout.getTotalScrollRange();

        spinnerFrom.animate().alpha(b).start();
        spinnerTo.animate().alpha(b).start();
        languageFromHint.animate().alpha(z).start();
        languageToHint.animate().alpha(z).start();

        if (b < 0.3) {
            spinnerFrom.setVisibility(View.GONE);
            spinnerTo.setVisibility(View.GONE);
            languageFromHint.setVisibility(View.VISIBLE);
            languageToHint.setVisibility(View.VISIBLE);

            isInputFrameNarrow = false;
            isOutputFrameNarrow = false;

            if (isInputFrameStretch) {
                stretchInputFrame(inputTextFrame);
                isInputFrameStretch = false;
            }

            if (isOutputFrameStretch) {
                stretchOutputFrame(outputTextFrame);
                closeShareSettings();
                closeSpeechSettings();
                isOutputFrameStretch = false;
            }
        }

        if (b >= 0.3) {
            spinnerFrom.setVisibility(View.VISIBLE);
            spinnerTo.setVisibility(View.VISIBLE);
            languageFromHint.setVisibility(View.GONE);
            languageToHint.setVisibility(View.GONE);
        }

        if (b > 0.9) {

            isInputFrameStretch = true;
            isOutputFrameStretch = true;

            if (!isInputFrameNarrow) {
                narrowInputFrame(inputTextFrame);
                isInputFrameNarrow = true;
            }

            if (!isOutputFrameNarrow) {
                narrowOutputFrame(outputTextFrame);
                isOutputFrameNarrow = true;
            }
        }
    }


    private void showShareSettings() {

        float x = getResources().getDimension(R.dimen.standard_95);
        float y = fabSpeechSettings.getHeight() + getResources().getDimension(R.dimen.standard_50);

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


    private void openSpeechSettings() {

        fabSpeechSpeed.setVisibility(View.VISIBLE);
        fabSpeechFeed.setVisibility(View.VISIBLE);
        seekBarSpeechSpeed.setVisibility(View.VISIBLE);

        //set speech speed button under share button
        final float x = fabSpeechSettings.getX() - fabShare.getX();
        final float y = fabShare.getHeight() + getResources().getDimension(R.dimen.standard_50);

        //set speech feed button under speech speed
        float y1 = y + fabShare.getHeight() + getResources().getDimension(R.dimen.standard_21);

        fabSpeechSpeed.animate().translationX(-x).translationY(y).start();

        fabSpeechFeed.animate().translationX(-x).translationY(y1).start();

        seekBarSpeechSpeed.animate().translationY(y).start();
        seekBarSpeechFeed.animate().translationY(y1).start();

        final float seekBarWidth = getResources().getDimension(R.dimen.standard_220);
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


    private void setBackground() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ViewSettings.setBackgroundImage(LanguageSettings.getLanguage(), backgroundImage);
                backgroundImage.setImageBitmap(ViewSettings.setImageBlurry(context, backgroundImage.getDrawable()));
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


        swipeSettingsMenu.setOnTouchListener(new OnSwipeTouchListener(context) {

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                swipeSettingsMenu.animate()
                        .translationX(getResources().getDimension(R.dimen.standard_145))
                        .start();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                swipeSettingsMenu.animate().translationX(0).start();
            }
        });
    }


    private void onSelectLanguageFromListener() {

        spinnerFrom.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                switch (newVal) {

                    case 0:
                        languageFrom = GoogleTranslate.ENGLISH;
                        languageFromHint.setText("English");
                        break;

                    case 1:
                        languageFrom = GoogleTranslate.RUSSIAN;
                        languageFromHint.setText("Русский");
                        break;

                    case 2:
                        languageFrom = GoogleTranslate.FRENCH;
                        languageFromHint.setText("Français");
                        break;

                    case 3:
                        languageFrom = GoogleTranslate.GERMAN;
                        languageFromHint.setText("Deutsch");
                        break;

                    case 4:
                        languageFrom = GoogleTranslate.ITALIAN;
                        languageFromHint.setText("Italiano");
                        break;

                    case 5:
                        languageFrom = GoogleTranslate.SPANISH;
                        languageFromHint.setText("Español");
                        break;

                    case 6:
                        languageFrom = GoogleTranslate.POLISH;
                        languageFromHint.setText("Polish");
                        break;
                }

                editor.putInt("selectionFrom", newVal);
                editor.putString("languageFrom", languageFrom);
                editor.apply();
            }
        });
    }


    public void onSelectLanguageToListener() {

        spinnerTo.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                switch (newVal) {

                    case 0:
                        languageTo = GoogleTranslate.ENGLISH;
                        languageToHint.setText("English");
                        break;

                    case 1:
                        languageTo = GoogleTranslate.RUSSIAN;
                        languageToHint.setText("Русский");
                        break;

                    case 2:
                        languageTo = GoogleTranslate.FRENCH;
                        languageToHint.setText("Français");
                        break;

                    case 3:
                        languageTo = GoogleTranslate.GERMAN;
                        languageToHint.setText("Deutsch");
                        break;

                    case 4:
                        languageTo = GoogleTranslate.ITALIAN;
                        languageToHint.setText("Italiano");
                        break;

                    case 5:
                        languageTo = GoogleTranslate.SPANISH;
                        languageToHint.setText("Español");
                        break;

                    case 6:
                        languageTo = GoogleTranslate.POLISH;
                        languageToHint.setText("Polish");
                        break;
                }

                editor.putInt("selectionTo", newVal);
                editor.putString("languageTo", languageTo);
                editor.apply();

                initializeTextToSpeech(languageTo);
            }
        });

    }


    public void initializeTextToSpeech(final String locale) {

        if (textToSpeech != null) {

            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

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
        }, "com.google.android.tts");
    }


    private void onPitchChangeListener() {

        seekBarSpeechFeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                speechFeed = (float) seekBar.getProgress() / 50;

                if (speechFeed < 0.1)
                    speechFeed = 0.1f;

                textToSpeech.setPitch(speechFeed);
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

                speechSpeed = (float) seekBarSpeechSpeed.getProgress() / 50;

                if (speechSpeed < 0.1)
                    speechSpeed = 0.1f;

                textToSpeech.setSpeechRate(speechSpeed);
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

        DialogNoInternet dialog = new DialogNoInternet(this);
        dialog.show();
    }


    private void initLanguagesSpinners() {

        languages = new String[]{"English", "Русский", "Français", "Deutsch", "Italiano", "Español"
                , "Polish"};

        spinnerFrom.setMinValue(0);
        spinnerFrom.setMaxValue(languages.length - 1);
        spinnerFrom.setDisplayedValues(languages);
        spinnerFrom.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        spinnerTo.setMinValue(0);
        spinnerTo.setMaxValue(languages.length - 1);
        spinnerTo.setDisplayedValues(languages);
        spinnerTo.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }


    private void buildSnackBar(String title) {
        Snacky.builder()
                .setActivity(TranslatorActivity.this)
                .setText(title)
                .setTextColor(getResources().getColor(R.color.colorText))
                .centerText()
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setDuration(Snacky.LENGTH_SHORT)
                .build()
                .show();
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
            showTranslatedTextFrame();
            progressBar.dismiss();

        }
    }
}
