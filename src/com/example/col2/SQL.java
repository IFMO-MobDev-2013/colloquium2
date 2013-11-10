package com.example.col2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Date: 08.11.13
 *
 * @author Margarita Markina
 */
public class SQL extends SQLiteOpenHelper {


    public SQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ob(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mark TEXT)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "Math");
        contentValues.put("mark", "30");
        db.insert("ob", null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
