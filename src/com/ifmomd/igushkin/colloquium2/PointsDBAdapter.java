package com.ifmomd.igushkin.colloquium2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sergey on 11/8/13.
 */
public class PointsDBAdapter {

    private static final String LOG_TAG = "PointsDBAdapter";

    public static final String KEY_ID              = "_id";
    public static final String SUBJECTS_TABLE_NAME = "subjects";
    public static final String POINTS_TABLE_NAME   = "points";

    //Subjects table
    public static final String KEY_SUBJECT                 = "subject";
    public static final String SUBJECTS_TABLE_CREATE_QUERY = "CREATE TABLE " + SUBJECTS_TABLE_NAME + " (" +
                                                             KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                             KEY_SUBJECT + " TEXT, " +
                                                             "UNIQUE (" + KEY_SUBJECT + ") ON CONFLICT IGNORE)";

    //Points table
    public static final String KEY_SUBJECT_ID            = "subject_id";
    public static final String KEY_POINTS                = "points";
    public static final String KEY_DESCRIPTION           = "description";
    public static final String POINTS_TABLE_CREATE_QUERY = "CREATE TABLE " + POINTS_TABLE_NAME + " (" +
                                                           KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                           KEY_SUBJECT_ID + " INTEGER, " +
                                                           KEY_POINTS + " INTEGER, " +
                                                           KEY_DESCRIPTION + " TEXT, " +
                                                           "FOREIGN KEY (" + KEY_SUBJECT_ID + ") REFERENCES " +
                                                           SUBJECTS_TABLE_NAME + "(" + KEY_ID + ") ON DELETE CASCADE)";


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME    = "points.db";
    private static final int    DATABASE_VERSION = 13;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        @Override
        public void onOpen(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON");
        }

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(SUBJECTS_TABLE_CREATE_QUERY);
            db.execSQL(POINTS_TABLE_CREATE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                           + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SUBJECTS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + POINTS_TABLE_NAME);
            onCreate(db);
        }
    }

    public PointsDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public PointsDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createSubject(String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SUBJECT, name);
        return mDb.insert(SUBJECTS_TABLE_NAME, null, initialValues);
    }

    public long createPoints(long subjectId, String description, int amount) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SUBJECT_ID, subjectId);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_POINTS, amount);

        return mDb.insert(POINTS_TABLE_NAME, null, initialValues);
    }

    public boolean deleteSubject(long id) {
        return mDb.delete(SUBJECTS_TABLE_NAME, KEY_ID + "=" + id, null) > 0;
    }

    public static final String KEY_POINTS_SUM = "points_sum";

    public Cursor fetchAllSubjects() {
        return mDb.query(
                SUBJECTS_TABLE_NAME
                , new String[]{KEY_ID, KEY_SUBJECT, "(SELECT SUM("+POINTS_TABLE_NAME+"."+KEY_POINTS+") FROM "+POINTS_TABLE_NAME+" WHERE "+POINTS_TABLE_NAME+"."+KEY_SUBJECT_ID+"="+SUBJECTS_TABLE_NAME+"."+ KEY_ID+") AS " +KEY_POINTS_SUM}
                , null, null, null, null, KEY_SUBJECT + " ASC");
    }

    public Cursor fetchSubjectById(long id) {
        return mDb.query(
                SUBJECTS_TABLE_NAME
                , new String[]{KEY_ID, KEY_SUBJECT, "(SELECT SUM("+POINTS_TABLE_NAME+"."+KEY_POINTS+") FROM "+POINTS_TABLE_NAME+" WHERE "+POINTS_TABLE_NAME+"."+KEY_SUBJECT_ID+"="+SUBJECTS_TABLE_NAME+"."+ KEY_ID+") AS " +KEY_POINTS_SUM}
                , KEY_ID +"="+id, null, null, null, null);
    }

    public Cursor fetchPointsBySubject(long subjectId) {
        return mDb.query(POINTS_TABLE_NAME, new String[]{KEY_ID, KEY_DESCRIPTION, KEY_POINTS}, KEY_SUBJECT_ID + "=" + subjectId, null, null, null, KEY_ID + " DESC");
    }

    /*
    public Cursor fetchAllChannels() {
        return mDb.query(CHANNELS_TABLE_NAME, new String[]{KEY_ID, KEY_NAME,
                KEY_LINK, KEY_LAST_UPDATE}, null, null, null, null, null);
    }

    public Cursor fetchChannelById(long id) {
        return mDb.query(CHANNELS_TABLE_NAME, new String[]{KEY_ID, KEY_NAME,
                KEY_LINK, KEY_LAST_UPDATE}, KEY_ID + "=" + id, null, null, null, null);
    }

    public RSSChannel getChannelById(long id) {
        Cursor c = fetchChannelById(id);
        if (c.getCount() > 0)
            c.moveToFirst();
        else
            return null;
        RSSChannel result = new RSSChannel();
        result.id = id;
        result.title = c.getString(c.getColumnIndex(KEY_NAME));
        result.link = c.getString(c.getColumnIndex(KEY_LINK));
        result.lasUpdated = c.getLong(c.getColumnIndex(KEY_LAST_UPDATE));
        return result;
    }
    */
    public boolean updateSubjectInfo(long id, String subject) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_SUBJECT, subject);
        return mDb.update(SUBJECTS_TABLE_NAME, newValues, KEY_ID + "=" + id, null) > 0;
    }

    public boolean updatePointsInfo(long id, String desc, int amount) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_DESCRIPTION, desc);
        newValues.put(KEY_POINTS, amount);
        return mDb.update(POINTS_TABLE_NAME, newValues, KEY_ID + "=" + id, null) > 0;
    }

    public boolean deleteSubjectById(long id) {
        return mDb.delete(SUBJECTS_TABLE_NAME, KEY_ID + "=" + id, null) > 0;
    }

    public boolean deletePointsById(long id) {
        return mDb.delete(POINTS_TABLE_NAME, KEY_ID + "=" + id, null) > 0;
    }

    /*
    public Cursor fetchItem(long id) throws SQLException {
        Cursor mCursor =

                mDb.query(true, ITEMS_TABLE_NAME, new String[]{KEY_ID,
                        KEY_TITLE, KEY_DESCRIPTION, KEY_LINK, KEY_DATE_TIME, KEY_CHANNEL_ID}, KEY_ID + "=" + id, null,
                          null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    */

}
