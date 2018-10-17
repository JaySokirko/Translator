package com.jay.translator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.jay.translator.R;
import com.jay.translator.adapters.SavedTextAdapter;

public class SavedTextActivity extends AppCompatActivity {

    private String date[] = {
            "12.01.12",
            "13.13.18",
            "14.10.19",
            "14.10.19",
            "14.10.19"
    };

    private String inputText[] = {
            "hello1",
            "hello2",
            "hello3",
            "hello3",
            "hello3"
    };

    private String outputText[] = {
            "example1",
            "example1",
            "example1",
            "example1",
            "example1"
    };

    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_translation);

        listView = findViewById(R.id.saved_text);

        SavedTextAdapter adapter = new SavedTextAdapter(this, date, inputText, outputText);
        listView.setAdapter(adapter);

    }
}
