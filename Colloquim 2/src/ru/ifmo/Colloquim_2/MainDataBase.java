package ru.ifmo.Colloquim_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: asus
 * Date: 06.11.13
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public class MainDataBase {

    public static final String KEY_SUBJ = "subject";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SCORE = "score";


    private static final String TAG = "MainDataBase";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    private static final String DATABASE_NAME = "SubjectData";
    private static final String DATABASE_TABLE = "SubjectDatabase";

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + DATABASE_TABLE + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_SUBJ + ", " + KEY_SCORE + ", UNIQUE(" +KEY_SUBJ + "));";

    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public MainDataBase(Context ctx) {
        this.mCtx = ctx;
    }

    public MainDataBase open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public long addSubj(String subj, int score) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SUBJ, subj);
        initialValues.put(KEY_SCORE, score);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }


    public boolean deleteSubj(String keySubj) {
        return mDb.delete(DATABASE_TABLE, KEY_SUBJ + "=" + "'" + keySubj + "'", null) > 0;

    }


    public Cursor getAllSubjs() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SUBJ, KEY_SCORE}, null, null, null, null, null);

    }

    public Cursor getSubj(String subj) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SUBJ}, KEY_SUBJ + "='" + subj + "'", null,
                        null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;


    }

    public boolean updateSubj(String oldName, String name, int score) {
        ContentValues args = new ContentValues();
        args.put(KEY_SUBJ, name);
        args.put(KEY_SCORE, score);

        return mDb.update(DATABASE_TABLE, args, KEY_SUBJ + "='" + oldName + "'", null) > 0;
    }

}
