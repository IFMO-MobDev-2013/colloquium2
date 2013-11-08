package com.example.PashaAC.Diary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LessonsDataBase {
    public SQLiteDatabase sqLiteDatabase;
    private Context context;
    private DatabaseHelper databaseHelper;
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "subjects.db";
    public static final String TABLE_NAME = "subjects";
    public static final String KEY_PART = "part";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_MARK = "mark";
    public static final String KEY_ID = "_id";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_SUBJECT + " TEXT NOT NULL, " + KEY_PART + " TEXT NOT NULL, " + KEY_MARK + " TEXT NOT NULL);";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    public LessonsDataBase(Context context) {
        this.context = context;
    }

    public LessonsDataBase open() throws SQLiteException {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    private void updateMainSubject(String s, int plusscore) {
        Cursor cursor = this.sqLiteDatabase.query(LessonsDataBase.TABLE_NAME, new String[] {
                LessonsDataBase.KEY_ID, LessonsDataBase.KEY_SUBJECT, LessonsDataBase.KEY_PART, LessonsDataBase.KEY_MARK},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            // GET COLUMN INDICES + VALUES OF THOSE COLUMNS
            int id = cursor.getInt(cursor.getColumnIndex(LessonsDataBase.KEY_ID));
            String subject = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_SUBJECT));
            String part = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_PART));
            String mark = cursor.getString(cursor.getColumnIndex(LessonsDataBase.KEY_MARK));
            if (subject.equals(s) && part.equals("")) {
                int tmp = Integer.parseInt(mark);
                tmp += plusscore;
                this.updateItem(id, subject, part, tmp);
            }
        }
    }

    public long insertSubject(String subject, String part, int mark) throws SQLiteException  {
        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT, subject);
        values.put(KEY_PART, part);
        values.put(KEY_MARK, mark);
        updateMainSubject(subject, mark);
        return sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    public void updateItem(int id, String subject, String part, int mark) throws SQLiteException {
        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT, subject);
        values.put(KEY_PART, part);
        values.put(KEY_MARK, mark);
        sqLiteDatabase.update(TABLE_NAME, values, KEY_ID + "="+(new Integer(id)).toString(), null);
    }

    public boolean deleteItem(int articleId) throws SQLiteException {
        return sqLiteDatabase.delete(TABLE_NAME, KEY_ID + "=" +  (new Integer(articleId)).toString(), null) > 0;
    }

    public void deleteAllItem() throws SQLiteException {
        Cursor cursor = this.sqLiteDatabase.query(LessonsDataBase.TABLE_NAME, new String[] {
                LessonsDataBase.KEY_ID, LessonsDataBase.KEY_SUBJECT, LessonsDataBase.KEY_PART, LessonsDataBase.KEY_MARK},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            // GET COLUMN INDICES + VALUES OF THOSE COLUMNS
            int id = cursor.getInt(cursor.getColumnIndex(LessonsDataBase.KEY_ID));
            deleteItem(id);
        }
    }

    public void close() {
        databaseHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("TABLE CREATE", DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
            onCreate(db);
        }
    }


}
