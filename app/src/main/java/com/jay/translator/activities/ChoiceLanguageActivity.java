package com.jay.translator.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
            "Italiano",
            "Polish"};

    private int[] images = {
            R.drawable.london,
            R.drawable.moscow,
            R.drawable.berlin,
            R.drawable.paris,
            R.drawable.madrid,
            R.drawable.venice,
            R.drawable.warsaw};


    private int[] flags = {
            R.drawable.ic_united_kingdom_flag,
            R.drawable.ic_russian_flag,
            R.drawable.ic_germany_flag,
            R.drawable.ic_french_flag,
            R.drawable.ic_spanish_flag,
            R.drawable.ic_italian_flag,
            R.drawable.ic_poland_flag
    };

    private TextView appBarTV;

    private ListView listView;
    private AnimationDrawable toolBarAnimation;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set app language
        LanguageSettings.loadLocale(this);

        setContentView(R.layout.activity_choice_language);

        listView = findViewById(R.id.list_view_choice_language);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_choice_language);

        appBarTV = findViewById(R.id.app_bar_choice_language_tv);

        ChoiceLanguageAdapter adapter = new ChoiceLanguageAdapter(ChoiceLanguageActivity.this,
                languages, images, flags);

        listView.setAdapter(adapter);

        toolBarAnimation = (AnimationDrawable) toolbar.getBackground();
        toolBarAnimation.setExitFadeDuration(4000);

        preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        editor = preferences.edit();

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
                        appBarTV.setText(getResources().getString(R.string.select_your_language));
                        break;

                    case 1:
                        LanguageSettings.setLocale("ru", ChoiceLanguageActivity.this);
                        appBarTV.setText(getResources().getString(R.string.select_your_language));
                        break;

                    case 2:
                        LanguageSettings.setLocale("de", ChoiceLanguageActivity.this);
                        appBarTV.setText(getResources().getString(R.string.select_your_language));
                        break;

                    case 3:
                        LanguageSettings.setLocale("fr", ChoiceLanguageActivity.this);
                        appBarTV.setText(getResources().getString(R.string.select_your_language));
                        break;

                    case 4:
                        LanguageSettings.setLocale("es", ChoiceLanguageActivity.this);
                        appBarTV.setText(getResources().getString(R.string.select_your_language));
                        break;

                    case 5:
                        LanguageSettings.setLocale("it", ChoiceLanguageActivity.this);
                        appBarTV.setText(getResources().getString(R.string.select_your_language));
                        break;

                    case 6:
                        LanguageSettings.setLocale("pl", ChoiceLanguageActivity.this);
                        appBarTV.setText(getResources().getString(R.string.select_your_language));
                        break;
                }
                editor.putInt("blurImage",images[position]);
                editor.apply();
            }
        });
    }


    public void acceptLanguageChoice(View view){

        startActivity(new Intent(this, SelectAppActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
