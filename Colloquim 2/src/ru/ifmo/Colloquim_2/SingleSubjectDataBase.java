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
public class SingleSubjectDataBase {

    public static final String KEY_TASK = "task";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SCORE = "score";


    private static final String TAG = "SingleSubjectDataBase";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    private final String DATABASE_NAME;
    private final String DATABASE_TABLE;


    private final String DATABASE_CREATE;

    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

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

    public SingleSubjectDataBase(Context ctx, String subject) {
        this.mCtx = ctx;
        DATABASE_NAME = "SingleSubjectData_" + subject;
        DATABASE_TABLE = "SingleSubjectDatabase_" + subject;
        DATABASE_CREATE = "CREATE TABLE "
                + DATABASE_TABLE + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TASK + ", " + KEY_SCORE + ", UNIQUE(" + KEY_TASK + "));";
    }

    public SingleSubjectDataBase open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public long addTask(String task, int score) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TASK, task);
        initialValues.put(KEY_SCORE, score);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    static int a;
    public boolean deleteTask(String keySubj) {
        return mDb.delete(DATABASE_TABLE, KEY_TASK + "=" + "'" + keySubj + "'", null) > 0;

    }

    public Cursor getAllTasks() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TASK, KEY_SCORE}, null, null, null, null, null);

    }

    public Cursor getTask(String task) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TASK}, KEY_TASK + "='" + task + "'", null,
                        null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;


    }

    public boolean updateTask(String oldName, String name, int score) {
        ContentValues args = new ContentValues();
        args.put(KEY_TASK, name);
        args.put(KEY_SCORE, score);

        return mDb.update(DATABASE_TABLE, args, KEY_TASK + "='" + oldName + "'", null) > 0;
    }

}
