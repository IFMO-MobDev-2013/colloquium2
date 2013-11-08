package ru.zulyaev.ifmo.colloquium2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * author: zulyaev
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "JournalDB";
    private static final int version = 3;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SubjectsTable.init(db);
        ScoresTable.init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + SubjectsTable.TABLE_NAME);
        db.execSQL("drop table if exists " + ScoresTable.TABLE_NAME);
        onCreate(db);
    }
}
