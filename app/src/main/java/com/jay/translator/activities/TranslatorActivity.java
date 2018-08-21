package com.jay.translator.activities;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.jay.translator.LanguageSettings;
import com.jay.translator.R;
import com.jay.translator.ViewSettings;

public class TranslatorActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "TAG";
    private AnimationDrawable toolBarAnimation;
    private FloatingActionButton fab;
    private boolean isSettingsOpen;
    private ImageView backgroundImage;
    private Context context;

    private Animation openSettingsAnimation;
    private Animation closeSettingsAnimation;
    private TableLayout settingsMenu;

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

        settingsMenu = findViewById(R.id.settings_layout);

        fab = findViewById(R.id.fab_settings);

        backgroundImage = findViewById(R.id.image_view_translator_background);

        toolBarAnimation = (AnimationDrawable) toolbarLayout.getBackground();
        toolBarAnimation.setExitFadeDuration(4000);

        LanguageSettings.loadLocale(context);

        setBackground();

        //settings menu is collapsed by default
        isSettingsOpen = false;

        openSettingsAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_from_right_to_x0);
        openSettingsAnimation.setFillAfter(true);
        closeSettingsAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_from_x0_to_right);
        closeSettingsAnimation.setFillAfter(true);

        onSettingsClickListener();

        settingsMenu.setVisibility(View.INVISIBLE);

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


    private void onSettingsClickListener() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isSettingsOpen) {

                    showSettings();

                } else {

                    closeSettings();
                }
            }
        });
    }


    private void showSettings() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                isSettingsOpen = true;

                fab.animate().rotation(360).setDuration(1500).start();

                settingsMenu.setVisibility(View.VISIBLE);

                settingsMenu.setAnimation(openSettingsAnimation);
                openSettingsAnimation.start();
            }
        });
    }


    private void closeSettings() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                isSettingsOpen = false;

                fab.animate().rotation(0).setDuration(1500).start();

                settingsMenu.setVisibility(View.INVISIBLE);

                settingsMenu.setAnimation(closeSettingsAnimation);
                closeSettingsAnimation.start();
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
}
