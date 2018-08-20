package com.jay.translator.activities;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jay.translator.LanguageSettings;
import com.jay.translator.R;
import com.jay.translator.ViewSettings;
import com.jay.translator.fragments.EmptyFragment;
import com.jay.translator.fragments.SettingsTranslatorFragment;

public class TranslatorActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "TAG";
    private AnimationDrawable toolBarAnimation;
    private FloatingActionButton fab;
    private boolean isSettingsOpen;
    private ImageView backgroundImage;
    private Context context;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment settingsFragment;
    private Fragment emptyFragment;
    private Animation animation;

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

        fab = findViewById(R.id.fab_settings);

        backgroundImage = findViewById(R.id.image_view_translator_background);

        toolBarAnimation = (AnimationDrawable) toolbarLayout.getBackground();
        toolBarAnimation.setExitFadeDuration(4000);

        LanguageSettings.loadLocale(context);

        setBackground();

        //settings menu is collapsed by default
        isSettingsOpen = false;

        settingsFragment = new SettingsTranslatorFragment();
        emptyFragment = new EmptyFragment();
        fragmentManager = getSupportFragmentManager();

        animation = AnimationUtils.loadAnimation(context, R.anim.anim_from_x0_to_right);

        onSettingsClickListener();

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

                fragmentTransaction = fragmentManager.beginTransaction();

                if (!isSettingsOpen) {
//                    if (fragmentManager.findFragmentByTag(SettingsTranslatorFragment.FRAGMENT_SETTINGS) == null) {
                    showSettings();
//                    }
                } else {
//                    if (fragmentManager.findFragmentByTag(SettingsTranslatorFragment.FRAGMENT_SETTINGS) != null) {
                    closeSettings();
//                    }
                }

                fragmentTransaction.commit();
            }
        });
    }


    private void showSettings() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                isSettingsOpen = true;

                fab.animate().rotation(360).setDuration(1500).start();

                fragmentTransaction.setCustomAnimations(R.anim.anim_from_right_to_x0,
                        R.anim.anim_from_x0_to_right,
                        R.anim.anim_from_x0_to_right,
                        R.anim.anim_from_right_to_x0);

                fragmentTransaction.replace(R.id.fragment_сonteiner_settings, settingsFragment);
                fragmentTransaction.addToBackStack(null);
            }
        });
    }


    private void closeSettings() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                isSettingsOpen = false;

                fab.animate().rotation(0).setDuration(1500).start();

                fragmentTransaction.replace(R.id.fragment_сonteiner_settings, emptyFragment);

            }
        });
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
            //if app bar is collapsed hide settings view
            if (fragmentManager.findFragmentByTag(SettingsTranslatorFragment.FRAGMENT_SETTINGS) != null) {
//                closeSettings();
            }
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
