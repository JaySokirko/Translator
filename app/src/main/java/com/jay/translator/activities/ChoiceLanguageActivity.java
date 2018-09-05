package com.jay.translator.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jay.translator.LanguageSettings;
import com.jay.translator.R;
import com.jay.translator.adapters.ChoiceLanguageAdapter;

import de.mateware.snacky.Snacky;

public class ChoiceLanguageActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private String[] languages = {
            "English",
            "Русский",
            "Deutsch",
            "Français",
            "Español",
            "Italiano"};

    private int[] images = {
            R.drawable.london,
            R.drawable.moscow,
            R.drawable.berlin,
            R.drawable.paris,
            R.drawable.madrid,
            R.drawable.venice};


    private int[] flags = {
            R.drawable.ic_united_kingdom_flag,
            R.drawable.ic_russian_flag,
            R.drawable.ic_germany_flag,
            R.drawable.ic_french_flag,
            R.drawable.ic_spanish_flag,
            R.drawable.ic_italian_flag
    };


    private ListView listView;
    private AnimationDrawable toolBarAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set app language
        LanguageSettings.loadLocale(this);

        setContentView(R.layout.activity_choice_language);

        listView = findViewById(R.id.list_view_choice_language);
        FloatingActionButton next = findViewById(R.id.button_choice_language_next);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_choice_language);

        ChoiceLanguageAdapter adapter = new ChoiceLanguageAdapter(ChoiceLanguageActivity.this,
                languages, images, flags);

        listView.setAdapter(adapter);

        toolBarAnimation = (AnimationDrawable) toolbar.getBackground();
        toolBarAnimation.setExitFadeDuration(4000);

        //on list view click listener
        onListClickListener();

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


    private void onListClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        LanguageSettings.setLocale("en", ChoiceLanguageActivity.this);
                        setLanguage();
                        break;

                    case 1:
                        LanguageSettings.setLocale("ru", ChoiceLanguageActivity.this);
                        setLanguage();
                        break;

                    case 2:
                        LanguageSettings.setLocale("de", ChoiceLanguageActivity.this);
                        setLanguage();
                        break;

                    case 3:
                        LanguageSettings.setLocale("fr", ChoiceLanguageActivity.this);
                        setLanguage();
                        break;

                    case 4:
                        LanguageSettings.setLocale("es", ChoiceLanguageActivity.this);
                        setLanguage();
                        break;

                    case 5:
                        LanguageSettings.setLocale("it", ChoiceLanguageActivity.this);
                        setLanguage();
                        break;
                }
            }
        });
    }


    private void setLanguage() {

        Snacky.builder()
                .setActivity(ChoiceLanguageActivity.this)
                .setText(R.string.please_wait)
                .setTextColor(getResources().getColor(R.color.colorText))
                .centerText()
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setDuration(Snacky.LENGTH_SHORT)
                .build()
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ChoiceLanguageActivity.this,
                        ChoiceLanguageActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }, 1000);
    }


    public void startTranslatorActivity(final View view) {

        view.setBackground(getDrawable(R.drawable.view_rounded));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(ChoiceLanguageActivity.this, TranslatorActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                ChoiceLanguageActivity.this.finish();
            }
        }, 0);


    }
}
