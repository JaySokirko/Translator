package com.jay.translator.activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.jay.translator.LanguageSettings;
import com.jay.translator.OnSwipeTouchListener;
import com.jay.translator.R;
import com.jay.translator.ViewSettings;


public class TranslatorActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "TAG";
    private FloatingActionButton fabSettings;
    private FloatingActionButton fabChoiceLanguage;
    private FloatingActionButton fabBackgroundSettings;
    private FloatingActionButton fabStartTranslate;
    private FloatingActionButton fabSpeechSettings;
    private FloatingActionButton fabSpeechFeed;
    private FloatingActionButton fabSpeechSpeed;
    private ImageView backgroundImage;
    private CoordinatorLayout inputTextLayout;
    private CoordinatorLayout outputTextLayout;
    private CoordinatorLayout onTouchEventField;

    private AnimationDrawable toolBarAnimation;
    private boolean isSettingsOpen;
    private boolean isShowTranslatedTextFrame;
    private boolean isShowInputTextFrame;
    private Context context;
    private ValueAnimator valueAnimator;


    @SuppressLint("ClickableViewAccessibility")
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

        inputTextLayout = findViewById(R.id.input_text_layout);

        outputTextLayout = findViewById(R.id.output_text_layout);

        onTouchEventField = findViewById(R.id.container);

        backgroundImage = findViewById(R.id.image_view_translator_background);

        toolBarAnimation = (AnimationDrawable) toolbarLayout.getBackground();
        toolBarAnimation.setExitFadeDuration(4000);

        LanguageSettings.loadLocale(context);

        setBackground();

        //settings menu is collapsed by default
        isSettingsOpen = false;

        isShowTranslatedTextFrame = false;

        isShowInputTextFrame = true;

        outputTextLayout.setVisibility(View.GONE);
        fabSpeechSpeed.setVisibility(View.GONE);

        onSwipeTouchListener();

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


    public void onTranslateClickListener(View view) {

        switch (view.getId()) {

            case R.id.fab_translation:

                showTranslatedTextFrame();
                break;

            case R.id.fab_cancel:

                showInputTextFrame();
                break;
        }
    }


    public void onSpeechSettingsClickListener(View view){

        fabSpeechSpeed.setVisibility(View.VISIBLE);

        valueAnimator = ValueAnimator.ofFloat(fabSpeechSettings.getX(),0f);
        valueAnimator.setDuration(800);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fabSpeechSpeed.setTranslationX((float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();

    }

    public void onSettingsClickListener(View view) {

        if (!isSettingsOpen) {

            showSettings();
        } else {

            closeSettings();
        }
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


}
