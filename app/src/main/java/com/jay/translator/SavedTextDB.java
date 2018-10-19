package com.jay.translator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SavedTextDB extends SQLiteOpenHelper {


    public SavedTextDB(@Nullable Context context) {
        super(context, "DB", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table ST (id integer primary key autoincrement, " +
                "data text ," +
                "input text ," +
                "output text ) ; " );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
