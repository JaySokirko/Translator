package com.jay.translator.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.jay.translator.R;
import com.jay.translator.SavedTextDB;
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

        SavedTextDB db;
        SQLiteDatabase sqLiteDatabase;

        ArrayList<String> date = new ArrayList<>();

        ArrayList<String> inputText = new ArrayList<>();

        ArrayList<String> translatedText = new ArrayList<>();

        db = new SavedTextDB(this);
        sqLiteDatabase = db.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query("ST", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            do {
                date.add(cursor.getString(cursor.getColumnIndex("data")));
                inputText.add(cursor.getString(cursor.getColumnIndex("input")));
                translatedText.add(cursor.getString(cursor.getColumnIndex("output")));

            } while (cursor.moveToNext());
        }

        cursor.close();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);

        RecyclerView recyclerView = findViewById(R.id.saved_text);
        recyclerView.setLayoutManager(manager);

        SavedTextAdapter adapter = new SavedTextAdapter(date, inputText, translatedText, this);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);

    }
}
