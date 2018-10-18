package com.jay.translator.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListView;

import com.jay.translator.R;
import com.jay.translator.ViewSettings;
import com.jay.translator.adapters.SavedTextAdapter;

import java.util.ArrayList;

public class SavedTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_translation);

        initializeRecyclerView();

        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);

        ImageView backgroundImage = findViewById(R.id.saved_text_image_background);

        int image = preferences.getInt("blurImage", R.drawable.london);
        backgroundImage.setImageBitmap(ViewSettings.setImageBlurry(this, getResources().getDrawable(image)));

    }


    private void initializeRecyclerView() {

        ArrayList<String> date = new ArrayList<>();
        date.add("1 Jun 1");
        date.add("1 Feb 1");
        date.add("1 Mar 1");
        date.add("1 Apr 1");
        date.add("1 May 1");
        date.add("1 Jun 1");
        date.add("1 Jul 1");

        ArrayList<String> inputText = new ArrayList<>();
        inputText.add(getResources().getString(R.string.lon));
        inputText.add("hello2");
        inputText.add("hello3");
        inputText.add("hello4");
        inputText.add("world5");
        inputText.add("hello6");
        inputText.add("hello7");

        ArrayList<String> translatedText = new ArrayList<>();
        translatedText.add(getResources().getString(R.string.lon));
        translatedText.add("world2");
        translatedText.add("world3");
        translatedText.add("world4");
        translatedText.add("world5");
        translatedText.add("world6");
        translatedText.add("world7");

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);

        RecyclerView recyclerView = findViewById(R.id.saved_text);
        recyclerView.setLayoutManager(manager);

        SavedTextAdapter adapter = new SavedTextAdapter(date, inputText, translatedText, this);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);

    }
}
