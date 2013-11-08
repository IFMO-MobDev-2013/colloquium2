package com.example.col2;


import android.*;
import android.R;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class MarkDataBase {

    private static final String DATABASE_NAME = "mark_database.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase myDataBase;
    private DataBaseHelper dataBaseHelper;
    private Context context;
    private static int counterTables = 0;

    public static final String TITLE =        "title";
    public static final String DESCRIPTION =  "description";
    public static final String DATE =         "date";
    public static final String NAME =         "name";
    public static final String LINK =         "link";
    public static final String TABLE_NAME =   "subjects";
    public static final String TRUE_NAME =    "true_name";
    public static final String SUBJECT_NAME = "subject_name";
    public static final String SCORE =        "score";
    public static final String SUM_SCORE =    "sum_score";

    public MarkDataBase(Context context) {
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();
        dataBaseHelper.close();
        myDataBase.close();
    }

    public ArrayList<HashMap<String, String>> getParts(String name) {
        ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        Cursor cursor = myDataBase.query(name, new String[] {
                DataBaseHelper._ID, MarkDataBase.SUBJECT_NAME, MarkDataBase.SCORE },
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            map = new HashMap<String, String>();
            map.put("partName", cursor.getString(cursor.getColumnIndex(MarkDataBase.SUBJECT_NAME)));
            map.put("partScore", cursor.getString(cursor.getColumnIndex(MarkDataBase.SCORE)));

            items.add(map);
        }

        cursor.close();
        dataBaseHelper.close();
        myDataBase.close();

        Log.d("aaahhhh", new Integer(items.size()).toString());

        return items;
    }

    /*public String getDescription(String name, int index) {
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        Cursor cursor = myDataBase.query(name, new String[] {
                DataBaseHelper._ID, MarkDataBase.TITLE, MarkDataBase.DATE, MarkDataBase.DESCRIPTION },
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToPosition(index);
        String description = cursor.getString(cursor.getColumnIndex(MarkDataBase.DESCRIPTION));
        cursor.close();
        dataBaseHelper.close();
        myDataBase.close();

        return description;
    }  */

    public ArrayList<HashMap<String, String>> getSubjectItems() {
        ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        Cursor cursor = myDataBase.query(MarkDataBase.TABLE_NAME, new String[] {
                DataBaseHelper._ID, MarkDataBase.TRUE_NAME, MarkDataBase.SUM_SCORE, MarkDataBase.NAME },
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(MarkDataBase.TRUE_NAME));

            map = new HashMap<String, String>();
            map.put("subjectName", name);
            map.put("score", cursor.getString(cursor.getColumnIndex(MarkDataBase.SUM_SCORE)));
            items.add(map);
        }

        cursor.close();
        dataBaseHelper.close();
        myDataBase.close();

        //Log.d("offfff", new Integer(items.size()).toString());

        return items;
    }

    boolean checkPart(String name, String part) {
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        boolean result = false;

        Cursor cursor = myDataBase.query(name, new String[] {
                DataBaseHelper._ID, MarkDataBase.SUBJECT_NAME, MarkDataBase.SCORE },
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            if (part.equals(cursor.getString(cursor.getColumnIndex(MarkDataBase.SUBJECT_NAME)))) {
                result = true;
            }


        }

        cursor.close();
        dataBaseHelper.close();
        myDataBase.close();

        return result;
    }

    public void setArticle(String name, ArrayList<SubjectNode> nodes) {
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        myDataBase.delete(name, null, null);

        for (int i = 0; i < nodes.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MarkDataBase.SUBJECT_NAME, nodes.get(i).getSubject());
            contentValues.put(MarkDataBase.SCORE, nodes.get(i).getScore());
            //if (!checkPart(name, nodes.get(i).getSubject())) {
            myDataBase.insert(name, null, contentValues);
            //}
        }

        dataBaseHelper.close();
        myDataBase.close();
    }

    /*
    public String getTrueName(String name) {
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        String trueName = "";

        Cursor cursor = myDataBase.query(MarkDataBase.TABLE_NAME, new String[] {
                DataBaseHelper._ID, MarkDataBase.LINK, MarkDataBase.TRUE_NAME, MarkDataBase.NAME },
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            trueName = cursor.getString(cursor.getColumnIndex(MarkDataBase.NAME));

            if (name.equals(cursor.getString(cursor.getColumnIndex(MarkDataBase.TRUE_NAME))))   {
                break;
            }
        }

        cursor.close();
        dataBaseHelper.close();
        myDataBase.close();

        return trueName;
    }

    public void delete(int index, String name) {
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        myDataBase.delete(RSSDataBase.TABLE_NAME, DataBaseHelper._ID + " = " + index, null);
        myDataBase.execSQL("DROP TABLE IF EXISTS "+ name);

        dataBaseHelper.close();
        myDataBase.close();
    }

    public int getId(String name) {
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        int result = 0;

        Cursor cursor = myDataBase.query(RSSDataBase.TABLE_NAME, new String[] {
                DataBaseHelper._ID, RSSDataBase.LINK, RSSDataBase.TRUE_NAME, RSSDataBase.NAME },
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            result = cursor.getInt(cursor.getColumnIndex(DataBaseHelper._ID));

            if (name.equals(cursor.getString(cursor.getColumnIndex(RSSDataBase.TRUE_NAME))))   {
                break;
            }
        }

        cursor.close();
        dataBaseHelper.close();
        myDataBase.close();

        return result;
    }  */

    public String getName(int index) {
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        Cursor cursor = myDataBase.query(MarkDataBase.TABLE_NAME, new String[] {
                DataBaseHelper._ID, MarkDataBase.TRUE_NAME, MarkDataBase.SUM_SCORE, MarkDataBase.NAME },
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToPosition(index);
        String name = cursor.getString(cursor.getColumnIndex(MarkDataBase.NAME));
        cursor.close();
        dataBaseHelper.close();
        myDataBase.close();

        return name;
    }

    /*
    public String getLink(int index) {
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        Cursor cursor = myDataBase.query(RSSDataBase.TABLE_NAME, new String[] {
                DataBaseHelper._ID, RSSDataBase.LINK, RSSDataBase.LINK, RSSDataBase.NAME },
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToPosition(index);
        String link = cursor.getString(cursor.getColumnIndex(RSSDataBase.LINK));
        cursor.close();
        dataBaseHelper.close();
        myDataBase.close();

        return link;
    }

    public int getTableCount() {
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        Cursor cursor = myDataBase.query(RSSDataBase.TABLE_NAME, new String[] {
                DataBaseHelper._ID, RSSDataBase.LINK, RSSDataBase.LINK, RSSDataBase.NAME },
                null,
                null,
                null,
                null,
                null
        );

        int answer = cursor.getCount();

        dataBaseHelper.close();
        myDataBase.close();
        cursor.close();

        return answer;
    }  */

    private boolean checkSubject(String subjectName) {
        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        boolean result = false;

        Cursor cursor = myDataBase.query(MarkDataBase.TABLE_NAME, new String[] {
                DataBaseHelper._ID, MarkDataBase.TRUE_NAME, MarkDataBase.SUM_SCORE, MarkDataBase.NAME },
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

            if (subjectName.equals(cursor.getString(cursor.getColumnIndex(MarkDataBase.TRUE_NAME)))) {
                result = true;
            }


        }

        cursor.close();
        dataBaseHelper.close();
        myDataBase.close();

        return result;
    }

    public void addTable(String subjectName, String sumScore) {

        if (checkSubject(subjectName)) {
            return;
        }

        counterTables++;

        dataBaseHelper = new DataBaseHelper(context);
        myDataBase = dataBaseHelper.getWritableDatabase();

        String request =  "CREATE TABLE "
                + "table" + counterTables + " (" + DataBaseHelper._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MarkDataBase.SUBJECT_NAME + " TEXT,"
                + MarkDataBase.SCORE + " TEXT);";

        try {
            myDataBase.execSQL(request);

            ContentValues contentValues = new ContentValues();
            contentValues.put(NAME, "table" + counterTables);
            contentValues.put(SUM_SCORE, sumScore);
            contentValues.put(TRUE_NAME, subjectName);

            myDataBase.insert(TABLE_NAME, null, contentValues);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        dataBaseHelper.close();
        myDataBase.close();

    }

    public class DataBaseHelper extends SQLiteOpenHelper implements BaseColumns {

        private final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " (" + DataBaseHelper._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TRUE_NAME + " TEXT,"
                + SUM_SCORE + " TEXT,"
                + NAME + " TEXT);";

        private static final String DELETE_TABLE = "DROP TABLE IF EXISTS "
                + TABLE_NAME;

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        }
    }
}
