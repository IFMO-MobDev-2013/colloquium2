package ru.ifmo.rain.loboda.colloq2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

public class SQLRequester {

    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private Context context;

    private static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "colloquium";
    public static final String TABLE_SUBJECTS = "subjects";
    public static final String TABLE_MARKS = "marks";
    public static final String KEY_ID_SUBJECTS = "_id";
    public static final String KEY_ID_MARKS = "_id";
    public static final String KEY_ID_IN_SUBJ = "subj_id";
    public static final String SUBJ_NAME = "name";
    public static final String MARK = "mark";
    public static final String MARK_NAME = "mark_name";

    private static final String[] DATABASE_CREATE =
    {"CREATE TABLE " + TABLE_SUBJECTS + " (" + KEY_ID_SUBJECTS + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJ_NAME + " TEXT NOT NULL);",
     "CREATE TABLE " + TABLE_MARKS + " (" + KEY_ID_MARKS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
           KEY_ID_IN_SUBJ + " INTEGER NOT NULL, " + MARK + " INTEGER NOT NULL, " + MARK_NAME + " TEXT NOT NULL)"};

    public SQLRequester(Context context){
        this.context = context;
    }

    public void open(){
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    public Cursor fetchAllSubjects(){
        return database.query(TABLE_SUBJECTS, null, null, null, null, null, "_id desc");
    }

    public String getNameSubjectById(Integer id){
        Cursor cursor = database.query(TABLE_SUBJECTS, null, "_id="+id, null, null, null, null);
        cursor.moveToNext();
        String result = cursor.getString(cursor.getColumnIndex(SUBJ_NAME));
        cursor.close();
        return result;
    }

    public void deleteMarkById(Integer id){
        database.delete(TABLE_MARKS, "_id=" + id, null);
    }

    public String getNameByMarkId(Integer id){
        Cursor cursor = database.query(TABLE_MARKS, null, "_id="+id, null, null, null, null);
        cursor.moveToNext();
        String result = cursor.getString(cursor.getColumnIndex(MARK_NAME));
        cursor.close();
        return result;
    }

    public int getMarkByMarkId(Integer id){
        Cursor cursor = database.query(TABLE_SUBJECTS, null, "_id="+id, null, null, null, null);
        cursor.moveToNext();
        int result = cursor.getInt(cursor.getColumnIndex(MARK));
        cursor.close();
        return result;
    }

    public void insertMark(Integer subj_id, Integer mark, String name){
        ContentValues values = new ContentValues();
        values.put(KEY_ID_IN_SUBJ, subj_id);
        values.put(MARK, mark);
        values.put(MARK_NAME, name);
        database.insert(TABLE_MARKS, null, values);
    }

    public void updateMarkById(Integer id, String name, Integer mark){
        ContentValues values = new ContentValues();
        values.put(MARK, mark);
        values.put(MARK_NAME, name);
        database.update(TABLE_MARKS, values, "_id="+id, null);
    }

    public void updateNameSubject(Integer id, String name){
        ContentValues values = new ContentValues();
        values.put(SUBJ_NAME, name);
        database.update(TABLE_SUBJECTS, values, "_id=" + id, null);
    }

    public void deleteSubjectById(Integer id){
        database.delete(TABLE_SUBJECTS, "_id=" + id.toString(), null);
    }

    public void insertSubject(String name){
        ContentValues values = new ContentValues();
        values.put(SUBJ_NAME, name);
        database.insert(TABLE_SUBJECTS, null, values);
    }

    public Cursor fetchMarksBySubjectId(Integer id){
        return database.query(TABLE_MARKS, null, KEY_ID_IN_SUBJ + "=" + id, null, null, null, null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for(int i = 0; i < DATABASE_CREATE.length; ++i){
                Log.d("TABLE CREATE", DATABASE_CREATE[i]);
                db.execSQL(DATABASE_CREATE[i]);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKS);
            onCreate(db);
        }
    }
}