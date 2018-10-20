package com.jay.translator.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jay.translator.R;
import com.jay.translator.SavedTextDB;
import com.jay.translator.ViewSettings;
import com.jay.translator.adapters.SavedTextAdapter;

import java.util.ArrayList;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;

public class SavedTextActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_translation);

        preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        editor = preferences.edit();

        ImageView backgroundImage = findViewById(R.id.saved_text_image_background);

        int image = preferences.getInt("blurImage", R.drawable.london);
        backgroundImage.setImageBitmap(ViewSettings.setImageBlurry(this, getResources().getDrawable(image)));

        initializeRecyclerView();
    }



    private void initializeRecyclerView() {

        SavedTextDB db;
        SQLiteDatabase sqLiteDatabase;

        ArrayList<String> date = new ArrayList<>();

        ArrayList<String> inputText = new ArrayList<>();

        final ArrayList<String> translatedText = new ArrayList<>();

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

        recyclerView = findViewById(R.id.saved_text);
        recyclerView.setLayoutManager(manager);

        SavedTextAdapter adapter = new SavedTextAdapter(date, inputText, translatedText, this);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);

        //show tutorial if app run first time
        boolean isAppRunFirstTime = preferences.getBoolean("isSavedActivityRunFirstTime", true);

        if (isAppRunFirstTime) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(0);
                        FrameLayout layout = holder.itemView.findViewById(R.id.translated_text);

                        new GuideView.Builder(SavedTextActivity.this)
                                .setTitle(getResources().getString(R.string.tap_here_to_delete))
                                .setGravity(GuideView.Gravity.auto) //optional
                                .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
                                .setTargetView(layout)
                                .setTitleTextSize(14)//optional
                                .build()
                                .show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },1000);

            editor.putBoolean("isSavedActivityRunFirstTime", false);
            editor.apply();
        }
    }
}
