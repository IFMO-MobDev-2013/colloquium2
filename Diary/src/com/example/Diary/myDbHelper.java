package com.example.Diary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 08.11.13
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
public class myDbHelper {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SNAME = "subj_name";

    public static final String KEY_TNAME = "task_name";
    public static final String KEY_SUBID = "sub_id";
    public static final String KEY_POINT = "points";
    public static final String KEY_SUM = "sum";


    private static final String DATABASE_NAME = "MyDatabase";

    private static final String TABLE_SUBJECTS = "subjects";
    private static final String TABLE_TASKS = "tasks";

    private static final int DATABASE_VERSION = 2;

    public static final String TAG = "FUCKENFUCK";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;


    private static final String CREATE_TABLE_SUBJECTS = "create table " + TABLE_SUBJECTS + " ( " +
            KEY_ROWID + " integer primary key autoincrement, " +
            KEY_SNAME + " text not null unique );";

    private static final String CREATE_TABLE_TASKS = "create table " + TABLE_TASKS + " ( " +
            KEY_ROWID + " integer primary key autoincrement, " +
            KEY_TNAME + " string not null unique, " +
            KEY_SUBID + " integer not null, " +
            KEY_POINT + " integer not null );";


    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_SUBJECTS);
            db.execSQL(CREATE_TABLE_TASKS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    public myDbHelper(Context ctx) {
        this.mCtx = ctx;
    }

    public myDbHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        try {
            mDb = mDbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            try {
                mDb = mDbHelper.getReadableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
                return this;
            }
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    //--------------------------------------------------------------------
    // DROPS

    public void dropSubjects() {
        open();
        try {
            mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
            mDb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SUBJECTS + " ( " +
                    KEY_ROWID + " integer primary key autoincrement, " +
                    KEY_SNAME + " text not null unique, " +
                    KEY_SUM + " integer );");
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
        close();
    }

    public void dropTasks() {
        open();
        try {
            mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            mDb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( " +
                    KEY_ROWID + " integer primary key autoincrement, " +
                    KEY_TNAME + " string not null unique, " +
                    KEY_SUBID + " integer not null, " +
                    KEY_POINT + " integer not null );");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropAll() {
        open();
        try {
            mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
            mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            mDb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SUBJECTS + " ( " +
                    KEY_ROWID + " integer primary key autoincrement, " +
                    KEY_SNAME + " text not null unique, " +
                    KEY_SUM + " integer );");
            mDb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( " +
                    KEY_ROWID + " integer primary key autoincrement, " +
                    KEY_TNAME + " string not null unique, " +
                    KEY_SUBID + " integer not null, " +
                    KEY_POINT + " integer not null );");
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
        close();
    }

    public boolean deleteSubj(long rowId) {
        try {
            return mDb.delete(TABLE_SUBJECTS, KEY_ROWID + "=" + rowId, null) > 0;
        } catch (Exception e) {
            Log.w(TAG, "deleteSubj - " + e.toString());
            return false;
        }
    }

    public boolean deleteTask(long rowId) {     ///  TODO: can be a propoblem! what the ID???
        try {
            return mDb.delete(TABLE_TASKS, KEY_ROWID + "=" + rowId, null) > 0;
        } catch (Exception e) {
            Log.w(TAG, "deleteTask - " + e.toString());
            return false;
        }
    }

    //-----------------------------------------------------------------
    //CREATES
    public boolean createSubject(String name, Integer p) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SNAME, name);
        initialValues.put(KEY_SUM, p);

        try {
            return mDb.insert(TABLE_SUBJECTS, null, initialValues) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "createSubject - " + e.toString());
            return false;
        }
    }

    public boolean createTask(String name, Integer points, String subj) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TNAME, name);
        initialValues.put(KEY_POINT, points);

        open();
        Cursor cursor = null;
        long id = -1;
        try {
            id = fetchSubjId(subj);
        } catch (Exception e) {
            Log.w(TAG, "createTask - " + e.toString());
        }

        if (id == -1)
            return false;

        initialValues.put(KEY_SUBID, new Integer((int)id));

        try {
            return mDb.insert(TABLE_TASKS, null, initialValues) > 0;
        } catch (Exception e) {
            Log.w(TAG, "createTask - " + e.toString());
            e.printStackTrace();
            return false;
        }
    }


    //----------------------------------------------------------------------
    //SELECTS

    public long fetchSubjId(String subj) {
        Cursor cursor = null;
        long res = -1;
        try {
            cursor = mDb.query(TABLE_SUBJECTS, new String[]{KEY_ROWID}, KEY_SNAME + "=?", new String[]{subj}, null, null, null, null);
            cursor.moveToNext();
            res = cursor.getInt(cursor.getColumnIndex(KEY_ROWID));
        } catch (Exception e) {
            Log.w(TAG, "fetchSubjId - " + e.toString());

        } finally {
            return res;
        }
    }

    public Cursor fetchAllSubj() {
        Cursor cursor = null;

        open();
        try {
            cursor = mDb.query(TABLE_SUBJECTS, new String[]{KEY_ROWID, KEY_SNAME, KEY_SUM}, null, null, null, null, null);

        } catch (Exception e) {
            Log.w(TAG, "fetchAllSuj - " + e.toString());

        } finally {

            return cursor;
        }

    }

    public Cursor fetchTasks(Integer sub_id) {
        Cursor cursor = null;

        try {
            cursor = mDb.query(TABLE_TASKS, new String[]{KEY_ROWID, KEY_TNAME, KEY_POINT}, KEY_SUBID + "=" + sub_id.toString(), null, null, null, null);

        } catch (Exception e) {
            Log.w(TAG, "fetchTasks - " + e.toString());

        } finally {
            return cursor;
        }
    }
//
//    public Cursor fetchALLSubjAndPoints(long rowId) {
//        Cursor cursor = null;
//
//        open();
//        try {
////            cursor = mDb.execSQL("SELECT " + TABLE_SUBJECTS + "." + KEY_ROWID + KEY_SNAME + " SUM(" + KEY_POINT + ") FROM "
////                    + TABLE_TASKS + ", " + TABLE_SUBJECTS + " WHERE " + KEY_SUBID" = " + rowId);
////            mDb.execSQL("SELECT " + TABLE_SUBJECTS + "." + KEY_ROWID + KEY_SNAME + " SUM(" + KEY_POINT + ") FROM " + TABLE_TASKS + ", " + TABLE_SUBJECTS + " WHERE " + KEY_SUBID + " = " + rowId );
//
//
//        } catch (Exception e) {
//            Log.w(TAG, "fetchAllSuj - " + e.toString());
//
//        } finally {
//
//            return cursor;
//        }
//    }


    //------------------------------------------------------
    //EDITS: later


}
