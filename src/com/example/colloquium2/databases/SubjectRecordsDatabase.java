package com.example.colloquium2.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Genyaz
 * Date: 08.11.13
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class SubjectRecordsDatabase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String _ID = "_id";
    public static final String DATABASE_NAME = "subjectrecordsdb";
    public static final String SUBJECT_ID = "subject_id";
    public static final String RECORD_NAME = "name";
    public static final String RECORD_POINTS = "points";

    public static final String CREATE_DATABASE = "CREATE TABLE " + DATABASE_NAME
            + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SUBJECT_ID + " INTEGER, "
            + RECORD_NAME + " TEXT, " + RECORD_POINTS + " INTEGER);";

    public static final String DROP_DATABASE = "DROP TABLE IF EXISTS " + DATABASE_NAME;

    public SubjectRecordsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            db.execSQL(DROP_DATABASE);
            onCreate(db);
        }
    }
}
