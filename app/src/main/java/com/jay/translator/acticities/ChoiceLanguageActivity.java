package com.jay.translator.acticities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.jay.translator.R;
import com.jay.translator.adapters.choice_language.ChoiceLanguageAdapter;

import java.util.Locale;

public class ChoiceLanguageActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private String[] languages = {"English",
            "Русский",
            "Deutsch",
            "Français",
            "Español",
            "Italiano"};

    private int[] images = {R.drawable.ic_united_kingdom_flag,
            R.drawable.ic_russian_flag,
            R.drawable.ic_germany_flag,
            R.drawable.ic_french_flag,
            R.drawable.ic_spanish_flag,
            R.drawable.ic_italian_flag};

    private ListView listView;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_language);

        loadLocale();

        listView = findViewById(R.id.list_view_choice_language);
        backgroundImage = findViewById(R.id.image_view_choice_language);

        String language = loadLocale();
        setBackgroundImage(language);

        ChoiceLanguageAdapter adapter = new ChoiceLanguageAdapter(ChoiceLanguageActivity.this, languages, images);

        listView.setAdapter(adapter);

        //on list view click listener
        onListClickListener();


    }


    private void onListClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        setLocale("en");
                        startActivity(new Intent(ChoiceLanguageActivity.this,
                                ChoiceLanguageActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    case 1:
                        setLocale("ru");
                        startActivity(new Intent(ChoiceLanguageActivity.this,
                                ChoiceLanguageActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    case 2:
                         setLocale("de");
                        startActivity(new Intent(ChoiceLanguageActivity.this,
                                ChoiceLanguageActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    case 3:
                         setLocale("fr");
                        startActivity(new Intent(ChoiceLanguageActivity.this,
                                ChoiceLanguageActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    case 4:
                         setLocale("es");
                        startActivity(new Intent(ChoiceLanguageActivity.this,
                                ChoiceLanguageActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    case 5:
                         setLocale("it");
                        startActivity(new Intent(ChoiceLanguageActivity.this,
                                ChoiceLanguageActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                }
            }
        });
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;

        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();

        editor.putString("language",language);

        editor.apply();
    }

    public String loadLocale(){

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("language","en");
        setLocale(language);

        return language;

    }


    public void setBackgroundImage(String language){

        switch (language) {
            case "en":
                backgroundImage.setImageResource(R.drawable.london);
                break;
            case "ru":
                backgroundImage.setImageResource(R.drawable.moscow);
                break;
            case "de":
                backgroundImage.setImageResource(R.drawable.germany);
                break;
            case "fr":
                backgroundImage.setImageResource(R.drawable.paris);
                break;
            case "es":
                backgroundImage.setImageResource(R.drawable.madrid);
                break;
            case "it":
                backgroundImage.setImageResource(R.drawable.italy);
                break;
        }
    }
}
